// 【注意】如果你的项目是在 GitHub 上用 Actions 编译的，这一段必须放在最前面
pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://repo.spongepowered.org/repository/maven-public/")
    }
    resolutionStrategy {
        eachPlugin {
            if (requested.id.id == "org.spongepowered.mixin") {
                useModule("org.spongepowered:mixingradle:0.7.+")
            }
        }
    }
}

rootProject.name = "black-and-white-world" // 【注意】改成你仓库的名字

plugins {
    kotlin("jvm") version "1.9.22" // 根据你的 Kotlin 版本修改，官方通常用 1.9.x
    id("org.spongepowered.mixin") version "0.7.+"
}

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    // 游戏核心依赖，版本已经确认是 v160
    compileOnly("com.github.Anuken.Mindustry:core:v160")
    compileOnly("com.github.Anuken.Arc:arc-core:v160")

    // Mixin 编译依赖
    annotationProcessor("org.spongepowered:mixin:0.8.5")
    compileOnly("org.spongepowered:mixin:0.8.5")
}

kotlin {
    jvmToolchain(17) // v160 及以上的源码需要 JDK 17
}

tasks.withType<Jar> {
    archiveFileName.set("BlackWhiteMod.jar") // 【注意】最终生成的 mod 文件名
    
    // 把 mixin 配置文件打包进去，避免运行时找不到
    from("src/main/resources") {
        include("mixins.blackwhite.json")
    }
}

// 以下为 GitHub Actions 需要的额外配置（防止混淆和报错）
tasks.processResources {
    inputs.property("version", project.version)
    filesMatching("mod.json") { // 如果你有 mod.json
        expand("version" to project.version)
    }
}
