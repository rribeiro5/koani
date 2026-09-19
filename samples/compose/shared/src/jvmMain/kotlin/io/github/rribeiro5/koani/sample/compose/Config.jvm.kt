package io.github.rribeiro5.koani.sample.compose

actual val clientId: String = System.getProperty("MAL_CLIENT_ID") ?: System.getenv("MAL_CLIENT_ID") ?: ""
