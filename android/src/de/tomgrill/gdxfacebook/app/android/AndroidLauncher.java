package de.tomgrill.gdxfacebook.app.android;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import de.tomgrill.gdxfacebook.app.GdxFacebookSampleApp;
import de.tpronold.gdxfacebook.android.GdxFacebookAndroidApplication;

public class AndroidLauncher extends GdxFacebookAndroidApplication {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new GdxFacebookSampleApp(), config);
	}
}
