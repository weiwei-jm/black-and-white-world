plugins {
    kotlin("jvm") version "1.9.22" // 如果你原本是别的版本，保留你原本的即可
}

repositories {
    mavenCentral()
    maven("https://jitpack.io")
    // 关键：Sponge 的 Maven 仓库，用于下载 Mixin
    maven("https://repo.spongepowered.org/repository/maven-public/")
}

dependencies {
    // 游戏依赖
    compileOnly("com.github.Anuken.Mindustry:core:v160")
    compileOnly("com.github.Anuken.Arc:arc-core:v160")

    // Mixin 依赖
    compileOnly("org.spongepowered:mixin:0.8.5")
    annotationProcessor("org.spongepowered:mixin:0.8.5:processor")
}

kotlin {
    jvmToolchain(17)
}

// 确保 Mixin 配置文件被打包进 Jar
tasks.withType<Jar> {
    archiveFileName.set("BlackWhiteMod.jar")
    from("src/main/resources") {
        include("mixins.blackwhite.json")
    }
}

// 补上 Actions 需要的 deploy 任务（刚才报错找不到这个任务）
tasks.register("deploy") {
    dependsOn("jar")
    doLast {
        println("Deploy task finished, jar is in build/libs/")
    }
}
