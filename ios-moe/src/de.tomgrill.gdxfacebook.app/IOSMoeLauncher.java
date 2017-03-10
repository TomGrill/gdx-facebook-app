package de.tomgrill.gdxfacebook.app;

import apple.foundation.NSDictionary;
import apple.foundation.NSURL;
import apple.uikit.UIApplication;
import apple.uikit.c.UIKit;
import com.badlogic.gdx.backends.iosmoe.IOSApplication;
import com.badlogic.gdx.backends.iosmoe.IOSApplicationConfiguration;
import org.moe.natj.general.Pointer;
import org.robovm.pods.facebook.core.FBSDKAppEvents;


public class IOSMoeLauncher extends IOSApplication.Delegate {

    protected IOSMoeLauncher(Pointer peer) {
        super(peer);
    }

    public static void main(String[] argv) {
        UIKit.UIApplicationMain(0, null, null, IOSMoeLauncher.class.getName());
    }

    @Override
    protected IOSApplication createApplication() {
        IOSApplicationConfiguration config = new IOSApplicationConfiguration();
        config.orientationLandscape = false;
        config.orientationPortrait = true;
        config.useCompass = false;
        config.useAccelerometer = false;
        return new IOSApplication(new GdxFacebookSampleApp(), config);
    }

    @Override
    public void applicationDidBecomeActive(UIApplication application) {
        super.applicationDidBecomeActive(application);
//        // You need to add this line, otherwise Facebook will not work
//        // correctly!
        FBSDKAppEvents.activateApp();
    }

    @Override
    public boolean applicationOpenURLOptions(UIApplication app, NSURL url, NSDictionary<String, ?> options) {
        super.applicationOpenURLOptions(app, url, options);
        throw new RuntimeException("IOSMOE not yet supported. This is not yet implemented properly."); // TODO
//        return FBSDKApplicationDelegate.getSharedInstance().openURL(null, null, null,null );
    }

    @Override
    public void applicationDidFinishLaunching(UIApplication application) {
        super.applicationDidFinishLaunching(application);
        throw new RuntimeException("IOSMOE not yet supported.  This is not yet implemented properly."); // TODO
//        FBSDKApplicationDelegate.getSharedInstance().didFinishLaunching(null, null);
    }
}