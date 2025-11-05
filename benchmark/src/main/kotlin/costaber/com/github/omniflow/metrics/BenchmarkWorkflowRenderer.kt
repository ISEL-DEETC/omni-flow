package costaber.com.github.omniflow.metrics

import costaber.com.github.omniflow.generator.WorkflowGenerator.withIndependentSteps
import costaber.com.github.omniflow.generator.WorkflowGenerator.usingVariables
import costaber.com.github.omniflow.generator.WorkflowGenerator.withBinaryConditions
import costaber.com.github.omniflow.generator.WorkflowGenerator.withMultipleDecisions
import costaber.com.github.omniflow.generator.WorkflowGenerator.withIterationsRange
import costaber.com.github.omniflow.generator.WorkflowGenerator.withIterationsForEach
import costaber.com.github.omniflow.generator.WorkflowGenerator.withParallelOneBranch
import costaber.com.github.omniflow.generator.WorkflowGenerator.withParallelMultipleBranches
import costaber.com.github.omniflow.generator.WorkflowGenerator.withParallelIterationRange
import costaber.com.github.omniflow.generator.WorkflowGenerator.withParallelIterationWithForEach
import costaber.com.github.omniflow.model.Workflow
import org.openjdk.jmh.annotations.*
import java.util.concurrent.TimeUnit

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 5, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 100, timeUnit = TimeUnit.MILLISECONDS)
@Fork(1)
@State(Scope.Benchmark)
abstract class BenchmarkWorkflowRenderer {
    abstract fun benchmarkWorkflowWithIndependentSteps(state: WorkflowWithIndependentStepsState)
    abstract fun benchmarkWorkflowUsingVariables(state: WorkflowUsingVariablesState)
    abstract fun benchmarkWorkflowWithBinaryConditions(state: WorkflowWithBinaryConditionsState)
    abstract fun benchmarkWorkflowWithMultipleDecisions(state: WorkflowWithMultipleDecisionsState)
    abstract fun benchmarkWorkflowWithIterationsWithRange(state: WorkflowWithIterationsWithRangeState)
    abstract fun benchmarkWorkflowWithIterationsWithForEach(state: WorkflowWithIterationsWithForEachState)
    abstract fun benchmarkWorkflowWithParallelOneBranch(state: WorkflowWithParallelOneBranchState)
    abstract fun benchmarkWorkflowWithParallelMultipleBranches(state: WorkflowWithParallelMultipleBranchesState)
    abstract fun benchmarkWorkflowWithParallelIterationWithRange(state: WorkflowWithParallelIterationWithRangeState)
    abstract fun benchmarkWorkflowWithParallelIterationForEach(state: WorkflowWithParallelIterationForEachState)
}

@State(Scope.Benchmark)
open class BaseState {
    @Param("1", "10", "100", "1000", "10000", "100000")
    var numberOfSteps: Int = 0
}

@State(Scope.Benchmark)
open class WorkflowWithIndependentStepsState: BaseState() {
    lateinit var workflowWithIndependentSteps: Workflow
    @Setup
    fun setup() {
        workflowWithIndependentSteps = withIndependentSteps(numberOfSteps)
    }
}

@State(Scope.Benchmark)
open class WorkflowUsingVariablesState: BaseState() {
    lateinit var workflowUsingVariables: Workflow
    @Setup
    fun setup() {
        workflowUsingVariables = usingVariables(numberOfSteps)
    }
}

@State(Scope.Benchmark)
open class WorkflowWithBinaryConditionsState: BaseState() {
    lateinit var workflowWithBinaryConditions: Workflow
    @Setup
    fun setup() {
        workflowWithBinaryConditions = withBinaryConditions(numberOfSteps)
    }
}

@State(Scope.Benchmark)
open class WorkflowWithMultipleDecisionsState: BaseState() {
    lateinit var workflowWithMultipleDecisions: Workflow
    @Setup
    fun setup() {
        workflowWithMultipleDecisions = withMultipleDecisions(numberOfSteps)
    }
}

@State(Scope.Benchmark)
open class WorkflowWithIterationsWithRangeState: BaseState() {
    lateinit var workflowWithIterationsWithRange: Workflow
    @Setup
    fun setup() {
        workflowWithIterationsWithRange = withIterationsRange(numberOfSteps)
    }
}

@State(Scope.Benchmark)
open class WorkflowWithIterationsWithForEachState: BaseState() {
    lateinit var workflowWithIterationsWithForEach: Workflow
    @Setup
    fun setup() {
        workflowWithIterationsWithForEach = withIterationsForEach(numberOfSteps)
    }
}

@State(Scope.Benchmark)
open class WorkflowWithParallelOneBranchState: BaseState() {
    lateinit var workflowWithParallelOneBranch: Workflow
    @Setup
    fun setup() {
        workflowWithParallelOneBranch = withParallelOneBranch(numberOfSteps)
    }
}

@State(Scope.Benchmark)
open class WorkflowWithParallelMultipleBranchesState: BaseState() {
    lateinit var workflowWithParallelMultipleBranches: Workflow
    @Setup
    fun setup() {
        workflowWithParallelMultipleBranches = withParallelMultipleBranches(numberOfSteps)
    }
}

@State(Scope.Benchmark)
open class WorkflowWithParallelIterationWithRangeState: BaseState() {
    lateinit var withParallelIterationWithRange: Workflow
    @Setup
    fun setup() {
        withParallelIterationWithRange = withParallelIterationRange(numberOfSteps)
    }
}

@State(Scope.Benchmark)
open class WorkflowWithParallelIterationForEachState: BaseState() {
    lateinit var withParallelIterationWithForEach: Workflow
    @Setup
    fun setup() {
        withParallelIterationWithForEach = withParallelIterationWithForEach(numberOfSteps)
    }
}

