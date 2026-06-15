package io.github.rribeiro5.koani.di

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin

internal actual fun getEngine(): HttpClientEngine = Darwin.create()
