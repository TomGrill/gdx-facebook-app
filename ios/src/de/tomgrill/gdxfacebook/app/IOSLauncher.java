package de.tomgrill.gdxfacebook.app;

import org.robovm.apple.foundation.NSAutoreleasePool;
import org.robovm.apple.foundation.NSPropertyList;
import org.robovm.apple.foundation.NSURL;
import org.robovm.apple.uikit.UIApplication;
import org.robovm.apple.uikit.UIApplicationLaunchOptions;
import org.robovm.pods.facebook.core.FBSDKAppEvents;
import org.robovm.pods.facebook.core.FBSDKApplicationDelegate;

import com.badlogic.gdx.backends.iosrobovm.IOSApplication;
import com.badlogic.gdx.backends.iosrobovm.IOSApplicationConfiguration;

public class IOSLauncher extends IOSApplication.Delegate {
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
		NSAutoreleasePool pool = new NSAutoreleasePool();
		UIApplication.main(argv, null, IOSLauncher.class);
		pool.close();
	}

	@Override
	public void didBecomeActive(UIApplication application) {
		super.didBecomeActive(application);
		// You need to add this line, otherwise Facebook will not work
		// correctly!

		FBSDKAppEvents.activateApp();
	}

	@Override
	public boolean openURL(UIApplication application, NSURL url, String sourceApplication, NSPropertyList annotation) {
		// You need to add this line, otherwise Facebook will not work
		// correctly!

		return FBSDKApplicationDelegate.getSharedInstance().openURL(application, url, sourceApplication, annotation);

	}

	// @Override
	// public void willTerminate(UIApplication application) {
	// // You need to add this line, otherwise Facebook will not work
	// // correctly!
	// FacebookManager.getInstance().handleWillTerminate(application);
	// super.willTerminate(application);
	// }

	@Override
	public boolean didFinishLaunching(UIApplication application, UIApplicationLaunchOptions launchOptions) {
		return FBSDKApplicationDelegate.getSharedInstance().didFinishLaunching(application, launchOptions);
	}

}