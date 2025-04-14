package costaber.com.github.omniflow.cloud.provider.google.renderer

import costaber.com.github.omniflow.model.IterationContext
import costaber.com.github.omniflow.model.IterationForEachContext
import costaber.com.github.omniflow.model.IterationRangeContext
import costaber.com.github.omniflow.model.Node
import costaber.com.github.omniflow.renderer.IndentedNodeRenderer
import costaber.com.github.omniflow.renderer.IndentedRenderingContext
import costaber.com.github.omniflow.resource.util.render

class GoogleIterationRenderer(private val iterationContext: IterationContext) : IndentedNodeRenderer() {

    override val element: Node = iterationContext

    override fun internalBeginRender(renderingContext: IndentedRenderingContext): String =
        render(renderingContext) {
            addLine("for:")
            tab {
                addLine("value: ${iterationContext.value}")
                when (iterationContext) {
                    is IterationRangeContext -> addLine("range: [${iterationContext.range.min}, ${iterationContext.range.max}]")
                    is IterationForEachContext -> addLine("in: \${keys${iterationContext.forEachVariable.name}}")
                }
                add("steps:")
            }
            incIndentationLevel()
        }

    override fun internalEndRender(renderingContext: IndentedRenderingContext) =
        render(renderingContext) {
            decIndentationLevel()
        }
}