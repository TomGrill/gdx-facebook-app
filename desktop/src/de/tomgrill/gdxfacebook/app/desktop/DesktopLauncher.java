package de.tomgrill.gdxfacebook.app.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import de.tomgrill.gdxfacebook.app.GdxFacebookSampleApp;

public class DesktopLauncher {
	public static void main(String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 640;
		config.height = 800;
		new LwjglApplication(new GdxFacebookSampleApp(), config);
	}
}
