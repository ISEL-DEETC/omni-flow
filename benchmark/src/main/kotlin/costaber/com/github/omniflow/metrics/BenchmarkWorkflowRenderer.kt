package costaber.com.github.omniflow.metrics

import costaber.com.github.omniflow.model.Workflow
import org.openjdk.jmh.annotations.*
import java.util.concurrent.TimeUnit

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 5, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 100, timeUnit = TimeUnit.MILLISECONDS)
@Fork(1)
@State(
    Scope.Benchmark
)
abstract class BenchmarkWorkflowRenderer {
    @Param("1", "10", "100", "1000", "10000", "100000")
    var numberOfSteps: Int = 0
    protected lateinit var workflowWithIndependentSteps: Workflow
    protected lateinit var workflowUsingVariables: Workflow
    protected lateinit var workflowWithBinaryConditions: Workflow
    protected lateinit var workflowWithMultipleDecisions: Workflow

    abstract fun benchmarkWorkflowWithIndependentSteps()

    abstract fun benchmarkWorkflowUsingVariables()

    abstract fun benchmarkWorkflowWithBinaryConditions()

    abstract fun benchmarkWorkflowWithMultipleDecisions()
}