package io.github.rribeiro5.koani.sample.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import io.github.rribeiro5.koani.sample.compose.di.initKoin
import org.koin.android.ext.koin.androidContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        initKoin {
            androidContext(this@MainActivity)
        }

        setContent {
            App()
        }
    }
}
