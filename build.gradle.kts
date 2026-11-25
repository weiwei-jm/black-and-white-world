plugins {
    // 你原有的插件保留
    id("org.spongepowered.mixin") version "0.7.+"  // 如果没有这行就加
}

dependencies {
    // 你原有的依赖保留
    compileOnly("com.github.Anuken.Mindustry:core:v160")
    compileOnly("com.github.Anuken.Arc:arc-core:v160")
    annotationProcessor("org.spongepowered:mixin:0.8.5")
}
