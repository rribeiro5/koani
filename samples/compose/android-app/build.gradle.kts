plugins {
    id("com.android.application")
    alias(samples.plugins.kotlin.compose.compiler)
}

android {
    namespace = "io.github.rribeiro5.koani.sample.compose.android"
    compileSdk = 37

    defaultConfig {
        applicationId = "io.github.rribeiro5.koani.sample.compose.android"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        val malClientId = System.getenv("MAL_CLIENT_ID")
            ?: project.findProperty("MAL_CLIENT_ID")?.toString()
            ?: ""
        buildConfigField(
            "String",
            "MAL_CLIENT_ID",
            "\"$malClientId\""
        )
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
    kotlin {
        jvmToolchain(17)
    }
}

dependencies {
    implementation(project(":samples:compose:shared"))
    
    implementation(samples.activity.compose)
    implementation(samples.koin.android)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.ktor.client.okhttp)
}
