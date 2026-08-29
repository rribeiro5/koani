plugins {
    base
}

tasks.named("assemble") {
    subprojects.forEach { subproject ->
        dependsOn(subproject.tasks.matching { it.name == "assemble" })
    }
}
