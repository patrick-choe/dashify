import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.32"
    kotlin("plugin.serialization") version "1.4.21"
    id("com.github.johnrengelman.shadow") version "5.2.0"
}

val relocate = (findProperty("relocate") as? String)?.toBoolean() ?: true
val ktorVersion = "1.5.4"

group = "me.aroxu"
version = ".${project.property("version").toString()}"

repositories {
    maven("https://repo.maven.apache.org/maven2/")
    maven("https://papermc.io/repo/repository/maven-public/")
    maven("https://jitpack.io")
}

dependencies {
    compileOnly(kotlin("stdlib-jdk8")) //8이상
    compileOnly("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.1")
    compileOnly("com.destroystokyo.paper:paper-api:1.16.5-R0.1-SNAPSHOT")
    compileOnly("org.jetbrains.kotlinx:kotlinx-serialization-json:1.0.1")

    implementation("at.favre.lib:bcrypt:0.9.0")
    implementation("com.github.monun:kommand:0.9.0")
    implementation("io.ktor:ktor-server-core:$ktorVersion")
    implementation("io.ktor:ktor-server-netty:$ktorVersion")
    implementation("io.ktor:ktor-jackson:1.5.4")
}

configurations.runtimeClasspath.get().exclude("org.jetbrains.kotlin", "kotlin-stdlib")
configurations.runtimeClasspath.get().exclude("org.jetbrains.kotlin", "kotlin-reflect")
configurations.runtimeClasspath.get().exclude("org.jetbrains.kotlinx", "kotlinx-serialization-json+")
configurations.runtimeClasspath.get().exclude("org.jetbrains.kotlinx", "kotlinx-coroutines-core")
configurations.runtimeClasspath.get().exclude("org.jetbrains.exposed", "exposed-core")
configurations.runtimeClasspath.get().exclude("org.jetbrains.exposed", "exposed-dao")
configurations.runtimeClasspath.get().exclude("org.jetbrains.exposed", "exposed-jdbc")

tasks {
    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "11"
    }
    create<Jar>("sourcesJar") {
        from(sourceSets["main"].allSource)
        archiveClassifier.set("sources")
    }
    processResources {
        filesMatching("**/*.yml") {
            expand(project.properties)
        }
    }
    shadowJar {
        archiveBaseName.set(project.property("pluginName").toString())
        archiveVersion.set("") // For bukkit plugin update
        archiveClassifier.set("") // Remove 'all'

        if (relocate) {
            relocate("com.github.monun.kommand", "${rootProject.group}.${rootProject.name}.kommand")
        }

        doFirst {
            println("relocate = $relocate")
        }
    }
    build {
        dependsOn(shadowJar)
    }
    create<Copy>("paper") {
        from(shadowJar)
        var dest = file("${project.projectDir}/server/plugins")
        // if plugin.jar exists in plugins change dest to plugins/update
        if (File(dest, shadowJar.get().archiveFileName.get()).exists()) dest = File(dest, "update")
        into(dest)
    }
}