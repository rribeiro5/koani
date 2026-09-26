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
    kover(project(":koani-core"))
    kover(project(":koani-auth-persistence-ksafe"))
    dokka(project(":koani-core"))
    dokka(project(":koani-auth-persistence-ksafe"))
}
