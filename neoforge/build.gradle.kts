@file:Suppress("UnstableApiUsage", "SpellCheckingInspection")

plugins {
    alias(libs.plugins.shadow)
}

architectury { neoForge() }

val shadowBundle: Configuration by configurations.getting
val developmentNeoForge: Configuration by configurations.getting
configurations {
    developmentNeoForge.extendsFrom(common.get())
}

repositories {
    maven("https://maven.neoforged.net/releases") { name = "NeoForged" }
}

dependencies {
    neoForge(libs.neoforge.neoforge)

    modLocalRuntime(libs.neoforge.jei)

    modApi(libs.neoforge.clothconfig)

    forgeRuntimeLibrary(libs.toml4j)
    shadowBundle(libs.toml4j) { exclude(group = "com.google.code.gson", module = "gson") }
}

tasks {
    shadowJar {
        configurations = listOf(shadowBundle)
        archiveClassifier.set("dev-shadow")

        exclude("META-INF/maven/**/*", "META-INF/versions/**/*")
        relocate("com.moandjiezana.toml", "${mod.group}.libs.toml")
    }

    remapJar {
        inputFile.set(shadowJar.flatMap { it.archiveFile })
        dependsOn(shadowJar)
    }
}
