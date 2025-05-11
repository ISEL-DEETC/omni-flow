package costaber.com.github.omniflow.cloud.provider.amazon.renderer

import costaber.com.github.omniflow.renderer.IndentedNodeRenderer
import costaber.com.github.omniflow.renderer.RenderingContext

abstract class AmazonRenderer() : IndentedNodeRenderer() {

    override fun beginRender(renderingContext: RenderingContext): String {
        val context = (renderingContext as AmazonRenderingContext).getLastRenderingContext()
        return super.beginRender(context)
    }

    override fun endRender(renderingContext: RenderingContext): String {
        val context = (renderingContext as AmazonRenderingContext).getLastRenderingContext()
        return super.endRender(context)
    }
}