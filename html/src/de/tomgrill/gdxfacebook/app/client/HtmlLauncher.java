package de.tomgrill.gdxfacebook.app.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import de.tomgrill.gdxfacebook.app.GdxFacebookSampleApp;

public class HtmlLauncher extends GwtApplication {

    @Override
    public GwtApplicationConfiguration getConfig() {
        GwtApplicationConfiguration cfg = new GwtApplicationConfiguration(640, 800);
        return cfg;
    }

    @Override
    public ApplicationListener createApplicationListener() {
        return new GdxFacebookSampleApp();
    }

}