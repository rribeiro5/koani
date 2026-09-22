# Integration Tests

This module contains integration tests that run against the real **MyAnimeList API**. These tests are isolated from the main unit test suite to avoid rate-limiting issues and to ensure they only run when explicitly requested or in appropriate CI environments.

## 🛠 Prerequisites

To run these tests locally, provide a valid MyAnimeList Client ID. You can do this in three ways (ordered by priority):

1.  **Gradle Property**: Pass `-PTEST_MAL_CLIENT_ID=your_id` to the command line.
2.  **Environment Variable**: Set `TEST_MAL_CLIENT_ID=your_id` in your system environment.
3.  **Local Properties**: Add `TEST_MAL_CLIENT_ID=your_id` to your `local.properties` file in the project root.

## 🔐 Rotating OAuth test tokens

Authenticated tests use pre-issued OAuth tokens. The repository includes a local utility that completes the MyAnimeList PKCE flow through a temporary localhost callback and prints copy-ready values. It never writes tokens to disk.

1. Register `http://127.0.0.1:8765/callback` as an allowed redirect URI in the MyAnimeList API application.
2. Put the client ID in `local.properties` as `TEST_MAL_CLIENT_ID` (and `TEST_MAL_CLIENT_SECRET` if the application requires one).
3. Run:

   ```bash
   ./gradlew :integration-tests:rotateTestTokens
   ```

   On Windows, use `gradlew.bat :integration-tests:rotateTestTokens`.
4. Complete the browser authorization. Copy the printed `TEST_MAL_ACCESS_TOKEN`, `TEST_MAL_REFRESH_TOKEN`, and `TEST_MAL_REFRESH_TOKEN_EXPIRES_AT` values into your local secret store or CI secret manager. The utility generates the refresh-token expiry as one calendar month after the rotation time. If MyAnimeList returns `expires_in`, it is used only for the access-token expiry; if the field is absent or invalid, no access-token expiry value is printed.

**Important:** The redirect URI configured in the MyAnimeList API dashboard must match the URI used by the utility exactly. This includes the scheme (`http`), host (`127.0.0.1`), port (`8765`), and path (`/callback`). A different localhost hostname, port, or path will cause the OAuth authorization to fail. If you use `--port` or `--redirect-uri`, register that exact resulting URI in the dashboard before starting the flow.

The utility also supports `--port`, `--redirect-uri`, `--client-id`, and `--client-secret` overrides. If the browser does not open automatically, copy the authorization URL printed by the command into a browser.

Never commit the printed values, add them to `local.properties`, or include them in logs. Rotate the access/refresh token pair before the recorded refresh-token expiry.

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

Authenticated tests should opt in explicitly with `runIntegrationTest(authenticated = true) { client -> ... }`. The default remains unauthenticated and requires only the client ID. Authenticated runs require `TEST_MAL_ACCESS_TOKEN` and `TEST_MAL_REFRESH_TOKEN`; these are loaded with the same Gradle-property, environment-variable, or root `local.properties` precedence.

## ⚙️ Configuration Details

*   **Serial Execution**: The module is configured with `maxParallelForks = 1` in `build.gradle.kts` to prevent multiple JVM processes from hitting the API simultaneously.
*   **Rate Limiting**: A shared `Mutex` in `BaseIntegrationTest` enforces a 1-second delay *after* each request.
