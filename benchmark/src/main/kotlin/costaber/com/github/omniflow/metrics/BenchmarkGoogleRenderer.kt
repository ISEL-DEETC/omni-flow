package costaber.com.github.omniflow.metrics

import costaber.com.github.omniflow.cloud.provider.google.provider.GoogleDefaultStrategyDeciderProvider.createNodeRendererStrategyDecider
import costaber.com.github.omniflow.cloud.provider.google.renderer.GoogleRenderingContext
import costaber.com.github.omniflow.cloud.provider.google.renderer.GoogleTermContext
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
        System.gc()
        traversor = DepthFirstNodeVisitorTraversor()
        googleContextVisitor = NodeContextVisitor(createNodeRendererStrategyDecider())
        googleRenderingContext = GoogleRenderingContext(0, StringBuilder(), GoogleTermContext())
    }

    @Benchmark
    override fun benchmarkWorkflowWithIndependentSteps(state: WorkflowWithIndependentStepsState) {
        traversor.traverse(
            googleContextVisitor,
            state.workflowWithIndependentSteps,
            googleRenderingContext
        )
    }

    @Benchmark
    override fun benchmarkWorkflowUsingVariables(state: WorkflowUsingVariablesState) {
        traversor.traverse(
            googleContextVisitor,
            state.workflowUsingVariables,
            googleRenderingContext
        )
    }

    @Benchmark
    override fun benchmarkWorkflowWithBinaryConditions(state: WorkflowWithBinaryConditionsState) {
        traversor.traverse(
            googleContextVisitor,
            state.workflowWithBinaryConditions,
            googleRenderingContext
        )
    }

    @Benchmark
    override fun benchmarkWorkflowWithMultipleDecisions(state: WorkflowWithMultipleDecisionsState) {
        traversor.traverse(
            googleContextVisitor,
            state.workflowWithMultipleDecisions,
            googleRenderingContext
        )
    }

    @Benchmark
    override fun benchmarkWorkflowWithIterationsWithRange(state: WorkflowWithIterationsWithRangeState) {
        traversor.traverse(
            googleContextVisitor,
            state.workflowWithIterationsWithRange,
            googleRenderingContext
        )
    }

    @Benchmark
    override fun benchmarkWorkflowWithIterationsWithForEach(state: WorkflowWithIterationsWithForEachState) {
        traversor.traverse(
            googleContextVisitor,
            state.workflowWithIterationsWithForEach,
            googleRenderingContext
        )
    }

    @Benchmark
    override fun benchmarkWorkflowWithParallelOneBranch(state: WorkflowWithParallelOneBranchState) {
        traversor.traverse(
            googleContextVisitor,
            state.workflowWithParallelOneBranch,
            googleRenderingContext
        )
    }

    @Benchmark
    override fun benchmarkWorkflowWithParallelMultipleBranches(state: WorkflowWithParallelMultipleBranchesState) {
        traversor.traverse(
            googleContextVisitor,
            state.workflowWithParallelMultipleBranches,
            googleRenderingContext
        )
    }

    @Benchmark
    override fun benchmarkWorkflowWithParallelIterationWithRange(state: WorkflowWithParallelIterationWithRangeState) {
        traversor.traverse(
            googleContextVisitor,
            state.withParallelIterationWithRange,
            googleRenderingContext
        )
    }

    @Benchmark
    override fun benchmarkWorkflowWithParallelIterationForEach(state: WorkflowWithParallelIterationForEachState) {
        traversor.traverse(
            googleContextVisitor,
            state.withParallelIterationWithForEach,
            googleRenderingContext
        )
    }
}