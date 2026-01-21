@file:Suppress("UnstableApiUsage", "SpellCheckingInspection")

import net.fabricmc.loom.api.LoomGradleExtensionAPI

plugins {
    java
    alias(libs.plugins.loom) apply false
    alias(libs.plugins.architectury)
    alias(libs.plugins.shadow) apply false
}

val mcVersion: String = libs.versions.minecraft.get()

architectury { minecraft = mcVersion }

allprojects {
    group = mod.group
    version = "${mod.version}-$mcVersion"
}

subprojects {
    apply(plugin = "architectury-plugin")
    apply(plugin = "dev.architectury.loom")

    val libs = rootProject.libs

    base.archivesName.set("${mod.id}-${project.name}")

    repositories {
        maven("https://maven.parchmentmc.org") { name = "ParchmentMC" }
        maven("https://maven.shedaniel.me/") // Cloth Config
        maven("https://maven.blamejared.com/") // JEI
    }

    dependencies {
        "minecraft"("net.minecraft:minecraft:$mcVersion")
        "mappings"(project.the<LoomGradleExtensionAPI>().let { it ->
            it.silentMojangMappingsLicense()
            it.layered {
                officialMojangMappings()
                libs.versions.parchment.get().let { it ->
                    if (it.isNotEmpty()) parchment("org.parchmentmc.data:parchment-$mcVersion:$it@zip")
                }
            }
        })

        implementation(libs.toml4j)
    }

    java {
        withSourcesJar()

        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
        options.release.set(21)
    }
    tasks.named("clean") {
        doLast { delete("logs") }
    }
}

configure(mod.enabled_platforms.map { project(":$it") }) {
    apply(plugin = "dev.architectury.loom")
    apply(plugin = "architectury-plugin")

    val platformName = project.the<LoomGradleExtensionAPI>().platform.map { it.displayName() }.get()

    configure<dev.architectury.plugin.ArchitectPluginExtension> {
        platformSetupLoomIde()
    }

    val common: Configuration by configurations.creating
    val shadowBundle: Configuration by configurations.creating
    configurations {
        compileOnly.configure { extendsFrom(common) }
        runtimeOnly.configure { extendsFrom(common) }

        shadowBundle.isCanBeResolved = true
        shadowBundle.isCanBeConsumed = false
    }

    dependencies {
        common(project(path = ":common", configuration = "namedElements")) { isTransitive = false }
        shadowBundle(project(path = ":common", configuration = "transformProduction$platformName"))
    }

    tasks.processResources {
        duplicatesStrategy = DuplicatesStrategy.INCLUDE

        inputs.property("version", project.version)

        val privateBuild = rootProject.file("assets/private-logo.png").exists()
        val modProperties = mapOf(
            "version" to project.version,
            "license" to if (privateBuild) "All Rights Reserved" else mod.license,
        )
        filesMatching("fabric.mod.json") { expand(modProperties) }
        filesMatching("META-INF/neoforge.mods.toml") { expand(modProperties) }

        if (privateBuild) {
            from(rootProject.file("assets/private-logo.png")) { rename { "${mod.id}_logo.png" } }
        } else {
            from(rootProject.file("assets/logo.png")) { rename { "${mod.id}_logo.png" } }
            from(rootProject.file("LICENSE")) { rename { "LICENSE.txt" } }
        }
        from(rootProject.file("third-party-licenses")) { into("third-party-licenses") }
        from(project.file("third-party-licenses")) { into("third-party-licenses") }
    }
}
