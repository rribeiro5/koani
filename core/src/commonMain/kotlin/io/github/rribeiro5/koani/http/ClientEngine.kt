package io.github.rribeiro5.koani.http

import io.ktor.client.engine.HttpClientEngine

internal expect fun getEngine(): HttpClientEngine
