package de.tomgrill.gdxfacebook.app;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.HttpMethods;
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
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import de.tomgrill.gdxfacebook.app.actors.BitmapFontActor;
import de.tomgrill.gdxfacebook.app.actors.ButtonActor;
import de.tomgrill.gdxfacebook.core.GDXFacebook;
import de.tomgrill.gdxfacebook.core.GDXFacebookCallback;
import de.tomgrill.gdxfacebook.core.GDXFacebookCallbackAdapter;
import de.tomgrill.gdxfacebook.core.GDXFacebookError;
import de.tomgrill.gdxfacebook.core.GDXFacebookGraphRequest;
import de.tomgrill.gdxfacebook.core.GDXFacebookGraphResult;
import de.tomgrill.gdxfacebook.core.GDXFacebookLoginResult;
import de.tomgrill.gdxfacebook.core.GDXFacebookSystem;

public class GdxFacebookSampleApp extends ApplicationAdapter {

	private static final String TAG = GdxFacebookSampleApp.class.getSimpleName();

	private static final String NOT_LOGGED_IN = "You are not logged in.";

	private static final String FB_WALL_MESSAGE = "A simple wall post made with gdx-facebook extension.";
	private static final String FB_WALL_LINK = "https://github.com/TomGrill/gdx-facebook";
	private static final String FB_WALL_CAPTION = "gdx-facebook wall post";

	private static int WORLD_WIDTH = 480;
	private static int WORLD_HEIGHT = 640;

	private Stage stage;
	private ButtonActor loginButton;
	private ButtonActor logoutButton;
	private ButtonActor requestPublishPermissionsButton;
	private ButtonActor postButton;
	private BitmapFontActor userLoginText;
	private BitmapFontActor autoLoginText;
	private BitmapFontActor postToWallText;
	private BitmapFontActor publishRequestText;
	private CheckBox checkbox;

	private Texture requestEnabledTexture;
	private Texture requestDisabledTexture;
	private Texture checkboxEnabledTexture;
	private Texture checkboxDisabledTexture;
	private Texture loginButtonTexture;
	private Texture logoutButtonTexture;
	private Texture postButtonTexture;

	private GDXFacebook gdxFacebook;

	private BitmapFont font;

	private Preferences prefs;

	private List<String> permissionsRead = new ArrayList<String>();
	private List<String> permissionsPublish = new ArrayList<String>();

	@Override
	public void create() {
		/* create preferences to store autologin options */
		prefs = Gdx.app.getPreferences("gdx-facebook-app-data.txt");

		createTextures();

		/* set log level to see some log output */
		Gdx.app.setLogLevel(Application.LOG_DEBUG);

		/* create stage and input processor */
		stage = new Stage(new ExtendViewport(WORLD_WIDTH, WORLD_HEIGHT, WORLD_WIDTH, WORLD_HEIGHT));
		Gdx.input.setInputProcessor(stage);

		/* setup GUI */
		setupLoginButton();
		setupLogoutButton();
		setupRequestPublishButton();
		setupPostButton();

		setupFont();

		setupLoginInfoText();
		setupAutoLoginText();
		setupPostToWallText();
		setupPublishRequestText();
		setupAutoLoginCheckbox();

		permissionsRead.add("email");
		permissionsRead.add("public_profile");
		permissionsRead.add("user_friends");

		permissionsPublish.add("publish_actions");

		/**
		 * Install gdx-facebook
		 * 
		 * To fix compiler error: create MyFacebookConfig class in your project
		 * and edit APP_ID field.
		 * 
		 * */
		MyFacebookConfig config = new MyFacebookConfig();
		if (config.APP_ID == null || config.APP_ID.equals("REPLACE_WITH_YOUR_APP_ID")) {
			throw new RuntimeException("You need to set APP_ID in MyFacebookConfig class.");
		}
		gdxFacebook = GDXFacebookSystem.install(config);

		/** perform auto login */
		if (prefs.getBoolean("autosignin", false)) {

			GDXFacebookGraphRequest request = new GDXFacebookGraphRequest().setNode("me").useCurrentAccessToken();

			gdxFacebook.newGraphRequest(request, new GDXFacebookCallbackAdapter<GDXFacebookGraphResult>() {

				@Override
				public void onSuccess(GDXFacebookGraphResult result) {
					JsonValue root = new JsonReader().parse(result.getResultAsJson());

					String fbID = root.getString("id");
					String fbNickname = root.getString("name");

					userLoginText.setText("Hello " + fbNickname + ", your unique ID is: " + fbID);

					setPublishButtonStatus(true);
					setLoginButtonStatus(true);
				}

			});

		}
	}

	private void setupPublishRequestText() {
		publishRequestText = new BitmapFontActor(font);
		publishRequestText.setX(50);
		publishRequestText.setY(385);
		publishRequestText.setText("");
		stage.addActor(publishRequestText);
	}

