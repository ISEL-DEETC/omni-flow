package costaber.com.github.omniflow.cloud.provider.google.jackson

import com.fasterxml.jackson.core.Version
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import costaber.com.github.omniflow.cloud.provider.google.jackson.serializers.VariableGoogleSerializer
import costaber.com.github.omniflow.model.Variable

class GoogleObjectMapper {
    companion object {
        private val module = SimpleModule(
            "VariableGoogleSerializer",
            Version(1, 0, 0, null, null, null)
        ).addSerializer(Variable::class.javaObjectType, VariableGoogleSerializer())

        val default: ObjectMapper = ObjectMapper(GoogleYAMLFactory.default)
            .registerModule(module)
    }
}