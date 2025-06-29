package costaber.com.github.omniflow.cloud.provider.google.jackson.serializers

import costaber.com.github.omniflow.model.Variable

class VariableGoogleSerializer : com.fasterxml.jackson.databind.JsonSerializer<Variable>() {
    override fun serialize(value: Variable, gen: com.fasterxml.jackson.core.JsonGenerator, serializers: com.fasterxml.jackson.databind.SerializerProvider) {
        gen.writeRawValue("\${${value.term()}}")
    }
}