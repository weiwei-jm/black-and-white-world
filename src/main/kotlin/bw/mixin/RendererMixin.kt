package bw.mixin

import arc.Core
import arc.graphics.Color
import arc.graphics.Texture
import arc.graphics.g2d.Draw
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

        private fun ensureInit() {
            if (initialized) return
            initialized = true
            try {
                bwShader = Shader(
                    Core.files.internal("shaders/bw.vert").readString(),
                    Core.files.internal("shaders/bw.frag").readString()
                )
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
        }

        // 把当前屏幕上的画面拷贝到我们的 Buffer 里
        bwBuffer!!.begin(Color.clear)
        Draw.flush()
        
        // 核心修改：使用 Arc 提供的稳定 GL 拷贝方法
        Core.graphics.gl20.glBindTexture(Texture.TextureFilter.linear.let { 3553 }, bwBuffer!!.getTexture().getTextureObjectHandle())
        
        bwBuffer!!.end()

        // 清空当前屏幕
        Core.graphics.clear(Color.clear)

        // 画全屏矩形，套用灰度 Shader
        Draw.proj(0f, 0f, w.toFloat(), h.toFloat())
        Draw.shader(bwShader!!)
        bwShader!!.setUniformi("u_texture", 0)
        
        val tex = bwBuffer!!.getTexture()
        // 核心修改：使用 TextureRegion 包装一下，再传入 Draw.rect
        val region = arc.graphics.g2d.TextureRegion(tex)
        Draw.rect(region, 0f, 0f, w.toFloat(), h.toFloat())
        
        // 恢复默认
        Draw.shader()
        Draw.proj(Core.camera)
    }
}
