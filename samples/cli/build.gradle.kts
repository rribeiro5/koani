plugins {
    alias(libs.plugins.kotlin.multiplatform)
}

kotlin {
    jvm()

    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation(project(":core"))
                implementation(libs.kotlinx.coroutines.core)
                implementation(samples.clikt)
            }
        }
    }
}

tasks.register<JavaExec>("run") {
    group = "application"
    mainClass.set("io.github.rribeiro5.koani.sample.cli.MainKt")
    val compilation = kotlin.targets.getByName("jvm").compilations.getByName("main")
    classpath = files(compilation.output.allOutputs, compilation.runtimeDependencyFiles)
    standardInput = System.`in`
}
