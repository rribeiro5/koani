package io.github.rribeiro5.koani.di

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.js.Js

internal actual fun getEngine(): HttpClientEngine = Js.create()
