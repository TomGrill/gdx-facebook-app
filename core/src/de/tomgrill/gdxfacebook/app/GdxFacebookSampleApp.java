package de.tomgrill.gdxfacebook.app;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox.CheckBoxStyle;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import de.tomgrill.gdxfacebook.app.actors.BitmapFontActor;
import de.tomgrill.gdxfacebook.app.actors.ButtonActor;
import de.tomgrill.gdxfacebook.core.GDXFacebook;
import de.tomgrill.gdxfacebook.core.GDXFacebookCallback;
import de.tomgrill.gdxfacebook.core.GDXFacebookConfig;
import de.tomgrill.gdxfacebook.core.GDXFacebookError;
import de.tomgrill.gdxfacebook.core.GDXFacebookGraphRequest;
import de.tomgrill.gdxfacebook.core.GDXFacebookGraphResult;
import de.tomgrill.gdxfacebook.core.GDXFacebookLoginResult;
import de.tomgrill.gdxfacebook.core.GDXFacebookSystem;

public class GdxFacebookSampleApp extends ApplicationAdapter {

	private static final String TAG = GdxFacebookSampleApp.class.getSimpleName();

	private static final String NOT_LOGGED_IN = "You are not logged in.";

	private Stage stage;
	private ButtonActor loginButton;
	private ButtonActor logoutButton;
	private BitmapFontActor facebookFont;
	private BitmapFontActor autoLogin;

	private CheckBox checkbox;

	private GDXFacebook gdxFacebook;

	private String fbNickname;
	private String fbID;

	private long lastRequest;

	Preferences prefs;

	private GDXFacebookConfig myConfig = new MyFacebookConfig();

	private List<String> permissions = new ArrayList<String>();

