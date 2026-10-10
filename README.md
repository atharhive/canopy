# Canopy

Canopy is an Android application for identifying birds from audio recordings
using the BirdNET machine-learning model.

## Features

- Bird sound recognition from the device microphone
- Location-aware recognition using the BirdNET meta model
- Observation history with species information
- Optional spectrogram and WAV recording support
- Manual location, Bluetooth audio, language, and accessibility settings
- Guided demo mode with a simulated bird call and sample detection
- Optional spoken detection summaries using the device's built-in text-to-speech

## Requirements

- Android Studio
- JDK 17 or newer
- Android SDK with API 35

## Building

Open the project in Android Studio, allow Gradle to synchronize, and run the
`app` configuration on an Android device or emulator.

From a terminal with Java configured:

```bash
./gradlew assembleDebug
```

The generated APK is written to `app/build/outputs/apk/debug/`.

The demo is available from the model download screen. It does not require a
model download and clearly labels its simulated result.

Spoken summaries use Android's local text-to-speech engine. Optional Gemma 3
1B instruction-tuned int4 inference runs on-device through MediaPipe when its
separate model download is complete. The model is approximately 529 MB and is
not bundled in the APK. If it is unavailable, Canopy uses a deterministic
local summary instead. Gemma receives only the BirdNET species and confidence
and is instructed not to change the detected species. ElevenLabs is not part
of the local-first build.

## Model and licensing

The application downloads the BirdNET model on first launch. The model is
licensed separately from this application; review its license and usage
restrictions before distributing builds or using the model commercially.

The application source is distributed under the GNU General Public License,
version 3. See [LICENSE](LICENSE).

## Project

- Application name: Canopy
- Application ID: `com.atharhive.canopy`
- Repository: <https://github.com/atharhive/canopy>
