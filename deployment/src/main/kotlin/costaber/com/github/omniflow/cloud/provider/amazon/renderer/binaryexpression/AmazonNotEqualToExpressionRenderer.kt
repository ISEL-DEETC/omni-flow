package costaber.com.github.omniflow.cloud.provider.amazon.renderer.binaryexpression

import costaber.com.github.omniflow.cloud.provider.amazon.*
import costaber.com.github.omniflow.cloud.provider.amazon.jackson.AmazonObjectMapper
import costaber.com.github.omniflow.cloud.provider.amazon.renderer.AmazonTermResolver
import costaber.com.github.omniflow.model.NotEqualToExpression
import costaber.com.github.omniflow.model.Value
import costaber.com.github.omniflow.renderer.IndentedRenderingContext
import costaber.com.github.omniflow.resource.util.render

class AmazonNotEqualToExpressionRenderer(
    private val notEqualToExpression: NotEqualToExpression<*>,
    private val amazonTermResolver: AmazonTermResolver
) : AmazonBinaryExpressionRenderer(notEqualToExpression, amazonTermResolver) {

    override val amazonVariablePath: String = AMAZON_STRING_EQUALS_PATH
    private val objectMapper = AmazonObjectMapper.default

    override fun IndentedRenderingContext.renderValue(value: Value<*>) {
        when (value.value) {
            is Number -> add("$AMAZON_NUMERIC_EQUALS${value.term()},")
            is String -> add("$AMAZON_STRING_EQUALS\"${value.term()}\",")
            is Boolean -> add("$AMAZON_BOOLEAN_EQUALS${value.term()},")
            else -> add("$AMAZON_STRING_EQUALS\"${objectMapper.writeValueAsString(value.term())}\",")
        }
    }

    override fun internalBeginRender(renderingContext: IndentedRenderingContext): String =
        render(renderingContext) {
            addLine(AMAZON_NOT)
            tab {
                super.internalBeginRender(renderingContext)
            }
        }

    override fun internalEndRender(renderingContext: IndentedRenderingContext): String =
        render(renderingContext) {
            super.internalEndRender(renderingContext)
            add(AMAZON_CLOSE_OBJECT_WITH_COMMA)
        }
}