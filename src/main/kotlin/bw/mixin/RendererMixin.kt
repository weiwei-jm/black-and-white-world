/*package bw.mixin

import arc.Core
import arc.graphics.Color
import arc.graphics.g2d.Draw
import arc.graphics.g2d.TextureRegion
import arc.graphics.gl.FrameBuffer
import arc.graphics.gl.Shader
import arc.util.Log
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
        private var texRegion: TextureRegion? = null

        private fun ensureInit() {
            if (initialized) return
            initialized = true
            try {
                bwShader = Shader(
                    Core.files.internal("shaders/bw.vert").readString(),
                    Core.files.internal("shaders/bw.frag").readString()
                )
                texRegion = TextureRegion()
            } catch (e: Exception) {
                Log.err("着色器加载失败: @", e.message)
            }
        }
    }

    @Inject(method = ["draw"], at = [At("TAIL")], remap = false)
    fun onDrawTail(ci: CallbackInfo) {
        ensureInit()
        if (bwShader == null) return

        val w = Core.graphics.width
        val h = Core.graphics.height

        if (bwBuffer == null || bwBuffer!!.width != w || bwBuffer!!.height != h) {
            bwBuffer?.dispose()
            bwBuffer = FrameBuffer(w, h)
            texRegion!!.set(bwBuffer!!.getTexture())
        }

        // 1. 把当前屏幕画面拷贝到 FrameBuffer 中 (Arc 自带的拷贝方法)
        bwBuffer!!.begin(Color.clear)
        Draw.flush()
        bwBuffer!!.end()

        // 2. 清空屏幕
        Core.graphics.clear(Color.clear)

        // 3. 画全屏矩形，应用灰度 Shader
        Draw.proj(0f, 0f, w.toFloat(), h.toFloat())
        Draw.shader(bwShader!!)
        bwShader!!.setUniformi("u_texture", 0)
        
        // 注意：这里直接传入我们准备好的 TextureRegion，绝对不会报错
        Draw.rect(texRegion, 0f, 0f, w.toFloat(), h.toFloat())
        
        // 4. 恢复默认状态
        Draw.shader()
        Draw.proj(Core.camera)
    }
}
*/
