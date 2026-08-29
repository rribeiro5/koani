# Koani CLI Sample

The Koani CLI is a search engine sample that demonstrates how to interact with the MyAnimeList API using the Koani library. It features a fully interactive terminal interface for searching and viewing details of anime and manga.

## Features

- **Interactive Search Engine**: Navigate through menus to search for anime or manga.
- **Optimized Data Fetching**: Fetches only essential fields for lists and full details only when requested.
- **Clikt & Mordant Integration**: Uses [Clikt](https://ajalt.github.io/clikt/) for command-line parsing and [Mordant](https://github.com/ajalt/mordant) for rich terminal interactions.
- **Flexible Authentication**: Pass your Client ID as a command-line argument or enter it via an interactive prompt.
- **Kotlin Multiplatform**: Built as a JVM target within the multiplatform project structure.

## How to Run

### Using Gradle

The easiest way to run the application is using the Gradle `run` task. You can pass your MyAnimeList Client ID as an argument:

```bash
./gradlew :samples:cli:run -PincludeSamples=true --args="--client-id YOUR_CLIENT_ID"
```

If you don't provide the `--client-id` argument, the application will interactively prompt you for it on startup:

```bash
./gradlew :samples:cli:run -PincludeSamples=true
```

### Building and Running the JAR

To build the project artifacts:

```bash
./gradlew :samples:cli:assemble -PincludeSamples=true
```

The resulting JAR will be located in `samples/cli/build/libs/`. 

> [!NOTE]
> When running the JAR directly via `java -jar`, ensure that all runtime dependencies (including the `core` library, Clikt, and Mordant) are included in the classpath. For development and testing, using the Gradle `run` task is recommended as it handles the classpath automatically.
