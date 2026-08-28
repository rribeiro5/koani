rootProject.name = "koani"

pluginManagement {
    repositories {
        google {
            content { 
              	includeGroupByRegex("com\\.android.*")
              	includeGroupByRegex("com\\.google.*")
              	includeGroupByRegex("androidx.*")
              	includeGroupByRegex("android.*")
            }
        }
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    versionCatalogs {
        create("samples") {
            from(files("gradle/samples.versions.toml"))
        }
    }
    repositories {
        google {
            content { 
              	includeGroupByRegex("com\\.android.*")
              	includeGroupByRegex("com\\.google.*")
              	includeGroupByRegex("androidx.*")
              	includeGroupByRegex("android.*")
            }
        }
        mavenCentral()
    }
}

include(":core")
include(":auth-persistence-ksafe")
include(":integration-tests")

val includeSamples = providers.gradleProperty("includeSamples").orNull == "true" 
    || System.getProperty("idea.active") == "true"

if (includeSamples) {
    include(":samples:cli")
}

