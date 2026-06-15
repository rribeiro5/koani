package io.github.rribeiro5.koani.di

import io.ktor.client.engine.HttpClientEngine

internal expect fun getEngine(): HttpClientEngine
