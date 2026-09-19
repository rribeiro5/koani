import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.kmp.library)
    alias(samples.plugins.jetbrains.compose)
    alias(samples.plugins.kotlin.compose.compiler)
    alias(libs.plugins.kotlinx.serialization)
}

kotlin {
    android {
        namespace = "io.github.rribeiro5.koani.sample.compose.shared"
        compileSdk = 37
        minSdk = 26
        compilerOptions { jvmTarget = JvmTarget.JVM_17 }
    }

    jvm {
        compilerOptions { jvmTarget = JvmTarget.JVM_17 }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(project(":core"))
            implementation(samples.compose.runtime)
            implementation(samples.compose.foundation)
            implementation(samples.compose.material3)
            implementation(samples.compose.material.icons.extended)
            implementation(samples.compose.resources)
            implementation(samples.compose.ui.tooling.preview)
            
            implementation(samples.koin.core)
            implementation(samples.koin.compose)
            implementation(samples.koin.compose.viewmodel)
            
            implementation(samples.coil.compose)
            implementation(samples.coil.network.ktor)
            
            implementation(samples.navigation3.ui)
            implementation(samples.navigation3.viewmodel)
            
            implementation(samples.lifecycle.viewmodel)
            implementation(samples.lifecycle.runtime.compose)

            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.kotlinx.datetime)
        }

        androidMain.dependencies {
            implementation(samples.koin.android)
            implementation(libs.kotlinx.coroutines.android)
            implementation(libs.ktor.client.okhttp)
        }

        jvmMain.dependencies {
            implementation(samples.compose.desktop)
            implementation(libs.kotlinx.coroutines.swing)
            implementation(libs.ktor.client.okhttp)
        }
    }
}

compose.desktop {
    application {
        mainClass = "io.github.rribeiro5.koani.sample.compose.MainKt"
        jvmArgs += "-DMAL_CLIENT_ID=${System.getenv("MAL_CLIENT_ID") ?: project.findProperty("MAL_CLIENT_ID")?.toString() ?: ""}"
        nativeDistributions {
            targetFormats(org.jetbrains.compose.desktop.application.dsl.TargetFormat.Dmg, org.jetbrains.compose.desktop.application.dsl.TargetFormat.Msi, org.jetbrains.compose.desktop.application.dsl.TargetFormat.Deb)
            packageName = "KoaniSample"
            packageVersion = "1.0.0"
        }
    }
}