	private void setupPostToWallText() {
		postToWallText = new BitmapFontActor(font);
		postToWallText.setX(50);
		postToWallText.setY(285);
		postToWallText.setText("");
		stage.addActor(postToWallText);

	}

	private void setupPostButton() {
		postButton = new ButtonActor(postButtonTexture);
		postButton.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}

			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				postToUserWall();
			}

		});
		postButton.setX(WORLD_WIDTH / 2f - 253 / 2f);
		postButton.setY(300);

		postButton.setWidth(253);
		postButton.setHeight(42);

		stage.addActor(postButton);

	}

	private void postToUserWall() {

		GDXFacebookGraphRequest request = new GDXFacebookGraphRequest().setNode("me/feed").useCurrentAccessToken();
		request.setMethod(HttpMethods.POST);
		request.putField("message", FB_WALL_MESSAGE);
		request.putField("link", FB_WALL_LINK);
		request.putField("caption", FB_WALL_CAPTION);
		gdxFacebook.newGraphRequest(request, new GDXFacebookCallback<GDXFacebookGraphResult>() {

			@Override
			public void onSuccess(GDXFacebookGraphResult result) {
				Gdx.app.debug(TAG, "Posted to user wall successful.");
				Gdx.app.debug(TAG, "Response: " + result.getResultAsJson());
				postToWallText.setText("CONGRATS - there is a new post on your wall.");

			}

			@Override
			public void onError(GDXFacebookError error) {
				Gdx.app.error(TAG, "An error occured while trying to post to user wall:" + error.getErrorMessage());
				postToWallText.setText("ERROR OCCURRED - view your log output");

			}

			@Override
			public void onFail(Throwable t) {
				Gdx.app.error(TAG, "Exception occured while trying to post to user wall.");
				postToWallText.setText("EXCEPTION OCCURRED - view your log output");
				t.printStackTrace();

			}

			@Override
			public void onCancel() {
				Gdx.app.debug(TAG, "Post to user wall has been cancelled.");
				postToWallText.setText("POSTING HAS BEEN CANCELED");
			}

		});

	}

	private void createTextures() {
		requestEnabledTexture = new Texture("request_button_enabled.png");
		requestDisabledTexture = new Texture("request_button_disabled.png");

		checkboxEnabledTexture = new Texture("checkbox_yes.png");
		checkboxDisabledTexture = new Texture("checkbox_no.png");

		loginButtonTexture = new Texture("login_button.png");
		logoutButtonTexture = new Texture("logout_button.png");

		postButtonTexture = new Texture("button_post_enabled.png");

	}

	private void setupRequestPublishButton() {
		requestPublishPermissionsButton = new ButtonActor(requestDisabledTexture);
		requestPublishPermissionsButton.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}

			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				loginWithPublishPermissions();
			}

		});
		requestPublishPermissionsButton.setX(WORLD_WIDTH / 2f - 424 / 2f);
		requestPublishPermissionsButton.setY(400);

		requestPublishPermissionsButton.setWidth(424);
		requestPublishPermissionsButton.setHeight(42);

		stage.addActor(requestPublishPermissionsButton);

	}

	private void setupAutoLoginCheckbox() {
		CheckBoxStyle style = new CheckBoxStyle();
		style.font = font;
		style.checkboxOff = new TextureRegionDrawable(new TextureRegion(checkboxDisabledTexture));
		style.checkboxOn = new TextureRegionDrawable(new TextureRegion(checkboxEnabledTexture));

		checkbox = new CheckBox("", style);
		checkbox.setX(130);
		checkbox.setY(470);
		checkbox.setHeight(240 * 0.1f);
		checkbox.setWidth(300 * 0.1f);

		if (prefs.getBoolean("autosignin", false)) {
			checkbox.setChecked(true);
		}

		checkbox.setVisible(true);
		stage.addActor(checkbox);
	}

	private void setupAutoLoginText() {
		autoLoginText = new BitmapFontActor(font);
		autoLoginText.setX(165);
		autoLoginText.setY(485);
		autoLoginText.setText("autosignin on next restart");
		stage.addActor(autoLoginText);
	}

	private void setupLoginInfoText() {
		userLoginText = new BitmapFontActor(font);
		userLoginText.setX(20);
		userLoginText.setY(530);
		userLoginText.setText(NOT_LOGGED_IN);
		stage.addActor(userLoginText);

	}

	private void setupFont() {
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("UbuntuMono-Regular.ttf"));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = 16;
		parameter.color = Color.BLACK;
		font = generator.generateFont(parameter);
		generator.dispose();
	}

	private void setupLogoutButton() {
		logoutButton = new ButtonActor(logoutButtonTexture);
		logoutButton.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}

			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				logout();
			}

		});
		logoutButton.setX(WORLD_WIDTH / 2f - 109 / 2f);
		logoutButton.setY(550);

		logoutButton.setWidth(109);
		logoutButton.setHeight(42);
		logoutButton.setVisible(false);

		stage.addActor(logoutButton);
	}

	private void setupLoginButton() {
		loginButton = new ButtonActor(loginButtonTexture);
		loginButton.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}

			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				loginWithReadPermissions();
			}

		});
		loginButton.setX(WORLD_WIDTH / 2f - 245 / 2f);
		loginButton.setY(550);

		loginButton.setWidth(245);
		loginButton.setHeight(42);

		stage.addActor(loginButton);
	}

	private void loginWithReadPermissions() {

		gdxFacebook.loginWithReadPermissions(permissionsRead, new GDXFacebookCallback<GDXFacebookLoginResult>() {

			@Override
			public void onSuccess(GDXFacebookLoginResult result) {
				Gdx.app.debug(TAG, "READ REQUEST: User logged in successfully.");
				gainUserInfo();
				setPublishButtonStatus(true);
				setLoginButtonStatus(true);
			}

			@Override
			public void onError(GDXFacebookError error) {
				Gdx.app.error(TAG, "READ REQUEST: Error login: " + error.getErrorMessage());
				logout();
			}

			@Override
			public void onCancel() {
				Gdx.app.debug(TAG, "READ REQUEST: User canceled login process");

			}

			@Override
			public void onFail(Throwable t) {
				Gdx.app.error(TAG, "READ REQUEST: Technical error occured:");
				logout();
				t.printStackTrace();
			}

		});

	}

	private void loginWithPublishPermissions() {
		gdxFacebook.loginWithPublishPermissions(permissionsPublish, new GDXFacebookCallback<GDXFacebookLoginResult>() {

			@Override
			public void onSuccess(GDXFacebookLoginResult result) {
				Gdx.app.debug(TAG, "PUBLISH REQUEST: User logged in successfully.");
				gainUserInfo();
				setPublishButtonStatus(true);
				setLoginButtonStatus(true);
				publishRequestText.setText("All good, you can post now");
			}

			@Override
			public void onError(GDXFacebookError error) {
				Gdx.app.error(TAG, "PUBLISH REQUEST: Error login: " + error.getErrorMessage());
				publishRequestText.setText("PUBLISH REQUEST ERROR: view log output");
				logout();
			}

			@Override
			public void onCancel() {
				Gdx.app.debug(TAG, "PUBLISH REQUEST: User canceled login process");
				publishRequestText.setText("PUBLISH REQUEST CANCELLED");

			}

			@Override
			public void onFail(Throwable t) {
				Gdx.app.error(TAG, "PUBLISH REQUEST: Technical error occured:");
				publishRequestText.setText("PUBLISH REQUEST EXCEPTION: view log output");
				logout();
				t.printStackTrace();
			}

		});
	}

	private void setPublishButtonStatus(boolean enabled) {
		if (enabled) {
			requestPublishPermissionsButton.setTexture(requestEnabledTexture);
		} else {
			requestPublishPermissionsButton.setTexture(requestDisabledTexture);
		}

	}

	private void gainUserInfo() {
		GDXFacebookGraphRequest request = new GDXFacebookGraphRequest().setNode("me").useCurrentAccessToken();

		gdxFacebook.newGraphRequest(request, new GDXFacebookCallback<GDXFacebookGraphResult>() {

			@Override
			public void onSuccess(GDXFacebookGraphResult result) {
				JsonValue root = new JsonReader().parse(result.getResultAsJson());

				String fbID = root.getString("id");
				String fbNickname = root.getString("name");

				userLoginText.setText("Hello " + fbNickname + ", your unique ID is: " + fbID);
			}

			@Override
			public void onError(GDXFacebookError error) {
				Gdx.app.error(TAG, "Request with error. Something went wrong with the access token.");
				logout();

			}

			@Override
			public void onCancel() {
				logout();
				Gdx.app.debug(TAG, "Request cancelled. Reason unknown.");

			}

			@Override
			public void onFail(Throwable t) {
				Gdx.app.error(TAG, "Request with exception.");
				logout();
				t.printStackTrace();
			}
		});

	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		stage.draw();
	}

	private void setLoginButtonStatus(boolean loggedIn) {
		if (loggedIn) {
			loginButton.setVisible(false);
			logoutButton.setVisible(true);
		} else {
			loginButton.setVisible(true);
			logoutButton.setVisible(false);
		}

	}

	private void logout() {
		gdxFacebook.logOut();
		userLoginText.setText(NOT_LOGGED_IN);
		checkbox.setChecked(false);
		setPublishButtonStatus(false);
		setLoginButtonStatus(false);
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
		super.dispose();
		stage.dispose();
		font.dispose();

		requestEnabledTexture.dispose();
		requestDisabledTexture.dispose();
		checkboxEnabledTexture.dispose();
		checkboxDisabledTexture.dispose();
		loginButtonTexture.dispose();
		logoutButtonTexture.dispose();
		postButtonTexture.dispose();

	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}
}
