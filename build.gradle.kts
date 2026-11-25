// 移除 pluginManagement 和 mixin 插件
rootProject.name = "black-and-white-world" // 【注意】改成你仓库的名字

plugins {
    kotlin("jvm") version "1.9.22" // 根据你的 Kotlin 版本调整
    // 不要写 org.spongepowered.mixin
}

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    compileOnly("com.github.Anuken.Mindustry:core:v160")
    compileOnly("com.github.Anuken.Arc:arc-core:v160")

    // 只保留普通依赖，不需要插件
    compileOnly("org.spongepowered:mixin:0.8.5")
    annotationProcessor("org.spongepowered:mixin:0.8.5:processor")
}

kotlin {
    jvmToolchain(17)
}

tasks.withType<Jar> {
    archiveFileName.set("BlackWhiteMod.jar")
    // 确保 mixins.blackwhite.json 被打进 jar 的根目录
    from("src/main/resources") {
        include("mixins.blackwhite.json")
    }
}
