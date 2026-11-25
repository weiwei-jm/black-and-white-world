package 你的包名.mixin

import arc.Core
import arc.graphics.Color
import arc.graphics.gl.FrameBuffer
import arc.graphics.gl.Shader
import mindustry.core.Renderer
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

@Mixin(Renderer::class)
class RendererMixin {

    companion object {
        private var bwShader: Shader? = null
        private var bwBuffer: FrameBuffer? = null
        private var initialized = false

        private fun ensureInit() {
            if (initialized) return
            initialized = true
            try {
                bwShader = Shader(
                    Core.files.internal("shaders/bw.vert").readString(),
                    Core.files.internal("shaders/bw.frag").readString()
                )
            } catch (e: Exception) {
                Core.app.post { arc.util.Log.err("黑白着色器加载失败: @", e.message) }
            }
        }
    }

    @Inject(method = ["draw"], at = [At("HEAD")], remap = false)
    fun onDrawHead(ci: CallbackInfo) {
        ensureInit()
        // 每帧绘制前确保缓冲尺寸正确
        if (bwShader == null) return
        val w = Core.graphics.width
        val h = Core.graphics.height
        if (bwBuffer == null || bwBuffer!!.width != w || bwBuffer!!.height != h) {
            bwBuffer?.dispose()
            bwBuffer = FrameBuffer(w, h)
        }
    }
}
