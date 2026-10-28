# DrewBlox Android APK project

This is a native Android WebView wrapper for the DrewBlox 3D prototype. It shows an original retro block-game loading screen, then opens the playable studded-baseplate world in landscape mode.

## What it includes
- Android app ID: `com.drewblox.classic`
- App label: DrewBlox
- Custom classic-inspired loading screen with animated transition when the 3D world reports ready
- Bundled world HTML and JavaScript
- Three.js is loaded from jsDelivr, so the game needs an internet connection
- AndroidX WebKit asset loader; local assets are served under a safe HTTPS origin
- GitHub Actions workflow that builds and uploads a debug APK artifact

## Build APK in GitHub using your phone
1. Create a new GitHub repository.
2. Upload all files and folders from this project ZIP to the repository root (keep `.github/workflows/build-apk.yml`).
3. Open the repository's **Actions** tab and select **Build DrewBlox APK**.
4. If needed, tap **Run workflow**. Wait for the workflow to finish successfully.
5. Open the completed workflow run and download the **DrewBlox-Android-APK** artifact.
6. Extract the downloaded artifact ZIP. It contains `app-debug.apk`.
7. Open the APK on your Android phone and allow installation from your browser/files app if Android asks. Only install APKs you built or trust.

## Local build
Requires Java 17, Gradle 8.9, and Android SDK platform 35. Run:
`gradle :app:assembleDebug`

The output is `app/build/outputs/apk/debug/app-debug.apk`.

## Honest limitations
- I cannot compile and test the APK in this environment because Android SDK/Gradle build tools are not installed here. The included workflow is configured to build it in GitHub Actions; the APK does not exist until that build succeeds.
- This is the playable 3D prototype, not the full DrewBlox website/backend. Registration, login, profiles, multiplayer, and persistent game data are not connected in this standalone APK.
- The 3D world uses Three.js from a CDN and therefore needs internet.
- The loading screen is original and classic-inspired, not a copy of Roblox's logo or exact proprietary screen.
