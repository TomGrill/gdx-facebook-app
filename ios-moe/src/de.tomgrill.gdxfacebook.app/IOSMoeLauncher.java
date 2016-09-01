package de.tomgrill.gdxfacebook.app;

import com.badlogic.gdx.backends.iosmoe.IOSApplication;
import com.badlogic.gdx.backends.iosmoe.IOSApplicationConfiguration;
import com.intel.moe.natj.general.Pointer;
import ios.foundation.NSAutoreleasePool;
import ios.foundation.NSDictionary;
import ios.uikit.c.UIKit;
import org.robovm.pods.facebook.core.FBSDKAppEvents;


public class IOSMoeLauncher extends IOSApplication.Delegate {

    protected IOSMoeLauncher(Pointer peer) {
        super(peer);
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

    public static void main(String[] argv) {
        NSAutoreleasePool pool = NSAutoreleasePool.alloc();
        UIKit.UIApplicationMain(0, null, null, IOSMoeLauncher.class.getName());
        pool.dealloc();
    }


    @Override
    public void applicationDidBecomeActive(ios.uikit.UIApplication application) {
        super.applicationDidBecomeActive(application);
        // You need to add this line, otherwise Facebook will not work
        // correctly!
        FBSDKAppEvents.activateApp();
    }

    @Override
    public boolean applicationOpenURLOptions(ios.uikit.UIApplication application, ios.foundation.NSURL url, NSDictionary<String, ?> options) {
        // You need to add this line, otherwise Facebook will not work
        // correctly!
//		return FBSDKApplicationDelegate.getSharedInstance().openURL(application, url, sourceApplication, annotation);
        return true;
    }

    @Override
    public void applicationDidFinishLaunching(ios.uikit.UIApplication application) {
//		FBSDKApplicationDelegate.getSharedInstance().didFinishLaunching(application);
        super.applicationDidFinishLaunching(application);
    }


//	@Override
//	public boolean didFinishLaunching(UIApplication application, UIApplicationLaunchOptions launchOptions) {
//		boolean finished = super.didFinishLaunching(application, launchOptions);
//		FBSDKApplicationDelegate.getSharedInstance().didFinishLaunching(application, launchOptions);
//		return finished;
//	}

}