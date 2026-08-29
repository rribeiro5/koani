# Koani CLI Sample

A simple command-line interface application demonstrating the basic initialization and usage of the `KoaniClient`.

## Features

- Built using [Clikt](https://ajalt.github.io/clikt/) for argument parsing and interactive prompts.
- Demonstrates `KoaniClient.Builder` configuration.
- Kotlin Multiplatform (JVM target).

## How to Run

### Using Gradle

Use the following Gradle command to run the application directly:

```bash
./gradlew :samples:cli:run -PincludeSamples=true
```

### Building and Running the JAR

You can also build a distribution and run the script or execute the JAR. To build the distribution:

```bash
./gradlew :samples:cli:assemble -PincludeSamples=true
```

The resulting JAR can be found in `build/libs/`. Note that when running the JAR directly via `java -jar`, you must ensure all dependencies (including the `core` library and `Clikt`) are on the classpath.
