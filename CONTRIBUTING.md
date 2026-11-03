# Contributing to Canopy

Thank you for contributing to Canopy! Canopy is an offline-first Android application designed to make bird detection, acoustic AI, and local-first LLM challenges accessible and private.

## Getting Started Locally

### Prerequisites
- Android Studio with Android SDK API 35
- JDK 17
- Android device or emulator running Android 8.0 (API 26) or newer

### Building from Source
```bash
git clone https://github.com/atharhive/canopy.git
cd canopy
./gradlew assembleDebug
```

The debug APK is generated at:
```text
app/build/outputs/apk/debug/app-debug.apk
```

Install it on a connected device via:
```bash
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

## Pull Request Guidelines
- Keep pull requests focused on a single feature, bug fix, or accessibility enhancement.
- Follow Kotlin, Java, and Android XML conventions.
- Never commit signing keys, keystores, or model binaries to the git repository.
- Verify that both the normal path and offline fallback (without local Gemma) function properly.

## Maintainer Responsiveness SLA
We aim to review pull requests and answer issues within **24 hours**.

## Code of Conduct
Please maintain a respectful, constructive, and welcoming environment for all contributors.
