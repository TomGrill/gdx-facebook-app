package de.tpronold.gdxfacebook.app.android;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import de.tpronold.gdxfacebook.android.GdxFacebookAndroidApplication;
import de.tpronold.gdxfacebook.app.GdxFacebookSampleApp;

public class AndroidLauncher extends GdxFacebookAndroidApplication {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new GdxFacebookSampleApp(), config);
	}
}
