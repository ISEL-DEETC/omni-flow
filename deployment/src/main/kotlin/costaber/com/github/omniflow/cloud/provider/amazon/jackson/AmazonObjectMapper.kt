package costaber.com.github.omniflow.cloud.provider.amazon.jackson

import com.fasterxml.jackson.core.Version
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import costaber.com.github.omniflow.cloud.provider.amazon.jackson.serializer.VariableAmazonSerializer
import costaber.com.github.omniflow.model.Variable

class AmazonObjectMapper {
    companion object {
        private val module = SimpleModule(
            "VariableAmazonSerializer",
            Version(1, 0, 0, null, null, null)
        ).addSerializer(Variable::class.javaObjectType, VariableAmazonSerializer())

        val default = ObjectMapper()
            .registerModule(module)
    }
}