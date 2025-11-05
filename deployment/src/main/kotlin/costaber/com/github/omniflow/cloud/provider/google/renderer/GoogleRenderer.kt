package costaber.com.github.omniflow.cloud.provider.google.renderer

import costaber.com.github.omniflow.renderer.IndentedNodeRenderer
import costaber.com.github.omniflow.renderer.RenderingContext

abstract class GoogleRenderer() : IndentedNodeRenderer() {

    override fun beginRender(renderingContext: RenderingContext): String {
        val context = (renderingContext as GoogleRenderingContext).getLastRenderingContext()
        return super.beginRender(context)
    }

    override fun endRender(renderingContext: RenderingContext): String {
        val context = (renderingContext as GoogleRenderingContext).getLastRenderingContext()
        return super.endRender(context)
    }
}