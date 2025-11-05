package costaber.com.github.omniflow.cloud.provider.amazon.renderer.binaryexpression

import costaber.com.github.omniflow.cloud.provider.amazon.AMAZON_NUMERIC_GREATER_THAN
import costaber.com.github.omniflow.cloud.provider.amazon.AMAZON_NUMERIC_GREATER_THAN_PATH
import costaber.com.github.omniflow.cloud.provider.amazon.jackson.AmazonObjectMapper
import costaber.com.github.omniflow.cloud.provider.amazon.renderer.AmazonTermResolver
import costaber.com.github.omniflow.model.GreaterThanExpression
import costaber.com.github.omniflow.model.Value
import costaber.com.github.omniflow.renderer.IndentedRenderingContext

class AmazonGreaterThanExpressionRenderer(
    greaterThanExpression: GreaterThanExpression<*>,
    amazonTermResolver: AmazonTermResolver
) : AmazonBinaryExpressionRenderer(greaterThanExpression, amazonTermResolver) {

    override val amazonVariablePath: String = AMAZON_NUMERIC_GREATER_THAN_PATH
    private val objectMapper = AmazonObjectMapper.default

    override fun IndentedRenderingContext.renderValue(value: Value<*>) {
        add("$AMAZON_NUMERIC_GREATER_THAN${objectMapper.writeValueAsString(value.term())},")
    }
}