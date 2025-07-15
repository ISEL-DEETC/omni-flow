package costaber.com.github.omniflow.generator

import costaber.com.github.omniflow.model.BranchContext
import costaber.com.github.omniflow.model.IterationContext
import costaber.com.github.omniflow.model.Range
import costaber.com.github.omniflow.model.Step
import costaber.com.github.omniflow.model.StepType
import costaber.com.github.omniflow.model.Variable

object StepGenerator {
    const val STEP_NAME: String = "stepNumber"

    @JvmStatic
    fun independent(stepName: String?, index: Int): Step {
        return Step(
            stepName + index,
            "Independent call step example",
            StepType.CALL,
            StepContextGenerator.independentCall()
        )
    }

    @JvmStatic
    fun usingVariables(stepName: String?, index: Int): Step {
        return Step(
            stepName + index,
            "Call step using variables example",
            StepType.CALL,
            StepContextGenerator.callUsingVariables()
        )
    }

    @JvmStatic
    fun assign(stepName: String?, index: Int): Step {
        return Step(
            stepName + index,
            "Assign step example",
            StepType.ASSIGN,
            StepContextGenerator.hardcodedVariables()
        )
    }

    @JvmStatic
    fun binaryConditional(stepName: String?, index: Int): Step {
        return Step(
            stepName + index,
            "Binary condition step example",
            StepType.CONDITIONAL,
            StepContextGenerator.ifElseSwitch(index)
        )
    }

    @JvmStatic
    fun multipleDecision(stepName: String?, index: Int): Step {
        return Step(
            stepName + index,
            "Multiple decision step example",
            StepType.CONDITIONAL,
            StepContextGenerator.multipleSwitch(index)
        )
    }

    @JvmStatic
    fun newTranslation(): Step {
        return Step(
            "new_translation",
            "Makes an HTTP POST request to the Cloud Translation API to translate text from English to Russian",
            StepType.CALL,
            StepContextGenerator.callTranslationApi()
        )
    }

    @JvmStatic
    fun assignTranslation(): Step {
        return Step(
            "assign_translation",
            "Assign the translated text to the translation_result variable",
            StepType.ASSIGN,
            StepContextGenerator.assignTranslationResult()
        )
    }

    @JvmStatic
    fun addPetToStore(): Step {
        return Step(
            "Add Pet to Store",
            "Add Pet to store by calling APIGW Rest endpoint",
            StepType.CALL,
            StepContextGenerator.addPetToStoreCall()
        )
    }

    @JvmStatic
    fun checkSuccess(): Step {
        return Step(
            "Pet was Added Successfully?",
            "Checks if the response was not successful",
            StepType.CONDITIONAL,
            StepContextGenerator.responseStatusCodeIs200()
        )
    }

    @JvmStatic
    val petsFromStore: Step
        get() = Step(
            "Retrieve Pet Store Data",
            "Get all data about the pet store",
            StepType.CALL,
            StepContextGenerator.getPetsCall()
        )

    @JvmStatic
    fun notifyWatchers(): Step {
        return Step(
            "Notify Result",
            "Call Notify Api to notify watchers with the result",
            StepType.CALL,
            StepContextGenerator.callNotifyApi()
        )
    }

    @JvmStatic
    fun iterationWithRange(stepName: String?, index: Int, steps: List<Step>, range: Range): Step {
        return Step(
            stepName + index,
            "Iterates through a collection",
            StepType.ITERATION,
            StepContextGenerator.iteration(steps, range)
        )
    }

    @JvmStatic
    fun iterationWithForEach(stepName: String?, index: Int, steps: List<Step>, forEachVariable: Variable): Step {
        return Step(
            stepName + index,
            "Iterates through a collection",
            StepType.ITERATION,
            StepContextGenerator.iteration(steps, forEachVariable)
        )
    }

    @JvmStatic
    fun parallelOneBranch(steps: List<Step>): Step {
        return Step(
            STEP_NAME,
            "Parallel Context",
            StepType.PARALLEL,
            StepContextGenerator.parallel(
                listOf(StepContextGenerator.branch("First Branch", steps)),
            )
        )
    }

    @JvmStatic
    fun parallelMultipleBranch(steps: List<Step>, numberOfBranches: Int): Step {
        return Step(
            STEP_NAME,
            "Parallel Context",
            StepType.PARALLEL,
            StepContextGenerator.parallel(
                (0 until numberOfBranches).map { StepContextGenerator.branch("Branch $it", steps) }
            )
        )
    }

    fun parallelIteration(iterationContext: IterationContext): Step {
        return Step(
            STEP_NAME,
            "Parallel Iteration",
            StepType.PARALLEL,
            StepContextGenerator.parallel(
                iterationContext = iterationContext
            )
        )
    }
}