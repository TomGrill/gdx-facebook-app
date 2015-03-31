package de.tomgrill.gdxfacebook.app;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.HttpResponse;
import com.badlogic.gdx.Net.HttpResponseListener;
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
import de.tomgrill.gdxfacebook.core.FacebookAPI;
import de.tomgrill.gdxfacebook.core.FacebookConfig;
import de.tomgrill.gdxfacebook.core.FacebookSystem;
import de.tomgrill.gdxfacebook.core.ResponseError;
import de.tomgrill.gdxfacebook.core.ResponseListener;

public class GdxFacebookSampleApp extends ApplicationAdapter {

	private static final String TAG = GdxFacebookSampleApp.class.getName();

	private static final String NOT_LOGGED_IN = "You are not logged in.";

	private Stage stage;
	private ButtonActor facebookButton;
	private BitmapFontActor facebookFont;
	private BitmapFontActor autoLogin;

	private CheckBox checkbox;

	private FacebookAPI facebookAPI;

	private String fbNickname;
	private String fbID;

	private long lastRequest;

	Preferences prefs;

	private FacebookConfig myConfig = new MyFacebookConfig();

	@Override
	public void create() {

		prefs = Gdx.app.getPreferences("gdx-facebook-app-data.txt");

		Gdx.app.setLogLevel(Application.LOG_DEBUG);

		stage = new Stage(new ExtendViewport(640, 800, 640, 800));
		Gdx.input.setInputProcessor(stage);

		FacebookSystem facebookSystem = new FacebookSystem(myConfig);
		facebookAPI = facebookSystem.getFacebookAPI();

		/* facebook */

		facebookButton = new ButtonActor(new TextureRegion(new Texture("facebook-button.png")));
		facebookButton.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}

			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				handleGUIFacebookSignin();
			}

		});
		facebookButton.setX(640 / 2 - 300 / 2);
		facebookButton.setY(600);

		facebookButton.setWidth(300);
		facebookButton.setHeight(60);

		stage.addActor(facebookButton);

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
		if (prefs.getBoolean("autosignin", false) && facebookAPI.isLoaded()) {
			facebookAPI.signin(false, new ResponseListener() {
				@Override
				public void success() {
					checkbox.setChecked(true);
					Gdx.app.log(TAG, "Autosignin: User logged in successfully.");
				}

				@Override
				public void error(ResponseError responseError) {
					checkbox.setChecked(false);
					Gdx.app.log(TAG, "Autosignin: Error: " + responseError.getMessage() + "(Error Code: " + responseError.getCode() + ")");
				}

				@Override
				public void cancel() {
					checkbox.setChecked(false);
					Gdx.app.log(TAG, "Autosignin: Something canceled login.");
				}
			});
		}

	}

	private void handleGUIFacebookSignin() {
		if (facebookAPI.isLoaded()) {
			facebookAPI.signin(true, new ResponseListener() {
				@Override
				public void success() {
					Gdx.app.log(TAG, "User logged in successfully.");
				}

				@Override
				public void error(ResponseError responseError) {
					Gdx.app.log(TAG, "Error: " + responseError.getMessage() + "(Error Code: " + responseError.getCode() + ")");
				}

				@Override
				public void cancel() {
					Gdx.app.log(TAG, "User canceled login.");
				}
			});
		}
	}

	private void handleGetUserFriendPermission() {
		myConfig.PERMISSIONS += ",user_friends";
		if (facebookAPI.isLoaded()) {
			facebookAPI.signin(true, new ResponseListener() {
				@Override
				public void success() {
					Gdx.app.log(TAG, "Permission user_friends received");
				}

				@Override
				public void error(ResponseError responseError) {
					Gdx.app.log(TAG, "Error: " + responseError.getMessage() + "(Error Code: " + responseError.getCode() + ")");
				}

				@Override
				public void cancel() {
					Gdx.app.log(TAG, "Could not get permission. Canceled.");
				}
			});

		}
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		updateText();

		stage.draw();

	}

	private void logout() {
		facebookAPI.signout();
		fbNickname = null;
		fbID = null;
	}

	private void updateText() {
		if (facebookAPI.isSignedin()) {
			if (fbNickname == null && fbID == null) {

				if (lastRequest + 10 < TimeUtils.millis() / 1000L) {

					lastRequest = TimeUtils.millis() / 1000L;

					facebookAPI.newGraphRequest("me", "access_token=" + facebookAPI.getAccessToken(), new HttpResponseListener() {

						@Override
						public void handleHttpResponse(HttpResponse httpResponse) {
							if (httpResponse.getStatus().getStatusCode() == 200) {

								String result = httpResponse.getResultAsString();

								Gdx.app.log(TAG, "Request successfull: Responsebody: " + result);

								JsonValue root = new JsonReader().parse(result);

								fbID = root.getString("id");
								fbNickname = root.getString("name");

								Gdx.app.log(TAG, "Name: " + fbNickname);
								Gdx.app.log(TAG, "ID: " + fbID);

							} else {
								Gdx.app.log(TAG, "Request with error. Something went wrong with the access token.");
								logout();
							}

						}

						@Override
						public void failed(Throwable t) {
							Gdx.app.log(TAG, "Request failed. Reason: " + t.getMessage());

						}

						@Override
						public void cancelled() {
							Gdx.app.log(TAG, "Request cancelled. Reason unknown.");

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
}
