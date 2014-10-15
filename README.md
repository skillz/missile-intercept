# Missile Intercept

This repo contains an example [Skillz](http://skillz.com) Android integration using a simple Missle Defender clone. The Skillz SDK is not included in this project and should be download on the [Skillz Developer Portal](https://skillz.com/developer/downloads).

## Building

1. Import to Android Studio using **File -> Import Project**.
2. Import Skillz SDK using **File -> Import Module**.
3. Edit AndroidManifest.xml in the newly imported Skillz module. Remove all content contained in the manifest tags. This content is example content and should be stripped to avoid interfering with a fully integrated project. 
4. Right click on the app module and select module settings, Add the Skillz module as a module dependency.
5. Clean project and build.
