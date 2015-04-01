# gdx-facebook-app
Example libGDX application implementing the gdx-facebook extension.

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



##Get it running

**General**
Import project to your IDE

1. Edit MyFacebookConfig core project. Replace APP_ID with your Facebook App ID. 

**In Android project**

1. Edit res/facebook.xml ``` <string name="facebook_app_id">YOUR_APP_ID</string>``` 

**In iOS project**

1. Edit robovm.properties and replace facebook.* variables with your Facebook App settings. (You may also edit app.* variables depending on your provisioning profiles and certificates.)
2. Open Info.plist.xml and edit this part accordingly 
```<key>FacebookAppID</key>
<string>0123456789</string>
<key>FacebookDisplayName</key>
<string>YOUR_FACEBOOK_APP_NAME</string>
<key>CFBundleURLTypes</key>
<array>
<dict>
<key>CFBundleURLSchemes</key>
<array>
    <string>fb0123456789</string>
</array>
</dict>
</array>
```
 
 
You should be good to go.
