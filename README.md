# Android Social App

Prototype. Factory-generated Social app from droid-builder.

Catalog: https://sw-builder.com/appstore/android/apps/android-social-app.html

Builder: https://github.com/Gator-Go/droid-builder

## Build (Unix)

Prerequisites: Git, Groovy, JDK, Android SDK.

Expected sibling directories:

    ~/android/droid-builder
    ~/android/android-social-app

```bash
cd ~/android/android-social-app
git pull
./android-social-build.sh
```
## Layout:
```text
android-social-app/
├── android-social-build.sh
├── Extender/
│   ├── SocialExtender.groovy
│   ├── logo.png
│   ├── ldpi-logo.png
│   ├── mdpi-logo.png
│   ├── hdpi-logo.png
│   ├── xhdpi-logo.png
│   └── xxhdpi-logo.png
└── options/
    ├── APP_ENUMS.xml
    ├── APP_NAMES.xml
    └── APP_TABLES.xml
```
## Note:

The template/ and build/ dirs appear after a build. They come from
droid-builder.

DroidBuilder.groovy is copied in from droid-builder at build time.

SocialExtender.groovy performs functions unique to the social app such
as deploying the social logo images.

The social/ dir appears after a build and is the build output where the
new app is created.