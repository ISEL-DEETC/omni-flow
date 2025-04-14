package costaber.com.github.omniflow.cloud.provider.google.deployer

import costaber.com.github.omniflow.cloud.provider.google.renderer.GoogleBranchRenderer
import costaber.com.github.omniflow.cloud.provider.google.renderer.GoogleTermContext
import costaber.com.github.omniflow.cloud.provider.google.service.GoogleWorkflowService
import costaber.com.github.omniflow.cloud.provider.google.strategy.*
import costaber.com.github.omniflow.deployer.CloudDeployer
import costaber.com.github.omniflow.factory.DefaultNodeRendererStrategyDecider
import costaber.com.github.omniflow.factory.NodeRendererStrategyDecider
import costaber.com.github.omniflow.model.Workflow
import costaber.com.github.omniflow.renderer.IndentedRenderingContext
import costaber.com.github.omniflow.resource.util.joinToStringNewLines
import costaber.com.github.omniflow.traversor.DepthFirstNodeVisitorTraversor
import costaber.com.github.omniflow.visitor.NodeContextVisitor
import mu.KotlinLogging

class GoogleCloudDeployer internal constructor(
    private val nodeTraversor: DepthFirstNodeVisitorTraversor,
    private val contextVisitor: NodeContextVisitor,
    private val googleWorkflowService: GoogleWorkflowService,
) : CloudDeployer<GoogleDeployContext> {

    private companion object {
        private val logger = KotlinLogging.logger { }
    }

    override fun deploy(workflow: Workflow, deployContext: GoogleDeployContext) {
        logger.info { "Starting to convert Workflow into a Workflow" }
        val renderingContext = IndentedRenderingContext(termContext = GoogleTermContext())
        val content = nodeTraversor.traverse(contextVisitor, workflow, renderingContext)
            .filterNot(String::isEmpty)
            .joinToStringNewLines()
        googleWorkflowService.deploy(
            projectId = deployContext.projectId,
            zone = deployContext.zone,
            serviceAccount = deployContext.serviceAccount,
            workflowId = deployContext.workflowId,
            workflowDescription = deployContext.workflowDescription,
            workflowLabels = deployContext.workflowLabels,
            workflowSourceContents = content,
        )
    }

    class Builder {
        fun build() = GoogleCloudDeployer(
            nodeTraversor = DepthFirstNodeVisitorTraversor(),
            contextVisitor = NodeContextVisitor(createNodeRendererStrategyDecider()),
            googleWorkflowService = GoogleWorkflowService(),
        )

        fun createNodeRendererStrategyDecider(): NodeRendererStrategyDecider {
            return DefaultNodeRendererStrategyDecider.Builder()
                .addRendererStrategy(GoogleAssignStrategyFactory())
                .addRendererStrategy(GoogleCallStrategyFactory())
                .addRendererStrategy(GoogleConditionStrategyFactory())
                .addRendererStrategy(GoogleEqualToExpressionStrategyFactory())
                .addRendererStrategy(GoogleGreaterThanExpressionStrategyFactory())
                .addRendererStrategy(GoogleGreaterThanOrEqualExpressionStrategyFactory())
                .addRendererStrategy(GoogleLessThanExpressionStrategyFactory())
                .addRendererStrategy(GoogleLessThanOrEqualExpressionStrategyFactory())
                .addRendererStrategy(GoogleNotEqualToExpressionStrategyFactory())
                .addRendererStrategy(GoogleStepStrategyFactory())
                .addRendererStrategy(GoogleSwitchStrategyFactory())
                .addRendererStrategy(GoogleVariableStrategyFactory())
                .addRendererStrategy(GoogleWorkflowStrategyFactory())
                .addRendererStrategy(GoogleParallelStrategyFactory())
                .addRendererStrategy(GoogleBranchStrategyFactory())
                .addRendererStrategy(GoogleIterationStrategyFactory())
                .build()
        }
    }
}