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
    archiveFileName.set("black-and-white-world.jar")
}

tasks.register("deploy") {
    dependsOn("jar")
    doLast {
        println("Deploy task finished, jar is in build/libs/")
    }
}
