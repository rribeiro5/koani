import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    id("com.android.application")
    alias(samples.plugins.jetbrains.compose)
    alias(samples.plugins.kotlin.compose.compiler)
    alias(libs.plugins.kotlinx.serialization)
}

kotlin {
    androidTarget {
        compilerOptions { jvmTarget = JvmTarget.JVM_17 }
    }

    jvm {
        compilerOptions { jvmTarget = JvmTarget.JVM_17 }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(project(":core"))
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.materialIconsExtended)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            
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
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
            implementation(libs.ktor.client.okhttp)
        }
    }
}

android {
    namespace = "io.github.rribeiro5.koani.sample.compose"
    compileSdk = 37

    defaultConfig {
        applicationId = "io.github.rribeiro5.koani.sample.compose"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        val malClientId = System.getenv("MAL_CLIENT_ID") ?: project.findProperty("MAL_CLIENT_ID")?.toString() ?: ""
        buildConfigField("String", "MAL_CLIENT_ID", "\"$malClientId\"")
    }

    buildFeatures {
        buildConfig = true
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
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
