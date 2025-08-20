package costaber.com.github.omniflow.generator

import costaber.com.github.omniflow.generator.StepGenerator.STEP_NAME
import costaber.com.github.omniflow.generator.StepGenerator.addPetToStore
import costaber.com.github.omniflow.generator.StepGenerator.assign
import costaber.com.github.omniflow.generator.StepGenerator.assignTranslation
import costaber.com.github.omniflow.generator.StepGenerator.binaryConditional
import costaber.com.github.omniflow.generator.StepGenerator.checkSuccess
import costaber.com.github.omniflow.generator.StepGenerator.independent
import costaber.com.github.omniflow.generator.StepGenerator.iterationWithForEach
import costaber.com.github.omniflow.generator.StepGenerator.iterationWithRange
import costaber.com.github.omniflow.generator.StepGenerator.multipleDecision
import costaber.com.github.omniflow.generator.StepGenerator.newTranslation
import costaber.com.github.omniflow.generator.StepGenerator.notifyWatchers
import costaber.com.github.omniflow.generator.StepGenerator.parallelIteration
import costaber.com.github.omniflow.generator.StepGenerator.parallelMultipleBranch
import costaber.com.github.omniflow.generator.StepGenerator.parallelOneBranch
import costaber.com.github.omniflow.generator.StepGenerator.petsFromStore
import costaber.com.github.omniflow.generator.StepGenerator.usingVariables
import costaber.com.github.omniflow.model.Range
import costaber.com.github.omniflow.model.Step
import costaber.com.github.omniflow.model.Variable
import costaber.com.github.omniflow.model.Workflow

object WorkflowGenerator {
    private const val WORKFLOW_NAME = "testWorkflow"
    private const val WORKFLOW_DESCRIPTION = "Workflow Example"
    private const val WORKFLOW_INPUT = "args"
    private const val WORKFLOW_RESULT = "result"

    /**
     * Generates a workflow with @stepsNumber steps
     * without any relation, independent.
     *
     * @param stepsNumber number of steps to generate
     * @return a workflow with independent steps
     */
    @JvmStatic
    fun withIndependentSteps(stepsNumber: Int): Workflow {
        val steps = (0 until stepsNumber).map { independent(STEP_NAME, it) }
        return Workflow(
            WORKFLOW_NAME,
            WORKFLOW_DESCRIPTION,
            WORKFLOW_INPUT,
            steps,
            WORKFLOW_RESULT
        )
    }

    /**
     * Generates a workflow with at least @stepsNumber
     * steps, where the maximum steps generated are
     * `stepsNumber` + 1. It will create steps,
     * where half are calls and the other half are assigns.
     * The last step is always a call.
     *
     * @param stepsNumber number of steps to generate
     * @return a workflow with steps that use variables
     */
    @JvmStatic
    fun usingVariables(stepsNumber: Int): Workflow {
        val steps = mutableListOf<Step>()
        var idx = 0
        while (idx < stepsNumber) {
            steps.add(assign(STEP_NAME, idx++))
            steps.add(usingVariables(STEP_NAME, idx++))
        }
        return Workflow(
            WORKFLOW_NAME,
            WORKFLOW_DESCRIPTION,
            WORKFLOW_INPUT,
            steps,
            WORKFLOW_RESULT
        )
    }

    /**
     * Generates a workflow with at least @stepsNumber
     * steps, where the maximum steps generated are
     * `stepsNumber` + 2. It will create steps,
     * where 1/3 are binary conditions and the rest
     * 2/3 are calls. The last 2 steps always calls
     *
     * @param stepsNumber number of steps to generate
     * @return a workflow with steps that use binary conditions
     */
    @JvmStatic
    fun withBinaryConditions(stepsNumber: Int): Workflow {
        val steps: MutableList<Step> = mutableListOf()
        val firstStep = independent(STEP_NAME, 0)
        steps.add(firstStep)
        var idx = 1
        while (idx < stepsNumber) {
            steps.add(binaryConditional(STEP_NAME, idx++))
            steps.add(independent(STEP_NAME, idx++))
            steps.add(independent(STEP_NAME, idx++))
        }
        return Workflow(
            WORKFLOW_NAME,
            WORKFLOW_DESCRIPTION,
            WORKFLOW_INPUT,
            steps,
            WORKFLOW_RESULT
        )
    }

