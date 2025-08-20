package costaber.com.github.omniflow.cloud.provider.amazon.renderer.binaryexpression

import costaber.com.github.omniflow.cloud.provider.amazon.AMAZON_NUMERIC_GREATER_THAN_EQUALS
import costaber.com.github.omniflow.cloud.provider.amazon.AMAZON_NUMERIC_GREATER_THAN_EQUALS_PATH
import costaber.com.github.omniflow.cloud.provider.amazon.jackson.AmazonObjectMapper
import costaber.com.github.omniflow.cloud.provider.amazon.renderer.AmazonTermResolver
import costaber.com.github.omniflow.model.GreaterThanOrEqualExpression
import costaber.com.github.omniflow.model.Value
import costaber.com.github.omniflow.renderer.IndentedRenderingContext

class AmazonGreaterThanOrEqualExpressionRenderer(
    greaterThanOrEqualExpression: GreaterThanOrEqualExpression<*>,
    amazonTermResolver: AmazonTermResolver
) : AmazonBinaryExpressionRenderer(greaterThanOrEqualExpression, amazonTermResolver) {

    override val amazonVariablePath: String = AMAZON_NUMERIC_GREATER_THAN_EQUALS_PATH
    private val objectMapper = AmazonObjectMapper.default

    override fun IndentedRenderingContext.renderValue(value: Value<*>) {
        add("$AMAZON_NUMERIC_GREATER_THAN_EQUALS${objectMapper.writeValueAsString(value.term())},")
    }
}