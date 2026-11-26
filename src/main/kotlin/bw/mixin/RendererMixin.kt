package bw.mixin

import arc.Core
import arc.graphics.Texture
import arc.graphics.gl.Shader
import arc.util.Log
import mindustry.core.Renderer
import mindustry.graphics.Shaders
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

@Mixin(Renderer::class)
class RendererMixin {

    companion object {
        private var bwShader: Shader? = null
        private var initialized = false

        private fun ensureInit() {
            if (initialized) return
            initialized = true
            try {
                bwShader = Shader(
                    Core.files.internal("shaders/bw.vert").readString(),
                    Core.files.internal("shaders/bw.frag").readString()
                )
                Log.info("黑白着色器加载成功！")
            } catch (e: Exception) {
                Log.err("黑白着色器加载失败: @", e.message)
            }
        }
    }

    // 注入点改为在原版 render() 方法尾部
    @Inject(method = ["draw"], at = [At("TAIL")], remap = false)
    fun onDrawTail(ci: CallbackInfo) {
        ensureInit()
        if (bwShader == null) return

        // 使用 Arc 自带的方法直接画全屏矩形
        // 先重置投影矩阵到全屏坐标系，防止受摄像机位置影响
        arc.graphics.g2d.Draw.proj(arc.Core.camera)
        
        // 开启我们的 Shader
        arc.graphics.g2d.Draw.shader(bwShader!!)
        
        // 画一个全屏矩形
        arc.graphics.g2d.Draw.rect(0f, 0f, Core.graphics.width.toFloat(), Core.graphics.height.toFloat())
        
        // 恢复默认 Shader
        arc.graphics.g2d.Draw.shader(Shaders.def)
    }
}
