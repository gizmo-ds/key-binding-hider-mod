@file:Suppress("UnstableApiUsage", "SpellCheckingInspection")

plugins {
    alias(libs.plugins.shadow)
}

architectury { fabric() }

val shadowBundle: Configuration by configurations.getting
val developmentFabric: Configuration by configurations.getting
configurations {
    developmentFabric.extendsFrom(common.get())
}

repositories {
    maven("https://maven.terraformersmc.com/") { name = "Terraformers" }
}

dependencies {
    modImplementation(libs.fabric.loader)

    modLocalRuntime(libs.fabric.api)
//    modLocalRuntime(libs.fabric.jei)

    modImplementation(libs.fabric.modmenu)
    modApi(libs.fabric.clothconfig) { exclude(group = "net.fabricmc.fabric-api") }

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
