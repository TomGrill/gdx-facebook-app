package de.tomgrill.gdxfacebook.app;

import apple.foundation.NSDictionary;
import apple.foundation.NSURL;
import apple.uikit.UIApplication;
import apple.uikit.c.UIKit;
import com.badlogic.gdx.backends.iosmoe.IOSApplication;
import com.badlogic.gdx.backends.iosmoe.IOSApplicationConfiguration;
import de.tomgrill.gdxfacebook.iosmoe.bindings.sdk.core.fbsdkcorekit.FBSDKAppEvents;
import de.tomgrill.gdxfacebook.iosmoe.bindings.sdk.core.fbsdkcorekit.FBSDKApplicationDelegate;
import org.moe.natj.general.Pointer;


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
        FBSDKAppEvents.activateApp();
        super.applicationDidBecomeActive(application);
    }

    @Override
    public boolean applicationOpenURLOptions(UIApplication app, NSURL url, NSDictionary<String, ?> options) {
        return ((FBSDKApplicationDelegate)FBSDKApplicationDelegate.sharedInstance()).applicationOpenURLOptions(app, url, options);
    }

    @Override
    public boolean applicationDidFinishLaunchingWithOptions(UIApplication application, NSDictionary<?, ?> launchOptions) {
        ((FBSDKApplicationDelegate) FBSDKApplicationDelegate.sharedInstance()).applicationDidFinishLaunchingWithOptions(application, launchOptions);
        return super.applicationDidFinishLaunchingWithOptions(application, launchOptions);
    }
}