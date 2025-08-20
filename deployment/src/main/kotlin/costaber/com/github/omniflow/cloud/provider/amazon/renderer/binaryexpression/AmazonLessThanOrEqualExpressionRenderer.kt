package costaber.com.github.omniflow.cloud.provider.amazon.renderer.binaryexpression

import costaber.com.github.omniflow.cloud.provider.amazon.AMAZON_NUMERIC_LESS_THAN_EQUALS
import costaber.com.github.omniflow.cloud.provider.amazon.AMAZON_NUMERIC_LESS_THAN_EQUALS_PATH
import costaber.com.github.omniflow.cloud.provider.amazon.jackson.AmazonObjectMapper
import costaber.com.github.omniflow.cloud.provider.amazon.renderer.AmazonTermResolver
import costaber.com.github.omniflow.model.LessThanOrEqualExpression
import costaber.com.github.omniflow.model.Value
import costaber.com.github.omniflow.renderer.IndentedRenderingContext

class AmazonLessThanOrEqualExpressionRenderer(
    lessThanOrEqualExpression: LessThanOrEqualExpression<*>,
    amazonTermResolver: AmazonTermResolver
) : AmazonBinaryExpressionRenderer(lessThanOrEqualExpression, amazonTermResolver) {

    override val amazonVariablePath: String = AMAZON_NUMERIC_LESS_THAN_EQUALS_PATH
    private val objectMapper = AmazonObjectMapper.default

    override fun IndentedRenderingContext.renderValue(value: Value<*>) {
        add("$AMAZON_NUMERIC_LESS_THAN_EQUALS${objectMapper.writeValueAsString(value.term())},")
    }
}