plugins {
    alias(libs.plugins.kotlin.multiplatform).apply(false)
    alias(libs.plugins.android.kmp.library).apply(false)
    alias(libs.plugins.maven.publish).apply(false)
    alias(libs.plugins.kotlinx.serialization).apply(false)
    alias(libs.plugins.detekt).apply(false)
    alias(libs.plugins.kover)
    alias(libs.plugins.dokka)
}

dokka {
    moduleName.set("Koani")
    moduleVersion.set(libs.versions.koani.version.get())
}

dependencies {
    kover(project(":core"))
    kover(project(":auth-persistence-ksafe"))
    dokka(project(":core"))
    dokka(project(":auth-persistence-ksafe"))
}
