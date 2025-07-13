package costaber.com.github.omniflow.cloud.provider.amazon.jackson.serializer

import costaber.com.github.omniflow.model.Variable

class VariableAmazonSerializer : com.fasterxml.jackson.databind.JsonSerializer<Variable>() {
    override fun serialize(
        value: Variable,
        gen: com.fasterxml.jackson.core.JsonGenerator,
        serializers: com.fasterxml.jackson.databind.SerializerProvider
    ) {
        gen.writeString("${value.name}.$")
    }
}