package costaber.com.github.omniflow.metrics

import costaber.com.github.omniflow.cloud.provider.amazon.provider.AmazonDefaultStrategyDeciderProvider
import costaber.com.github.omniflow.cloud.provider.amazon.renderer.AmazonRenderingContext
import costaber.com.github.omniflow.cloud.provider.amazon.traversor.AmazonTraversor
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

open class BenchmarkAmazonRenderer : BenchmarkWorkflowRenderer() {
    private lateinit var traversor: DepthFirstNodeVisitorTraversor
    private lateinit var amazonContextVisitor: NodeContextVisitor
    private lateinit var amazonRenderingContext: IndentedRenderingContext

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

        traversor = AmazonTraversor()
        amazonContextVisitor = NodeContextVisitor(AmazonDefaultStrategyDeciderProvider.createNodeRendererStrategyDecider())
        amazonRenderingContext = AmazonRenderingContext()
    }

    @Benchmark
    override fun benchmarkWorkflowWithIndependentSteps() {
        traversor.traverse(
            amazonContextVisitor,
            workflowWithIndependentSteps,
            amazonRenderingContext
        )
    }

    @Benchmark
    override fun benchmarkWorkflowUsingVariables() {
        traversor.traverse(
            amazonContextVisitor,
            workflowUsingVariables,
            amazonRenderingContext
        )
    }

    @Benchmark
    override fun benchmarkWorkflowWithBinaryConditions() {
        traversor.traverse(
            amazonContextVisitor,
            workflowWithBinaryConditions,
            amazonRenderingContext
        )
    }

    @Benchmark
    override fun benchmarkWorkflowWithMultipleDecisions() {
        traversor.traverse(
            amazonContextVisitor,
            workflowWithMultipleDecisions,
            amazonRenderingContext
        )
    }

    @Benchmark
    override fun benchmarkWorkflowWithIterationsWithRange() {
        traversor.traverse(
            amazonContextVisitor,
            workflowWithIterationsWithRange,
            amazonRenderingContext
        )
    }

    @Benchmark
    override fun benchmarkWorkflowWithIterationsWithForEach() {
        traversor.traverse(
            amazonContextVisitor,
            workflowWithIterationsWithForEach,
            amazonRenderingContext
        )
    }

    @Benchmark
    override fun benchmarkWorkflowWithParallelOneBranch() {
        traversor.traverse(
            amazonContextVisitor,
            workflowWithParallelOneBranch,
            amazonRenderingContext
        )
    }

    @Benchmark
    override fun benchmarkWorkflowWithParallelMultipleBranches() {
        traversor.traverse(
            amazonContextVisitor,
            workflowWithParallelMultipleBranches,
            amazonRenderingContext
        )
    }

    @Benchmark
    override fun benchmarkWorkflowWithParallelIterationWithRange() {
        traversor.traverse(
            amazonContextVisitor,
            withParallelIterationWithRange,
            amazonRenderingContext
        )
    }

    @Benchmark
    override fun benchmarkWorkflowWithParallelIterationForEach() {
        traversor.traverse(
            amazonContextVisitor,
            withParallelIterationWithForEach,
            amazonRenderingContext
        )
    }
}