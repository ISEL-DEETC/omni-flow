package costaber.com.github.omniflow.metrics

import costaber.com.github.omniflow.cloud.provider.google.provider.GoogleDefaultStrategyDeciderProvider.createNodeRendererStrategyDecider
import costaber.com.github.omniflow.cloud.provider.google.renderer.GoogleRenderingContext
import costaber.com.github.omniflow.cloud.provider.google.renderer.GoogleTermContext
import costaber.com.github.omniflow.generator.WorkflowGenerator.usingVariables
import costaber.com.github.omniflow.generator.WorkflowGenerator.withBinaryConditions
import costaber.com.github.omniflow.generator.WorkflowGenerator.withIndependentSteps
import costaber.com.github.omniflow.generator.WorkflowGenerator.withIterationsForEach
import costaber.com.github.omniflow.generator.WorkflowGenerator.withIterationsRange
import costaber.com.github.omniflow.generator.WorkflowGenerator.withMultipleDecisions
import costaber.com.github.omniflow.generator.WorkflowGenerator.withParallelIterationRange
import costaber.com.github.omniflow.generator.WorkflowGenerator.withParallelIterationWithForEach
import costaber.com.github.omniflow.generator.WorkflowGenerator.withParallelMultipleBranches
import costaber.com.github.omniflow.generator.WorkflowGenerator.withParallelOneBranch
import costaber.com.github.omniflow.renderer.IndentedRenderingContext
import costaber.com.github.omniflow.traversor.DepthFirstNodeVisitorTraversor
import costaber.com.github.omniflow.visitor.NodeContextVisitor
import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.Setup

open class BenchmarkGoogleRenderer : BenchmarkWorkflowRenderer() {
    private lateinit var traversor: DepthFirstNodeVisitorTraversor
    private lateinit var googleContextVisitor: NodeContextVisitor
    private lateinit var googleRenderingContext: IndentedRenderingContext

    @Setup
    fun setup() {
        workflowWithIndependentSteps = withIndependentSteps(numberOfSteps)
        workflowUsingVariables = usingVariables(numberOfSteps)
        workflowWithBinaryConditions = withBinaryConditions(numberOfSteps)
        workflowWithMultipleDecisions = withMultipleDecisions(numberOfSteps)
        workflowWithIterationsWithRange = withIterationsRange(numberOfSteps)
        workflowWithIterationsWithForEach = withIterationsForEach(numberOfSteps)
        workflowWithParallelOneBranch = withParallelOneBranch(numberOfSteps)
        workflowWithParallelMultipleBranches = withParallelMultipleBranches(numberOfSteps)
        withParallelIterationWithRange = withParallelIterationRange(numberOfSteps)
        withParallelIterationWithForEach = withParallelIterationWithForEach(numberOfSteps)

        traversor = DepthFirstNodeVisitorTraversor()
        googleContextVisitor = NodeContextVisitor(createNodeRendererStrategyDecider())
        googleRenderingContext = GoogleRenderingContext(0, StringBuilder(), GoogleTermContext())
    }

    @Benchmark
    override fun benchmarkWorkflowWithIndependentSteps() {
        traversor.traverse(
            googleContextVisitor,
            workflowWithIndependentSteps,
            googleRenderingContext
        )
    }

    @Benchmark
    override fun benchmarkWorkflowUsingVariables() {
        traversor.traverse(
            googleContextVisitor,
            workflowUsingVariables,
            googleRenderingContext
        )
    }

    @Benchmark
    override fun benchmarkWorkflowWithBinaryConditions() {
        traversor.traverse(
            googleContextVisitor,
            workflowWithBinaryConditions,
            googleRenderingContext
        )
    }

    @Benchmark
    override fun benchmarkWorkflowWithMultipleDecisions() {
        traversor.traverse(
            googleContextVisitor,
            workflowWithMultipleDecisions,
            googleRenderingContext
        )
    }

    @Benchmark
    override fun benchmarkWorkflowWithIterationsWithRange() {
        traversor.traverse(
            googleContextVisitor,
            workflowWithIterationsWithRange,
            googleRenderingContext
        )
    }

    @Benchmark
    override fun benchmarkWorkflowWithIterationsWithForEach() {
        traversor.traverse(
            googleContextVisitor,
            workflowWithIterationsWithForEach,
            googleRenderingContext
        )
    }

    @Benchmark
    override fun benchmarkWorkflowWithParallelOneBranch() {
        traversor.traverse(
            googleContextVisitor,
            workflowWithParallelOneBranch,
            googleRenderingContext
        )
    }

    @Benchmark
    override fun benchmarkWorkflowWithParallelMultipleBranches() {
        traversor.traverse(
            googleContextVisitor,
            workflowWithParallelMultipleBranches,
            googleRenderingContext
        )
    }

    @Benchmark
    override fun benchmarkWorkflowWithParallelIterationWithRange() {
        traversor.traverse(
            googleContextVisitor,
            withParallelIterationWithRange,
            googleRenderingContext
        )
    }

    @Benchmark
    override fun benchmarkWorkflowWithParallelIterationForEach() {
        traversor.traverse(
            googleContextVisitor,
            withParallelIterationWithForEach,
            googleRenderingContext
        )
    }
}