	@Override
	public void create() {

		permissions.add("email");
		permissions.add("public_profile");
		permissions.add("user_friends");

		prefs = Gdx.app.getPreferences("gdx-facebook-app-data.txt");

		Gdx.app.setLogLevel(Application.LOG_DEBUG);

		stage = new Stage(new ExtendViewport(640, 800, 640, 800));
		Gdx.input.setInputProcessor(stage);

		gdxFacebook = GDXFacebookSystem.install(myConfig);

		/* facebook */

		loginButton = new ButtonActor(new TextureRegion(new Texture("facebook-button.png")));
		loginButton.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}

			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				handleGUIFacebookSignin();
			}

		});
		loginButton.setX(640 / 2f - 300 / 2f);
		loginButton.setY(600);

		loginButton.setWidth(300);
		loginButton.setHeight(60);
		loginButton.setVisible(false);

		stage.addActor(loginButton);

		logoutButton = new ButtonActor(new TextureRegion(new Texture("logout-button.jpg")));
		logoutButton.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}

			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				logout();
			}

		});
		logoutButton.setX(640 / 2f - 106 / 2f);
		logoutButton.setY(600);

		logoutButton.setWidth(106);
		logoutButton.setHeight(40);
		logoutButton.setVisible(false);

		stage.addActor(logoutButton);

		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("UbuntuMono-Regular.ttf"));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = 15;
		parameter.color = Color.BLACK;
		BitmapFont font = generator.generateFont(parameter);
		generator.dispose();

		facebookFont = new BitmapFontActor(font);
		facebookFont.setX(140);
		facebookFont.setY(560);
		facebookFont.setText("");
		stage.addActor(facebookFont);

		autoLogin = new BitmapFontActor(font);
		autoLogin.setX(235);
		autoLogin.setY(515);
		autoLogin.setText("autosignin on next restart");

		stage.addActor(autoLogin);

		CheckBoxStyle style = new CheckBoxStyle();
		style.font = font;
		style.checkboxOff = new TextureRegionDrawable(new TextureRegion(new Texture("checkbox_no.png")));
		style.checkboxOn = new TextureRegionDrawable(new TextureRegion(new Texture("checkbox_yes.png")));

		checkbox = new CheckBox("", style);
		checkbox.setX(200);
		checkbox.setY(500);
		checkbox.setHeight(240 * 0.1f);
		checkbox.setWidth(300 * 0.1f);

		if (prefs.getBoolean("autosignin", false)) {
			checkbox.setChecked(true);
		}

		checkbox.setVisible(true);
		stage.addActor(checkbox);

		autoSignin();

	}

	private void autoSignin() {
		if (prefs.getBoolean("autosignin", false) && !gdxFacebook.isLoggedIn()) {

			gdxFacebook.loginWithReadPermissions(permissions, new GDXFacebookCallback<GDXFacebookLoginResult>() {

				@Override
				public void onSuccess(GDXFacebookLoginResult result) {
					checkbox.setChecked(true);
					Gdx.app.log(TAG, "Autosignin: User logged in successfully.");
				}

				@Override
				public void onError(GDXFacebookError error) {
					checkbox.setChecked(false);
					Gdx.app.log(TAG, "Autosignin: Error: " + error.getErrorMessage());
				}

				@Override
				public void onCancel() {
					checkbox.setChecked(false);
					Gdx.app.log(TAG, "Autosignin: Something canceled login.");

				}

				@Override
				public void onFail(Throwable t) {
					t.printStackTrace();
				}

			});

		}

	}

	private void handleGUIFacebookSignin() {

		if (!gdxFacebook.isLoggedIn()) {
			gdxFacebook.loginWithReadPermissions(permissions, new GDXFacebookCallback<GDXFacebookLoginResult>() {

				@Override
				public void onSuccess(GDXFacebookLoginResult result) {
					Gdx.app.log(TAG, "User logged in successfully. New AccessToken: " + result.getAccessToken());
				}

				@Override
				public void onError(GDXFacebookError error) {
					Gdx.app.log(TAG, "Error: " + error.getErrorMessage());
				}

				@Override
				public void onCancel() {
					Gdx.app.log(TAG, "User canceled login.");

				}

				@Override
				public void onFail(Throwable t) {
					t.printStackTrace();

				}

			});
		}
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		updateText();
		updateButton();

		stage.draw();

	}

	private void updateButton() {
		if (gdxFacebook.isLoggedIn()) {
			loginButton.setVisible(false);
			logoutButton.setVisible(true);
		} else {
			loginButton.setVisible(true);
			logoutButton.setVisible(false);
		}

	}

	private void logout() {
		gdxFacebook.logOut();
		checkbox.setChecked(false);
		fbNickname = null;
		fbID = null;
	}

	@Override
	public void resume() {
		autoSignin();
	}

	private void updateText() {

		if (gdxFacebook.isLoggedIn()) {
			if (fbNickname == null && fbID == null) {

				if (lastRequest + 10 < TimeUtils.millis() / 1000L) {

					lastRequest = TimeUtils.millis() / 1000L;

					GDXFacebookGraphRequest request = new GDXFacebookGraphRequest().setNode("me").useCurrentAccessToken();

					gdxFacebook.newGraphRequest(request, new GDXFacebookCallback<GDXFacebookGraphResult>() {

						@Override
						public void onSuccess(GDXFacebookGraphResult result) {

							JsonValue root = new JsonReader().parse(result.getResultAsJson());

							fbID = root.getString("id");

							fbNickname = root.getString("name");

							Gdx.app.log(TAG, "Name: " + fbNickname);
							Gdx.app.log(TAG, "ID: " + fbID);

						}

						@Override
						public void onError(GDXFacebookError error) {
							System.out.println(error.getErrorMessage());

							Gdx.app.log(TAG, "Request with error. Something went wrong with the access token.");
							logout();

						}

						@Override
						public void onCancel() {
							Gdx.app.log(TAG, "Request cancelled. Reason unknown.");

						}

						@Override
						public void onFail(Throwable t) {
							t.printStackTrace();
						}
					});

				}
			}

			if (fbNickname != null && fbID != null) {
				facebookFont.setText("Hello " + fbNickname + ", your unique ID is: " + fbID);
			}
		} else {
			facebookFont.setText(NOT_LOGGED_IN);
		}

	}

	@Override
	public void pause() {
		super.pause();

		if (checkbox.isChecked()) {
			prefs.putBoolean("autosignin", true);
		} else {

			prefs.putBoolean("autosignin", false);
		}

		prefs.flush();
	}

	@Override
	public void dispose() {
		stage.dispose();
		facebookFont.dispose();

	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}
}
