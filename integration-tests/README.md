# Integration Tests

This module contains integration tests that run against the real **MyAnimeList API**. These tests are isolated from the main unit test suite to avoid rate-limiting issues and to ensure they only run when explicitly requested or in appropriate CI environments.

## 🛠 Prerequisites

To run these tests locally, you must provide a valid MyAnimeList Client ID. You can do this in three ways (ordered by priority):

1.  **Gradle Property**: Pass `-PTEST_MAL_CLIENT_ID=your_id` to the command line.
2.  **Environment Variable**: Set `TEST_MAL_CLIENT_ID=your_id` in your system environment.
3.  **Local Properties**: Add `TEST_MAL_CLIENT_ID=your_id` to your `local.properties` file in the project root.

## 🚀 Running Tests

Integration tests are **not** executed by `./gradlew check`. To run them explicitly, use:

```bash
./gradlew :integration-tests:integrationTest
```

## 📝 How to add a new test

All integration tests must follow the patterns established in `BaseIntegrationTest` to ensure rate limits are respected.

### Step-by-Step Guide

1.  **Create a new test class**: Create your class in `src/jvmTest/kotlin` and inherit from `BaseIntegrationTest`.
2.  **Use the DSL**: Wrap your test logic in `runIntegrationTest { client -> ... }`. This provides you with a pre-configured `KoaniClient`.
3.  **Wrap API calls**: Every call to the `KoaniClient` should be wrapped in `performRequest { ... }`. This uses a global `Mutex` to ensure a 1-second gap between all API calls across the entire test suite.

### Example

```kotlin
class MyNewIntegrationTest : BaseIntegrationTest() {

    @Test
    fun `my new test case`() = runIntegrationTest { client ->
        // First request
        val ranking = performRequest {
            client.anime.getAnimeRanking(AnimeRankingType.ALL)
        }
        
        // ... assertions ...

        // Second request (will automatically wait for the 1s cooldown)
        val details = performRequest {
            client.anime.getAnimeDetails(ranking.data.first().node.id)
        }
        
        // ... assertions ...
    }
}
```

## ⚙️ Configuration Details

*   **Serial Execution**: The module is configured with `maxParallelForks = 1` in `build.gradle.kts` to prevent multiple JVM processes from hitting the API simultaneously.
*   **Rate Limiting**: A shared `Mutex` in `BaseIntegrationTest` enforces a 1-second delay *after* each request.
