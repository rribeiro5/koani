# Koani

Kotlin Multiplatform library for the MyAnimeList API.

[![CI](https://github.com/rribeiro5/koani/actions/workflows/ci.yml/badge.svg?event=push)](https://github.com/rribeiro5/koani/actions/workflows/ci.yml)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.rribeiro5/koani-core)](https://central.sonatype.com/artifact/io.github.rribeiro5/koani-core)
[![Documentation](https://img.shields.io/badge/docs-website-brightgreen.svg)](https://rribeiro5.github.io/koani/)
[![Platform](https://img.shields.io/badge/platform-Android%20%7C%20iOS%20%7C%20JVM%20%7C%20JS%20%7C%20WasmJS-blue.svg)](https://rribeiro5.github.io/koani/docs/getting-started/multiplatform)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

```kotlin
// 1. Initialize KoaniClient
val client = KoaniClient.Builder(clientId = "YOUR_CLIENT_ID").build()

// 2. Fetch anime details requesting only TITLE and MEAN fields
val anime = client.anime.getAnimeDetails(
    animeId = 5114,
    fields = listOf(
        AnimeField.TITLE,
        AnimeField.MEAN
    )
)

println("${anime.title} — Score: ${anime.mean}")
```

## Quickstart Guide

### 1. Add Dependencies

Add Koani to your `gradle/libs.versions.toml`:

```toml
[versions]
koani = "<latest-version>"

[libraries]
# Core client library (Required)
koani-core = { module = "io.github.rribeiro5:koani-core", version.ref = "koani" }

# Optional: Secure token persistence support using KSafe
koani-auth-persistence-ksafe = { module = "io.github.rribeiro5:koani-auth-persistence-ksafe", version.ref = "koani" }
```

Then in your module's `build.gradle.kts`:

```kotlin
kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.koani.core)

            // Optional: Secure token persistence support
            implementation(libs.koani.auth.persistence.ksafe)
        }
    }
}
```

### 2. Basic Request Example

```kotlin
import io.github.rribeiro5.koani.KoaniClient
import io.github.rribeiro5.koani.error.KoaniException

suspend fun main() {
    val client = KoaniClient.Builder(clientId = "YOUR_MYANIMELIST_CLIENT_ID").build()

    try {
        val searchResult = client.anime.getAnimeList(query = "Fullmetal Alchemist", limit = 5)
        searchResult.data.forEach { anime ->
            println("${anime.id}: ${anime.title}")
        }
    } catch (e: KoaniException) {
        println("API Error: ${e.message}")
    }
}
```

## Documentation Website

Visit our official documentation website for complete guides and references:

🔗 **[https://rribeiro5.github.io/koani/](https://rribeiro5.github.io/koani/)**

### Quick Links

* 🚀 **[Quickstart Guide](https://rribeiro5.github.io/koani/docs/getting-started/quickstart)** — Step-by-step installation & setup
* 📱 **[Multiplatform Setup](https://rribeiro5.github.io/koani/docs/getting-started/multiplatform)** — Supported targets & platform requirements
* ⚙️ **[Client Configuration](https://rribeiro5.github.io/koani/docs/configuration/client-initialization)** — Builder options & DI setup (Koin, Hilt)
* 🔐 **[OAuth2 Authentication](https://rribeiro5.github.io/koani/docs/auth/oauth-pkce)** — User login, PKCE flow & token refresh
* 🍿 **[Anime API Guide](https://rribeiro5.github.io/koani/docs/guides/anime)** — Search, rankings, seasonal schedules, and user list updates
* 📚 **[Manga API Guide](https://rribeiro5.github.io/koani/docs/guides/manga)** — Manga search, details, rankings, and reading lists
* 👤 **[User Profile Guide](https://rribeiro5.github.io/koani/docs/guides/user)** — User account details & anime statistics
* 💬 **[Forum API Guide](https://rribeiro5.github.io/koani/docs/guides/forum)** — Categories, boards, topics, and thread posts
* 💾 **[Token Persistence](https://rribeiro5.github.io/koani/docs/auth/token-persistence)** — Persistent secure token storage with KSafe
* 📖 **[Dokka API Reference](https://rribeiro5.github.io/koani/api/index.html)** — Generated API reference documentation

## License

This project is licensed under the [MIT License](LICENSE).
