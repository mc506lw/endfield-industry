import net.minecrell.pluginyml.bukkit.BukkitPluginDescription

plugins {
    kotlin("jvm") version "2.3.0"
    idea
    id("com.gradleup.shadow") version "9.2.2"
    id("net.minecrell.plugin-yml.bukkit") version "0.6.0"
    id("xyz.jpenilla.run-paper") version "2.3.0"
}

group = project.properties["group"]!!

repositories {
    mavenCentral()
    maven("https://central.sonatype.com/repository/maven-snapshots/")
    maven("https://repo.papermc.io/repository/maven-public/") {
        name = "papermc"
    }
    maven("https://jitpack.io") {
        name = "JitPack"
    }
    maven("https://repo.xenondevs.xyz/releases") {
        name = "InvUI"
    }
}

val rebarVersion = project.properties["rebar.version"] as String
val minecraftVersion = project.properties["minecraft.version"] as String

dependencies {
    compileOnly("io.papermc.paper:paper-api:$minecraftVersion-R0.1-SNAPSHOT")
    compileOnly("io.github.pylonmc:rebar:$rebarVersion")
    compileOnly(kotlin("stdlib"))
    implementation("com.h2database:h2:2.2.224")
}

idea {
    module {
        isDownloadJavadoc = true
        isDownloadSources = true
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

kotlin {
    jvmToolchain(21)
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.compilerArgs.add("-parameters")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21)
    }
}

tasks.processResources {
    filteringCharset = "UTF-8"
}

tasks.shadowJar {
    archiveClassifier = ""
}

bukkit {
    name = project.name
    main = project.properties["main-class"] as String
    version = project.version.toString()
    apiVersion = "1.21"
    depend = listOf("Rebar")
    load = BukkitPluginDescription.PluginLoadOrder.STARTUP
}

tasks.runServer {
    downloadPlugins {
        github("pylonmc", "rebar", rebarVersion, "rebar-$rebarVersion.jar")
    }
    maxHeapSize = "4G"
    minecraftVersion(minecraftVersion)
}

tasks.register<Copy>("copyToServer") {
    from(tasks.shadowJar)
    into("D:\\我的世界资源库\\服务器\\岚域3.0\\plugins")
    outputs.upToDateWhen { false }
}

tasks.build {
    finalizedBy("copyToServer")
}
