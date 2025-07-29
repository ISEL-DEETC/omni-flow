package costaber.com.github.omniflow.cloud.provider.amazon.renderer.binaryexpression

import costaber.com.github.omniflow.cloud.provider.amazon.AMAZON_BOOLEAN_EQUALS
import costaber.com.github.omniflow.cloud.provider.amazon.AMAZON_NUMERIC_EQUALS
import costaber.com.github.omniflow.cloud.provider.amazon.AMAZON_STRING_EQUALS
import costaber.com.github.omniflow.cloud.provider.amazon.AMAZON_STRING_EQUALS_PATH
import costaber.com.github.omniflow.cloud.provider.amazon.jackson.AmazonObjectMapper
import costaber.com.github.omniflow.cloud.provider.amazon.renderer.AmazonTermResolver
import costaber.com.github.omniflow.model.EqualToExpression
import costaber.com.github.omniflow.model.Value
import costaber.com.github.omniflow.renderer.IndentedRenderingContext

class AmazonEqualToExpressionRenderer(
    equalToExpression: EqualToExpression<*>,
    amazonTermResolver: AmazonTermResolver,
) : AmazonBinaryExpressionRenderer(equalToExpression, amazonTermResolver) {

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
}