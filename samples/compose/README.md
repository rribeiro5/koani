# Koani Compose Sample

A Compose Multiplatform application demonstrating how to use the **Koani** library to build a cross-platform UI for browsing anime and manga rankings.

> [!IMPORTANT]
> **Purpose of this Sample**: This application is primarily designed to demonstrate the integration and usage of the `KoaniClient`. To keep the example focused and concise, some architectural shortcuts may have been taken (e.g., simplified navigation, minimal state persistence). For production applications, we recommend following robust architecture patterns tailored to your specific needs.

## Features

- **Multiplatform Support**: Shared UI and logic for **Android** and **Desktop (JVM)**.
- **Ranked Browsing**: Browse the top-ranked anime and manga using a dual-tab interface.
- **Rich Detail Views**: View comprehensive information for any entry, including synopsis, scores, and status.

## How to Run

### Configuration (Client ID)

The application requires a MyAnimeList Client ID. You can provide it via a Gradle property or environment variable named `MAL_CLIENT_ID`.

### Desktop (JVM)

Run the following command from the project root:

```bash
./gradlew :samples:compose:run -PincludeSamples=true -PMAL_CLIENT_ID=YOUR_ID
```

### Android

You can run the app directly from Android Studio (ensure the `:samples:compose` module is selected) or use the command line:

```bash
./gradlew :samples:compose:installDebug -PincludeSamples=true -PMAL_CLIENT_ID=YOUR_ID
```
