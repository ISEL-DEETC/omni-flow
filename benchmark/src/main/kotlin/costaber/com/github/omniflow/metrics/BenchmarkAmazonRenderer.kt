package costaber.com.github.omniflow.metrics

import costaber.com.github.omniflow.cloud.provider.amazon.provider.AmazonDefaultStrategyDeciderProvider
import costaber.com.github.omniflow.cloud.provider.amazon.renderer.AmazonRenderingContext
import costaber.com.github.omniflow.cloud.provider.amazon.traversor.AmazonTraversor
import costaber.com.github.omniflow.renderer.IndentedRenderingContext
import costaber.com.github.omniflow.traversor.DepthFirstNodeVisitorTraversor
import costaber.com.github.omniflow.visitor.NodeContextVisitor
import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.Level
import org.openjdk.jmh.annotations.Setup

open class BenchmarkAmazonRenderer : BenchmarkWorkflowRenderer() {
    private lateinit var traversor: DepthFirstNodeVisitorTraversor
    private lateinit var amazonContextVisitor: NodeContextVisitor
    private lateinit var amazonRenderingContext: IndentedRenderingContext


    @Setup(Level.Iteration)
    fun setup() {
        traversor = AmazonTraversor()
        amazonContextVisitor = NodeContextVisitor(AmazonDefaultStrategyDeciderProvider.createNodeRendererStrategyDecider())
        amazonRenderingContext = AmazonRenderingContext()
        System.gc()
    }

    @Benchmark
    override fun benchmarkWorkflowWithIndependentSteps(state: WorkflowWithIndependentStepsState) {
        traversor.traverse(
            amazonContextVisitor,
            state.workflowWithIndependentSteps,
            amazonRenderingContext
        )
    }

    @Benchmark
    override fun benchmarkWorkflowUsingVariables(state: WorkflowUsingVariablesState) {
        traversor.traverse(
            amazonContextVisitor,
            state.workflowUsingVariables,
            amazonRenderingContext
        )
    }

    @Benchmark
    override fun benchmarkWorkflowWithBinaryConditions(state: WorkflowWithBinaryConditionsState) {
        traversor.traverse(
            amazonContextVisitor,
            state.workflowWithBinaryConditions,
            amazonRenderingContext
        )
    }

    @Benchmark
    override fun benchmarkWorkflowWithMultipleDecisions(state: WorkflowWithMultipleDecisionsState) {
        traversor.traverse(
            amazonContextVisitor,
            state.workflowWithMultipleDecisions,
            amazonRenderingContext
        )
    }

    @Benchmark
    override fun benchmarkWorkflowWithIterationsWithRange(state: WorkflowWithIterationsWithRangeState) {
        traversor.traverse(
            amazonContextVisitor,
            state.workflowWithIterationsWithRange,
            amazonRenderingContext
        )
    }

    @Benchmark
    override fun benchmarkWorkflowWithIterationsWithForEach(state: WorkflowWithIterationsWithForEachState) {
        traversor.traverse(
            amazonContextVisitor,
            state.workflowWithIterationsWithForEach,
            amazonRenderingContext
        )
    }

    @Benchmark
    override fun benchmarkWorkflowWithParallelOneBranch(state: WorkflowWithParallelOneBranchState) {
        traversor.traverse(
            amazonContextVisitor,
            state.workflowWithParallelOneBranch,
            amazonRenderingContext
        )
    }

    @Benchmark
    override fun benchmarkWorkflowWithParallelMultipleBranches(state: WorkflowWithParallelMultipleBranchesState) {
        traversor.traverse(
            amazonContextVisitor,
            state.workflowWithParallelMultipleBranches,
            amazonRenderingContext
        )
    }

    @Benchmark
    override fun benchmarkWorkflowWithParallelIterationWithRange(state: WorkflowWithParallelIterationWithRangeState) {
        traversor.traverse(
            amazonContextVisitor,
            state.withParallelIterationWithRange,
            amazonRenderingContext
        )
    }

    @Benchmark
    override fun benchmarkWorkflowWithParallelIterationForEach(state: WorkflowWithParallelIterationForEachState) {
        traversor.traverse(
            amazonContextVisitor,
            state.withParallelIterationWithForEach,
            amazonRenderingContext
        )
    }
}