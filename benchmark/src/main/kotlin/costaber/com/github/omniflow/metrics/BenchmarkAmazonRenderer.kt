package costaber.com.github.omniflow.metrics

import costaber.com.github.omniflow.cloud.provider.amazon.renderer.AmazonRenderingContext
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
        traversor = DepthFirstNodeVisitorTraversor()
        amazonContextVisitor = NodeContextVisitor(StrategyDeciderProvider.amazonNodeRendererStrategyDecider())
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
}