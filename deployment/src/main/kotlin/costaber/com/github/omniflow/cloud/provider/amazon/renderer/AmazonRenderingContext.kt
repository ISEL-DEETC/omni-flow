package costaber.com.github.omniflow.cloud.provider.amazon.renderer

import costaber.com.github.omniflow.model.Condition
import costaber.com.github.omniflow.model.Step
import costaber.com.github.omniflow.model.StepContext
import costaber.com.github.omniflow.model.VariableInitialization
import costaber.com.github.omniflow.renderer.IndentedRenderingContext
import costaber.com.github.omniflow.renderer.TermContext



class AmazonRenderingContext(
    indentationLevel: Int = 0,
    stringBuilder: StringBuilder = StringBuilder(),
    termContext: TermContext = object : TermContext {},
) : IndentedRenderingContext(indentationLevel, stringBuilder, termContext) {
    companion object {
        private var innerRenderingContext: MutableList<AmazonRenderingContext> = mutableListOf()
    }

    private lateinit var stepsNames: MutableList<String>
    private var currentStepName: String? = null
    private var lastVariable: VariableInitialization<*>? = null
    private var lastCondition: Condition? = null

    fun setVariables(variables: Collection<VariableInitialization<*>>) {
        lastVariable = variables.lastOrNull()
    }

    fun isNotLastVariable(variableInitialization: VariableInitialization<*>) =
        lastVariable != variableInitialization

    fun setConditions(conditions: Collection<Condition>) {
        lastCondition = conditions.lastOrNull()
    }

    fun isLastCondition(condition: Condition) =
        lastCondition == condition

    fun setSteps(steps: Collection<Step>) {
        var previousStep: Step? = null
        val result: MutableList<String> = mutableListOf()
        for (step in steps) {
            val stepName = if (previousStep?.next?.isNotBlank() == true) {
                previousStep.next
            } else {
                step.name
            }
            previousStep = step
            result.add(stepName)
        }
        stepsNames = result
    }

    fun setSteps(stepsContext: List<StepContext>) {
        stepsNames = stepsContext.map { "" }.toMutableList()
    }

    fun getNextStepName(): String? =
        stepsNames.firstOrNull()

    fun getNextStepNameAndAdvance(): String? {
        currentStepName = stepsNames.removeFirstOrNull()
        return currentStepName
    }

    fun getCurrentStepName(): String? =
        currentStepName

    fun appendInnerRenderingContext(innerContext: AmazonRenderingContext) {
        innerRenderingContext.add(innerContext)
    }

    fun popLastRenderingContext(): AmazonRenderingContext {
        return innerRenderingContext.removeLastOrNull() ?: this
    }

    fun getLastRenderingContext(): AmazonRenderingContext {
        return innerRenderingContext.lastOrNull() ?: this
    }

    fun nestedLevel(): Int {
        return innerRenderingContext.size
    }

    override fun toString(): String {
        return "AmazonRenderingContext(indentationLevel=${getIndentationLevel()},stepsNames=$stepsNames,currentStepName=$currentStepName,lastVariable=$lastVariable,lastCondition=$lastCondition,nestedLevel=${nestedLevel()})"
    }
}