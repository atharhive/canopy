# Canopy

<p align="center">
  <img src="docs/canopy-cover.svg" alt="Canopy — listen, learn, explore" width="100%">
</p>

<p align="center">
  <strong>Private, local-first bird intelligence for curious listeners.</strong><br>
  Identify birds from sound, build a personal collection, and turn every walk into a challenge.
</p>

<p align="center">
  <a href="https://github.com/atharhive/canopy/releases/tag/v5.4.0-gemma-beta"><img src="https://img.shields.io/badge/download-beta-0f766e?style=for-the-badge&logo=android&logoColor=white" alt="Download beta"></a>
  <a href="https://github.com/atharhive/canopy/releases"><img src="https://img.shields.io/github/v/release/atharhive/canopy?display_name=tag&style=for-the-badge&color=14b8a6" alt="Latest release"></a>
  <a href="https://github.com/atharhive/canopy/blob/main/LICENSE"><img src="https://img.shields.io/github/license/atharhive/canopy?style=for-the-badge&color=a3e635" alt="License"></a>
  <a href="#demo-video"><img src="https://img.shields.io/badge/demo-video-coming%20soon-ef4444?style=for-the-badge&logo=youtube&logoColor=white" alt="Demo video coming soon"></a>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Android-API%2026%2B-3ddc84?style=flat-square&logo=android&logoColor=white" alt="Android API 26+">
  <img src="https://img.shields.io/badge/Kotlin-2.3.21-7f52ff?style=flat-square&logo=kotlin&logoColor=white" alt="Kotlin">
  <img src="https://img.shields.io/badge/Java-8%2B-ed8b00?style=flat-square&logo=openjdk&logoColor=white" alt="Java">
  <img src="https://img.shields.io/badge/Gradle-8.14.5-02303a?style=flat-square&logo=gradle&logoColor=white" alt="Gradle">
  <img src="https://img.shields.io/badge/Android%20Gradle%20Plugin-8.12.2-3ddc84?style=flat-square&logo=android&logoColor=white" alt="Android Gradle Plugin">
  <img src="https://img.shields.io/badge/LiteRT-1.2.0-4285f4?style=flat-square&logo=google&logoColor=white" alt="LiteRT">
  <img src="https://img.shields.io/badge/MediaPipe%20GenAI-0.10.27-ff6f00?style=flat-square&logo=google&logoColor=white" alt="MediaPipe GenAI">
  <img src="https://img.shields.io/badge/Gemma-3%201B%20int4-8e75b2?style=flat-square&logo=google&logoColor=white" alt="Gemma 3 1B int4">
  <img src="https://img.shields.io/badge/SQLite-local-003b57?style=flat-square&logo=sqlite&logoColor=white" alt="SQLite">
  <img src="https://img.shields.io/badge/Material%20Design-1.12.0-757575?style=flat-square&logo=materialdesign&logoColor=white" alt="Material Design">
  <img src="https://img.shields.io/badge/OkHttp-4.12.0-2f9e44?style=flat-square&logo=square&logoColor=white" alt="OkHttp">
</p>

<p align="center">
  <a href="#getting-started">Getting started</a> ·
  <a href="#how-it-works">How it works</a> ·
  <a href="#features">Features</a> ·
  <a href="#roadmap">Roadmap</a>
</p>

