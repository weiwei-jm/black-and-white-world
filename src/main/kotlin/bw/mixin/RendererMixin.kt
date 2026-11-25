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
            // 这里暂时留空，为了让 GitHub Actions 顺利编译通过。
            // 等构建变绿后，我们再来这里写真正的画面变灰代码。
        }

        @Inject(method = ["draw"], at = [At("HEAD")], remap = false)
        fun onDrawHead(ci: CallbackInfo) {
            ensureInit()
        }
    }
}
