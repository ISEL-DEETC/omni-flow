package costaber.com.github.omniflow.cloud.provider.amazon.renderer

import costaber.com.github.omniflow.cloud.provider.amazon.AMAZON_CLOSE_OBJECT_WITH_COMMA
import costaber.com.github.omniflow.cloud.provider.amazon.AMAZON_NEXT
import costaber.com.github.omniflow.model.Condition
import costaber.com.github.omniflow.model.Node
import costaber.com.github.omniflow.renderer.IndentedNodeRenderer
import costaber.com.github.omniflow.renderer.IndentedRenderingContext
import costaber.com.github.omniflow.resource.util.render

class AmazonConditionRenderer(private val condition: Condition) : AmazonRenderer() {

    override val element: Node = condition

    override fun internalBeginRender(renderingContext: IndentedRenderingContext): String {
        val amazonContext = (renderingContext as AmazonRenderingContext).getLastRenderingContext()
        return render(amazonContext) {
            add("{")
        }
    }

    override fun internalEndRender(renderingContext: IndentedRenderingContext): String {
        val amazonContext = (renderingContext as AmazonRenderingContext).getLastRenderingContext()
        return render(amazonContext) {
            tab {
                addLine("$AMAZON_NEXT\"${condition.jump}\"")
            }
            if (amazonContext.isLastCondition(condition)) {
                add("}")
            } else {
                add(AMAZON_CLOSE_OBJECT_WITH_COMMA)
            }
        }
    }
}