> **Beta:** Download [Canopy 5.4.0 Local Gemma Beta](https://github.com/atharhive/canopy/releases/tag/v5.4.0-gemma-beta).
>
> **Demo video:** _Coming soon — this space is reserved for the walkthrough video._

## What is Canopy?

Canopy is an Android app that listens for bird sounds and uses the local BirdNET
recognition pipeline to identify likely species. It keeps observations on the
device, turns detections into collectible field cards, and optionally uses a
downloaded Gemma model to create short explanations, challenges, and answers
without sending the user's recordings or collection to a server.

<details>
<summary><strong>Why Canopy?</strong></summary>

- Birding should be approachable even when you do not know what to look for.
- Audio-first exploration lets people keep their eyes on the trail.
- Local processing keeps recordings and location history under the user's control.
- Progress, cards, and challenges make repeated listening feel rewarding.

</details>

## Features

<details>
<summary><strong>Recognition and narration</strong></summary>

- BirdNET-powered microphone detection.
- Location-aware recognition using the BirdNET metadata model.
- Adjustable confidence threshold and audio source.
- Optional Android offline text-to-speech summaries.
- Optional local Gemma summaries; BirdNET remains the source of truth.
- No ElevenLabs or cloud voice dependency in the local-first build.

</details>

<details>
<summary><strong>Collection and challenges</strong></summary>

- Gallery cards for every unique detected species.
- Best confidence, observation count, and collection date.
- Thirteen deterministic challenges that work without Gemma.
- Optional Gemma-generated challenges based only on collected species.
- Offline question box for asking Gemma about the collection and birding.

</details>

<details>
<summary><strong>Exploration tools</strong></summary>

- Guided simulated demo with a generated chirp.
- Manual latitude/longitude entry with range validation.
- Location-aware observations and history.
- Optional spectrogram and WAV capture.
- Bluetooth audio input support.
- Language and high-contrast settings.

</details>

## Demo video

The product walkthrough video is being prepared. This section will be updated
with the YouTube link once the recording is published.

## How it works

```mermaid
flowchart LR
    A[Microphone] --> B[Audio buffer]
    B --> C[BirdNET classifier]
    D[Location and date] --> C
    C --> E[Species and confidence]
    E --> F[Local observation database]
    F --> G[Collection gallery]
    E --> H{Gemma downloaded?}
    H -- Yes --> I[Gemma short summary or challenge]
    H -- No --> J[Deterministic local fallback]
    I --> K[Android offline TTS]
    J --> K
    F --> L[Personal challenges]
```

<details>
<summary><strong>Data and privacy flow</strong></summary>

1. Audio is captured on the device.
2. BirdNET performs the recognition locally.
3. Species, confidence, time, and location are stored in the local database.
4. Gemma receives structured detection or collection text only when enabled.
5. Android TTS speaks the result locally.
6. ElevenLabs is intentionally not used in this release.

</details>

## Getting started

### Download the beta

1. Open the [Canopy beta release](https://github.com/atharhive/canopy/releases/tag/v5.4.0-gemma-beta).
2. Download `Canopy-5.4.0-gemma-beta.apk`.
3. Optionally download the `.sha256` checksum file.
4. On Android, allow installation from the source you used to download the APK.
5. Open Canopy and download the BirdNET model when prompted.

The optional Gemma model is downloaded separately from inside the app and is
approximately 529 MB. Canopy remains usable without it.

### Build from source

<details>
<summary><strong>Requirements</strong></summary>

- Android Studio with Android SDK API 35.
- JDK 17.
- Android device or emulator running Android 8.0/API 26 or newer.
- Approximately 2 GB of free space for optional Gemma model testing.

</details>

```bash
git clone https://github.com/atharhive/canopy.git
cd canopy
./gradlew assembleDebug
```

The debug APK is generated at `app/build/outputs/apk/debug/app-debug.apk`.

<details>
<summary><strong>Run and test locally</strong></summary>

1. Open the project in Android Studio.
2. Let Gradle sync and select an Android device or emulator.
3. Run the `app` configuration.
4. Grant microphone and location permissions.
5. Use **Try a demo** to preview the flow without a real model.
6. Download the BirdNET model and test real detections outdoors.
7. Open the collection icon to review cards and challenges.
8. Download Gemma only if the device has enough storage and memory.

</details>

## Release verification

<details>
<summary><strong>5.4.0 Local Gemma Beta</strong></summary>

- Tag: [`v5.4.0-gemma-beta`](https://github.com/atharhive/canopy/releases/tag/v5.4.0-gemma-beta)
- APK: `Canopy-5.4.0-gemma-beta.apk`
- SHA-256: `edb7e6eb1d0f328030c9c092c0171c9db1b6b539c910a322a5b66c92f464f337`
- Gemma: Gemma 3 1B instruction-tuned int4, downloaded separately.

</details>

## Model and licensing

Canopy source code is distributed under the [GNU General Public License v3](LICENSE).
BirdNET model files are distributed separately from the app and have their own
license and usage restrictions. Review those terms before redistributing the
model or using a build commercially.

The optional Gemma model is downloaded from the model host and is subject to
Google/Gemma model terms. Users should review and accept those terms where
required before downloading or redistributing it.

## Roadmap

<details>
<summary><strong>Planned improvements</strong></summary>

- Walk/session summaries.
- Better collection filters and card artwork.
- Device-specific Gemma performance guidance.
- More accessibility refinements.
- A recorded product walkthrough.

</details>

## Contributing

Contributions are welcome and appreciated. Before opening an issue or pull
request, please search existing discussions and keep changes focused on one
problem or improvement.

<details>
<summary><strong>Contribution checklist</strong></summary>

- Fork the repository and create a focused feature branch.
- Use the existing Kotlin, Java, Android, and Gradle conventions.
- Keep user data local and never commit credentials, signing keys, or model secrets.
- Build and test with `./gradlew assembleDebug` before opening a pull request.
- Include a clear summary, testing details, screenshots for UI changes, and any
  device/API-level limitations.
- Update documentation when behavior, setup, or user-facing features change.
- Be respectful, constructive, and welcoming in issues and reviews.

</details>

<details>
<summary><strong>Hacktoberfest contributions</strong></summary>

Small, meaningful improvements are welcome during Hacktoberfest. Good first
contributions include documentation fixes, accessibility improvements, testing
coverage, UI polish, and carefully scoped bug fixes. Please avoid drive-by
formatting changes or duplicate submissions; maintainers review every
contribution for quality and relevance.

</details>

<p align="center">
  Built with love for curious listeners during Hacktoberfest ❤️ 🐦 ❤️
</p>
