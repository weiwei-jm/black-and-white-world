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

    // 注入到 Renderer.draw() 方法的最后
    @Inject(method = ["draw"], at = [At("TAIL")], remap = false)
    fun onDrawTail(ci: CallbackInfo) {
        ensureInit()
        if (bwShader == null) return

        val w = Core.graphics.width
        val h = Core.graphics.height

        // 尺寸检查
        if (bwBuffer == null || bwBuffer!!.width != w || bwBuffer!!.height != h) {
            bwBuffer?.dispose()
            bwBuffer = FrameBuffer(w, h)
        }

        // 1. 拷贝屏幕像素到我们的缓冲（这步在 GPU 内部完成，不经过 CPU，所以很快）
        // 使用 Arc 底层自带的拷贝方法，避免 CPU 读取
        bwBuffer!!.begin(Color.clear)
        Draw.flush()
        bwBuffer!!.getTexture().draw(new arc.graphics.Pixmap(w, h), 0, 0)
        bwBuffer!!.end()

        // 2. 清空屏幕
        Core.graphics.clear(Color.clear)

        // 3. 设置正确的正交投影矩阵（保证矩形铺满屏幕，不受摄像机缩放影响）
        Draw.proj(0f, 0f, w.toFloat(), h.toFloat())
        
        // 4. 绑定灰度 Shader
        Draw.shader(bwShader!!)
        bwShader!!.setUniformi("u_texture", 0)
        
        // 5. 画全屏矩形 (直接传 texture 对象和四个 float 参数)
        val tex = bwBuffer!!.getTexture()
        Draw.rect(tex, 0f, 0f, w.toFloat(), h.toFloat())
        
        // 6. 恢复默认 Shader（无参数）
        Draw.shader()
        
        // 7. 恢复相机投影
        Draw.proj(Core.camera)
    }
}
