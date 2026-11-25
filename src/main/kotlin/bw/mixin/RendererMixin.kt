package bw.mixin

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

        fun applyBlackWhite() {
            if (bwShader == null) return
            val w = Core.graphics.width
            val h = Core.graphics.height
            if (bwBuffer == null || bwBuffer!!.width != w || bwBuffer!!.height != h) {
                bwBuffer?.dispose()
                bwBuffer = FrameBuffer(w, h)
            }

            // 把当前默认帧缓冲内容拷到自定义缓冲
            bwBuffer!!.begin(Color.clear)
            // 注意：Arc 没有直接暴露默认帧缓冲的 Texture，
            // 这一步需要 ScreenUtils 或 Gl 接口，否则无法采样
            bwBuffer!!.end()

            // 用灰度着色器把 bwBuffer 画回屏幕
            bwShader!!.bind()
            bwShader!!.setUniformi("u_texture", 0)
            // Draw.rect(bwBuffer!!.texture, ...) 全屏绘制
            bwShader!!.unbind()
        }

        @Inject(method = ["draw"], at = [At("HEAD")], remap = false)
        fun onDrawHead(ci: CallbackInfo) {
            ensureInit()
        }
    }
}
