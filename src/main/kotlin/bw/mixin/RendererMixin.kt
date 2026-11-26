package bw.mixin

import arc.Core
import arc.graphics.GL20
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

    // 在游戏渲染的最后一步（即绘制完所有游戏内容），直接强行替换 Shader
    @Inject(method = ["draw"], at = [At("TAIL")], remap = false)
    fun onDrawTail(ci: CallbackInfo) {
        ensureInit()
        if (bwShader == null) return

        // 直接绑定我们的灰度 Shader，并用原生的全屏绘制指令覆盖当前画面
        bwShader!!.bind()
        
        // 绘制一个全屏四边形
        GL20.glBegin(GL20.GL_TRIANGLES)
        GL20.glVertex2f(-1f, -1f)
        GL20.glVertex2f(1f, -1f)
        GL20.glVertex2f(1f, 1f)

        GL20.glVertex2f(-1f, -1f)
        GL20.glVertex2f(1f, 1f)
        GL20.glVertex2f(-1f, 1f)
        GL20.glEnd()

        bwShader!!.unbind()
    }
}
