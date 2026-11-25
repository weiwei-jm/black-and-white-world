plugins {
    kotlin("jvm") version "1.9.22" // 如果你原本是别的版本，保留你原本的即可
}

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    // 游戏依赖
    compileOnly("com.github.Anuken.Mindustry:core:v160")
    compileOnly("com.github.Anuken.Arc:arc-core:v160")

    // Mixin 依赖（不需要插件）
    compileOnly("org.spongepowered:mixin:0.8.5")
    annotationProcessor("org.spongepowered:mixin:0.8.5:processor")
}

kotlin {
    jvmToolchain(17)
}

tasks.withType<Jar> {
    archiveFileName.set("BlackWhiteMod.jar")
    // 确保 mixins.blackwhite.json 被打包进 jar
    from("src/main/resources") {
        include("mixins.blackwhite.json")
    }
}
