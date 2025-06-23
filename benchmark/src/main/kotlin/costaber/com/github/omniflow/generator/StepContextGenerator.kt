package costaber.com.github.omniflow.generator

import costaber.com.github.omniflow.generator.StepGenerator.STEP_NAME
import costaber.com.github.omniflow.model.*
import java.util.*

object StepContextGenerator {
    private const val VARIABLE_NAME_1 = "number1"
    private const val VARIABLE_NAME_2 = "number2"

    fun independentCall(): StepContext {
        val headers = mapOf(
            "Content-Type" to  Value("application/json")
        )
        val queries = mapOf(
            "number" to Value(1)
        )
        return CallContext(
            HttpMethod.GET,
            "example.com",
            "/example",
            null,
            PersonNameBody("John", "Johnson"),
            headers,
            queries,
            5L,
            "result"
        )
    }

    fun callUsingVariables(): StepContext {
        val queries = mapOf(
            "number1" to Variable(VARIABLE_NAME_1),
            "number2" to Variable(VARIABLE_NAME_2)
        )
        return CallContext(
            HttpMethod.GET,
            "example.com",
            "/example",
            null,
            PersonNameBody("John", "Johnson"),
            emptyMap(),
            queries,
            5L,
            "result"
        )
    }

    fun hardcodedVariables(): StepContext {
        val random = Random()
        val random1 = random.nextInt()
        val random2 = random.nextInt()
        val variables = listOf(
            VariableInitialization(Variable(VARIABLE_NAME_1), Value(random1)),
            VariableInitialization(Variable(VARIABLE_NAME_2), Value(random2))
        )
        return AssignContext(variables)
    }

    fun ifElseSwitch(index: Int): StepContext {
        val greaterThanOrEqual = GreaterThanExpression(
            Variable("result.number"),
            Value(123)
        )
        val conditions = listOf(
            Condition(greaterThanOrEqual, STEP_NAME + (index + 1))
        )
        return ConditionalContext(conditions, STEP_NAME + (index + 2))
    }

    fun multipleSwitch(index: Int): StepContext {
        val equalTo = EqualToExpression(
            Variable("result"),
            Value(123)
        )
        val greaterThan = GreaterThanExpression(
            Variable("result"),
            Value(123)
        )
        val lessThan = LessThanExpression(
            Variable("result"),
            Value(123)
        )
        val conditions = listOf(
            Condition(equalTo, STEP_NAME + (index + 1)),
            Condition(greaterThan, STEP_NAME + (index + 2)),
            Condition(lessThan, STEP_NAME + (index + 3))
        )
        return ConditionalContext(conditions, STEP_NAME + (index + 4))
    }

    fun callTranslationApi(): StepContext {
        return CallContext(
            HttpMethod.POST,
            "https://translation.googleapis.com",
            "/v3/projects/19823573:translateText",
            Authentication("OAuth2", null, null, null),
            TranslationApiBody("Hello, my name is John!", "en-US", "ru-RU"),
            emptyMap(),
            emptyMap(),
            null,
            "translate_response"
        )
    }

    fun assignTranslationResult(): StepContext {
        val variableAssigned = Variable("translation_result")
        val variableNewValue = Variable("translate_response.translations[0].translatedText")
        val translationResultVariable = VariableInitialization(variableAssigned, variableNewValue)
        val variables = listOf(translationResultVariable)
        return AssignContext(variables)
    }

    fun addPetToStoreCall(): StepContext {
        return CallContext(
            HttpMethod.POST,
            "petstore.execute-api.us-east-1.amazonaws.com",
            "/pets",
            Authentication("IAM_ROLE", null, null, null),
            "$.NewPet",
            emptyMap(),
            emptyMap(),
            null,
            "ResponseBody"
        )
    }

    fun responseStatusCodeIs200(): StepContext {
        val is200 = EqualToExpression(
            Variable("StatusCode"),
            Value(200)
        )
        val conditions = listOf(Condition(is200, "Retrieve Pet Store Data"))
        return ConditionalContext(conditions, "Notify Result")
    }

    fun getPetsCall(): StepContext {
        return CallContext(
            HttpMethod.GET,
            "petstore.execute-api.us-east-1.amazonaws.com",
            "/pets",
            Authentication("IAM_ROLE", null, null, null),
            null,
            emptyMap(),
            emptyMap(),
            null,
            "Pets"
        )
    }

    fun callNotifyApi(): StepContext {
        return CallContext(
            HttpMethod.POST,
            "notifyApp.execute-api.us-east-1.amazonaws.com",
            "/",
            Authentication("IAM_ROLE", null, null, null),
            "Add pet to store status code - $.StatusCode",
            emptyMap(),
            emptyMap(),
            null,
            "NotificationStatus"
        )
    }

    internal class PersonNameBody(var firstName: String?, var lastName: String?)

    internal class TranslationApiBody(
        var contents: String?,
        var sourceLanguageCode: String?,
        var targetLanguageCode: String?
    )
}