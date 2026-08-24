import java.util.Properties

plugins {
    alias(libs.plugins.kotlin.multiplatform)
}

kotlin {
    jvm()

    sourceSets {
        commonMain.dependencies {
            implementation(project(":core"))
        }
        
        jvmTest.dependencies {
            implementation(kotlin("test"))
            implementation(libs.kotlinx.coroutines.test)
        }
    }
}

val malClientId: String? = project.findProperty("TEST_MAL_CLIENT_ID")?.toString()
    ?: System.getenv("TEST_MAL_CLIENT_ID")
    ?: loadFromLocalProperties("TEST_MAL_CLIENT_ID")

fun loadFromLocalProperties(propertyName: String): String? {
    val localPropertiesFile = rootProject.file("local.properties")
    if (localPropertiesFile.exists()) {
        val properties = Properties()
        localPropertiesFile.inputStream().use { properties.load(it) }
        return properties.getProperty(propertyName)
    }
    return null
}

// Disable standard JVM test tasks to keep them separate from 'check'
tasks.named("jvmTest") {
    enabled = false
}

tasks.register<Test>("integrationTest") {
    group = "verification"
    description = "Runs integration tests against the real MyAnimeList API."

    val jvmTarget = kotlin.targets.getByName("jvm")
    val testCompilation = jvmTarget.compilations.getByName("test")
    
    testClassesDirs = testCompilation.output.classesDirs
    classpath = testCompilation.runtimeDependencyFiles!!

    // Serial execution
    maxParallelForks = 1

    // Pass Client ID as system property
    malClientId?.let {
        systemProperty("TEST_MAL_CLIENT_ID", it)
    }

    // Force tests to run even if outputs are up to date
    outputs.upToDateWhen { false }
}
