# gdx-facebook-app
Example libGDX application implementing the [gdx-facebook](https://github.com/TomGrill/gdx-facebook) extension.

## Updates & News
Follow me to receive release updates about this and my other projects (Promise: No BS posts)

https://twitter.com/TomGrillGames and https://www.facebook.com/tomgrillgames

I will also stream sometimes when developing at https://www.twitch.tv/tomgrill and write a blog article from time to time at http://tomgrill.de 

## Setting up your Facebook App
Go to https://developers.facebook.com/apps/ and create a new app. 

**Android**

1. Add a new platform Android.
2. Enable Single Sign On
3. Add the key hashes of your debug and certificate. Read this to find out how to do that. https://developers.facebook.com/docs/android/getting-started/

**Desktop**
nothing to do.

**iOS**

1. Add a new platform iOS
2. Enable Single Sign On
3. Add the Bundle ID. Must be same as your robovm.properties->app.id value.



##Prepare

**General**
Import project to your IDE

Edit [MyFacebookConfig](core/src/de/tomgrill/gdxfacebook/app/MyFacebookConfig.java) Replace APP_ID with your Facebook App ID. 

**In Android project**

Edit res/facebook.xml ``` <string name="facebook_app_id">YOUR_APP_ID</string>``` 

**In iOS project**

Edit robovm.properties and replace facebook.* variables with your Facebook App settings. (You may also edit app.* variables depending on your provisioning profiles and certificates.)


##Run the app

You can probably run this app as you are used to run your libGDX applications. However there is on exception:

**Android:** 

facebook-android-sdk comes as .aar file. There some IDEs (f.e. Eclipse) which are not capable of handling .aar files. This means you need to start your androd project from command line like this:

Linux/Max:
```./gradlew android:installDebug android:run```

Windows:
```gradlew android:installDebug android:run```

