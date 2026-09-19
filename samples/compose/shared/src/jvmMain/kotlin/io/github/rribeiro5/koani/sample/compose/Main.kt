package io.github.rribeiro5.koani.sample.compose

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import io.github.rribeiro5.koani.sample.compose.di.initKoin

fun main() = application {
    initKoin()

    Window(
        onCloseRequest = ::exitApplication,
        title = "Koani Sample",
    ) {
        App()
    }
}
