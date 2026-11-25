plugins {
    kotlin("jvm") version "1.9.22"
}

repositories {
    mavenCentral()
    maven("https://jitpack.io")
    maven("https://repo.spongepowered.org/repository/maven-public/")
}

dependencies {
    compileOnly("com.github.Anuken.Mindustry:core:v160")
    compileOnly("com.github.Anuken.Arc:arc-core:v160")
    compileOnly("org.spongepowered:mixin:0.8.5")
    annotationProcessor("org.spongepowered:mixin:0.8.5:processor")
}

kotlin {
    jvmToolchain(17)
}

tasks.withType<Jar> {
    // 打出来的包名，和工作流里保持一致
    archiveFileName.set("black-and-white-world.jar")
    
    // 1. 把根目录的 mod.hjson 强行打包到 Jar 的根目录
    from("mod.hjson") {
        into("")
    }
    
    // 2. 把根目录的 assets 文件夹强行打包进 Jar 的 assets 目录
    from("assets") {
        into("assets")
    }
}

tasks.register("deploy") {
    dependsOn("jar")
}
