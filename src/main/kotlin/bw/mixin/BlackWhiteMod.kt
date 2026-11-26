package bw

import arc.Core
import arc.Events
import arc.graphics.gl.Shader
import arc.util.Log
import mindustry.game.EventType.ClientLoadEvent
import mindustry.mod.Mod

class BlackWhiteMod : Mod() {

    override fun init() {
        if (arc.Vars.headless) return

        // 监听游戏资源加载完成事件
        Events.on(ClientLoadEvent::class.java) {
            Log.info("开始加载黑白着色器...")
            
            // 替换游戏原版的核心着色器（比如地形、水面、护盾、发光等）
            replaceShader("sprite") // 基础贴图
            replaceShader("water")
            replaceShader("shield")
            replaceShader("buildBeam")
            replaceShader("blockbuild")
            // 你可以根据需要继续添加其他 Shader 的名字
        }
    }

    private fun replaceShader(name: String) {
        try {
            // 读取我们自己的黑白着色器
            val bwShader = Shader(
                Core.files.internal("shaders/bw.vert").readString(),
                Core.files.internal("shaders/bw.frag").readString()
            )
            
            // 获取游戏自带的 Shader 管理器
            val shadersField = mindustry.graphics.Shaders::class.java.getDeclaredField(name)
            shadersField.isAccessible = true
            shadersField.set(null, bwShader)
            
            Log.info("成功替换 Shader: @", name)
        } catch (e: Exception) {
            Log.err("替换 Shader 失败: @ - @", name, e.message)
        }
    }
}
