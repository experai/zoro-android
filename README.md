# ZORO Android

This repository contains an Android app scaffold for a ZORO background assistant with:

- Foreground Service
- Microphone permission and background listening placeholder
- Notification-based background status
- Boot recovery / startup handling
- Mode selection UI for Always Available, Energiesaver, Privacy, Headset, Offline, Standard
- Home screen style UI for app control

Important: This is a technical foundation, not a complete production-grade wake-word assistant. On Android, a real always-on "Hey ZORO" wake-word experience requires a local audio pipeline, wake-word model, and device-specific tuning.

## Repository

https://github.com/experai/zoro-android

## Requirements

- Android Studio (latest stable)
- JDK 17
- Android SDK 34

## Build instructions

1. Install Android Studio.
2. Open Android Studio.
3. Choose `Open` and select this project folder.
4. Let Gradle sync complete.
5. If Android SDK components are missing, install the requested SDK/Build Tools.
6. Connect a physical Android device or use an emulator.
7. Click `Run app` or `Build > Build APK`.
8. Install the produced APK on your Android device.

## Notes

- Foreground service requires Android 8+ and direct user-visible notification.
- Background microphone access is subject to Android OEM restrictions.
- Real wake-word detection should be implemented with a local engine such as Porcupine, OpenWakeWord, or a custom model pipeline.
- iOS does not support a comparable custom background wake-word flow for third-party apps in the same way.

## Expected result

This project is intended as a starting point for:

- app UI
- start/stop assistant service
- background status notification
- boot recovery
- app operational modes

It is not yet a fully polished, publishable "always-on personal assistant".
