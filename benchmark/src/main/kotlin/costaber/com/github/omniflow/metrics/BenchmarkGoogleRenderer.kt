package costaber.com.github.omniflow.metrics

import costaber.com.github.omniflow.cloud.provider.google.renderer.GoogleRenderingContext
import costaber.com.github.omniflow.cloud.provider.google.renderer.GoogleTermContext
import costaber.com.github.omniflow.generator.WorkflowGenerator.usingVariables
import costaber.com.github.omniflow.generator.WorkflowGenerator.withBinaryConditions
import costaber.com.github.omniflow.generator.WorkflowGenerator.withIndependentSteps
import costaber.com.github.omniflow.generator.WorkflowGenerator.withMultipleDecisions
import costaber.com.github.omniflow.provider.StrategyDeciderProvider
import costaber.com.github.omniflow.renderer.IndentedRenderingContext
import costaber.com.github.omniflow.renderer.RenderingContext
import costaber.com.github.omniflow.traversor.DepthFirstNodeVisitorTraversor
import costaber.com.github.omniflow.visitor.NodeContextVisitor
import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.Setup

open class BenchmarkGoogleRenderer : BenchmarkWorkflowRenderer() {
    private var traversor: DepthFirstNodeVisitorTraversor? = null
    private var googleContextVisitor: NodeContextVisitor? = null
    private var googleRenderingContext: IndentedRenderingContext? = null

    @Setup
    fun setup() {
        workflowWithIndependentSteps = withIndependentSteps(numberOfSteps)
        workflowUsingVariables = usingVariables(numberOfSteps)
        workflowWithBinaryConditions = withBinaryConditions(numberOfSteps)
        workflowWithMultipleDecisions = withMultipleDecisions(numberOfSteps)
        traversor = DepthFirstNodeVisitorTraversor()
        googleContextVisitor = NodeContextVisitor(StrategyDeciderProvider.googleNodeRendererStrategyDecider())
        googleRenderingContext = GoogleRenderingContext(0, StringBuilder(), GoogleTermContext())
    }

    @Benchmark
    override fun benchmarkWorkflowWithIndependentSteps() {
        traversor!!.traverse<RenderingContext, String>(
            googleContextVisitor!!,
            workflowWithIndependentSteps,
            googleRenderingContext!!
        )
    }

    @Benchmark
    override fun benchmarkWorkflowUsingVariables() {
        traversor!!.traverse<RenderingContext, String>(
            googleContextVisitor!!,
            workflowUsingVariables,
            googleRenderingContext!!
        )
    }

    @Benchmark
    override fun benchmarkWorkflowWithBinaryConditions() {
        traversor!!.traverse<RenderingContext, String>(
            googleContextVisitor!!,
            workflowWithBinaryConditions,
            googleRenderingContext!!
        )
    }

    @Benchmark
    override fun benchmarkWorkflowWithMultipleDecisions() {
        traversor!!.traverse<RenderingContext, String>(
            googleContextVisitor!!,
            workflowWithMultipleDecisions,
            googleRenderingContext!!
        )
    }
}