package io.github.rribeiro5.koani.http

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp

internal actual fun getEngine(): HttpClientEngine = OkHttp.create()