    /**
     * Generates a workflow with at least @stepsNumber
     * steps, where the maximum steps generated are
     * `stepsNumber` + 4. It will create steps,
     * where 1/5 are switch conditions and the rest
     * 4/5 are calls. The last 2 steps always calls
     *
     * @param stepsNumber number of steps to generate
     * @return a workflow with steps that use binary conditions
     */
    @JvmStatic
    fun withMultipleDecisions(stepsNumber: Int): Workflow {
        val steps: MutableList<Step> = mutableListOf()
        val firstStep = independent(STEP_NAME, 0)
        steps.add(firstStep)
        var idx = 1
        while (idx < stepsNumber) {
            steps.add(multipleDecision(STEP_NAME, idx++))
            steps.add(independent(STEP_NAME, idx++))
            steps.add(independent(STEP_NAME, idx++))
            steps.add(independent(STEP_NAME, idx++))
            steps.add(independent(STEP_NAME, idx++))
        }
        return Workflow(
            WORKFLOW_NAME,
            WORKFLOW_DESCRIPTION,
            WORKFLOW_INPUT,
            steps,
            WORKFLOW_RESULT
        )
    }

    @JvmStatic
    fun textTranslator(): Workflow {
        val steps: MutableList<Step> = mutableListOf()
        steps.add(newTranslation())
        steps.add(assignTranslation())
        return Workflow(
            WORKFLOW_NAME,
            WORKFLOW_DESCRIPTION,
            WORKFLOW_INPUT,
            steps,
            "translation_result"
        )
    }

    @JvmStatic
    fun saveAndGetPetFromStore(): Workflow {
        val steps: MutableList<Step> = mutableListOf()
        steps.add(addPetToStore())
        steps.add(checkSuccess())
        steps.add(petsFromStore)
        steps.add(notifyWatchers())
        return Workflow(
            WORKFLOW_NAME,
            "Calling APIGW HTTP Endpoint",
            WORKFLOW_INPUT,
            steps,
            "NotificationStatus"
        )
    }

    fun withIterationsRange(stepsNumber: Int): Workflow = Workflow(
        WORKFLOW_NAME,
        WORKFLOW_DESCRIPTION,
        WORKFLOW_INPUT,
        listOf(
            iterationWithRange(
                STEP_NAME, 0, (0 until stepsNumber).map { independent("INNER$STEP_NAME", it) },
                Range(1, 10)
            )
        ),
        WORKFLOW_RESULT
    )

    fun withIterationsForEach(stepsNumber: Int): Workflow = Workflow(
        WORKFLOW_NAME,
        WORKFLOW_DESCRIPTION,
        WORKFLOW_INPUT,
        listOf(
            iterationWithForEach(
                STEP_NAME, 0, (0 until stepsNumber).map { independent("INNER$STEP_NAME", it) },
                Variable("number")
            )
        ),
        WORKFLOW_RESULT
    )

    fun withParallelOneBranch(stepsNumber: Int): Workflow = Workflow(
        WORKFLOW_NAME,
        WORKFLOW_DESCRIPTION,
        WORKFLOW_INPUT,
        listOf(parallelOneBranch((0 until stepsNumber).map { independent("INNER$STEP_NAME", it) })),
        WORKFLOW_RESULT
    )

    fun withParallelMultipleBranches(stepsNumber: Int, bucketSize: Int = 10): Workflow {
        val steps = mutableListOf<Step>()
        0.until(stepsNumber / bucketSize).map {
            steps.add(
                parallelMultipleBranch(
                    (0 until 1).map { independent("INNER$it$STEP_NAME", it) },
                    bucketSize
                )
            )
        }
        if (stepsNumber % bucketSize != 0) {
            steps.add(
                parallelMultipleBranch(
                    (0 until 1).map { independent("INNER$it$STEP_NAME", it) },
                    bucketSize
                )
            )
        }
        return Workflow(
            WORKFLOW_NAME,
            WORKFLOW_DESCRIPTION,
            WORKFLOW_INPUT,
            steps,
            WORKFLOW_RESULT
        )
    }

    fun withParallelIterationRange(stepNumber: Int): Workflow = Workflow(
        WORKFLOW_NAME,
        WORKFLOW_DESCRIPTION,
        WORKFLOW_INPUT,
        listOf(
            parallelIteration(
                StepContextGenerator.iteration(
                    (0 until stepNumber).map { independent("INNER$it$STEP_NAME", it) },
                    Range(1, 10)
                )
            )
        ),
        WORKFLOW_RESULT
    )

    fun withParallelIterationWithForEach(stepNumber: Int): Workflow = Workflow(
        WORKFLOW_NAME,
        WORKFLOW_DESCRIPTION,
        WORKFLOW_INPUT,
        listOf(
            parallelIteration(
                StepContextGenerator.iteration(
                    (0 until stepNumber).map { independent("INNER$it$STEP_NAME", it) },
                    Variable("number1"),
                )
            )
        ),
        WORKFLOW_RESULT
    )
}