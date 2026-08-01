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
val paperApiVersion = project.properties["paper.api.version"] as String

dependencies {
    compileOnly("io.papermc.paper:paper-api:$paperApiVersion")
    compileOnly("io.github.pylonmc:rebar:$rebarVersion")
    // Rebar 的 paperLibraryApi 依赖，插件代码直接使用 InvUI 的 Gui / VirtualInventory / Window
    compileOnly("xyz.xenondevs.invui:invui:2.1.0")
    compileOnly("xyz.xenondevs.invui:invui-kotlin:2.1.0")
    compileOnly(kotlin("stdlib"))
    implementation("com.h2database:h2:2.2.224")
}

kotlin {
    jvmToolchain(25)

    sourceSets {
        main {
            kotlin.exclude(
                // TODO(重构): 以下模块尚未迁移到 Rebar 0.42.1 的新 API。
                // 每完成一个模块的迁移，就从该列表中移除对应的排除项。
                "**/EndfieldIndustryPages.kt",
            )
        }
    }
}

idea {
    module {
        isDownloadJavadoc = true
        isDownloadSources = true
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_25
    targetCompatibility = JavaVersion.VERSION_25
}

kotlin {
    jvmToolchain(25)
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.compilerArgs.add("-parameters")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_25)
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
    apiVersion = "26.1.2"
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
    into("E:\\Minecraft\\开发\\Pylon开发\\plugins")
    outputs.upToDateWhen { false }
}

tasks.build {
    finalizedBy("copyToServer")
}
