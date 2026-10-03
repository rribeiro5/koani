import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.kmp.library)
    alias(libs.plugins.maven.publish)
    alias(libs.plugins.detekt)
    alias(libs.plugins.kover)
    alias(libs.plugins.dokka)
}

dokka {
    moduleName.set("koani-auth-persistence-ksafe")
    moduleVersion.set(libs.versions.koani.version.get())
}

detekt {
    toolVersion = libs.versions.detekt.get()
    config.setFrom(files("$rootDir/config/detekt/detekt.yml"))
    baseline = file("detekt-baseline.xml")
    source.setFrom(files("src"))
}

dependencies {
    detektPlugins(libs.detekt.formatting)
}

kotlin {
    explicitApi = ExplicitApiMode.Strict

    android {
        namespace = "io.github.rribeiro5.koani.auth.persistence.ksafe"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
        androidResources.enable = true
        compilerOptions { jvmTarget = JvmTarget.JVM_17 }
    }

    jvm {
        compilerOptions { jvmTarget = JvmTarget.JVM_17 }
    }
    js { browser() }
    wasmJs { browser() }
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        commonMain.dependencies {
            api(project(":koani-core"))
            api(libs.ksafe)
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation(libs.kotlinx.coroutines.test)
        }
    }
}

//Publishing your Kotlin Multiplatform library to Maven Central
//https://www.jetbrains.com/help/kotlin-multiplatform-dev/multiplatform-publish-libraries.html
mavenPublishing {
    publishToMavenCentral()
    coordinates("io.github.rribeiro5", "koani-auth-persistence-ksafe", libs.versions.koani.version.get())

    pom {
        name = "koani-auth-persistence-ksafe"
        description = "Kotlin Multiplatform library for the MyAnimeList API - Support for secure token persistence with KSafe"
        url = "https://rribeiro5.github.io/koani/"

        licenses {
            license {
                name = "MIT"
                url = "https://opensource.org/licenses/MIT"
            }
        }

        developers {
            developer {
                id = "rribeiro5"
                name = "Rafael Ribeiro"
                email = "rribeiro5.dev@proton.me"
            }
        }

        scm {
            url = "https://github.com/rribeiro5/koani"
        }
    }
    if (project.hasProperty("signing.keyId")) signAllPublications()
}
