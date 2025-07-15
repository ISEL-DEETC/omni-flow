package costaber.com.github.omniflow.cloud.provider.amazon.deployer

import costaber.com.github.omniflow.cloud.provider.amazon.renderer.AmazonRenderingContext
import costaber.com.github.omniflow.cloud.provider.amazon.service.AmazonStateMachineService
import costaber.com.github.omniflow.cloud.provider.amazon.strategy.*
import costaber.com.github.omniflow.cloud.provider.google.provider.GoogleDefaultStrategyDeciderProvider.createNodeRendererStrategyDecider
import costaber.com.github.omniflow.deployer.CloudDeployer
import costaber.com.github.omniflow.factory.DefaultNodeRendererStrategyDecider
import costaber.com.github.omniflow.factory.NodeRendererStrategyDecider
import costaber.com.github.omniflow.model.Workflow
import costaber.com.github.omniflow.resource.util.joinToStringNewLines
import costaber.com.github.omniflow.traversor.DepthFirstNodeVisitorTraversor
import costaber.com.github.omniflow.visitor.NodeContextVisitor
import mu.KotlinLogging

class AmazonCloudDeployer internal constructor(
    private val nodeTraversor: DepthFirstNodeVisitorTraversor,
    private val contextVisitor: NodeContextVisitor,
    private val amazonStateMachineService: AmazonStateMachineService,
) : CloudDeployer<AmazonDeployContext> {

    private companion object {
        private val logger = KotlinLogging.logger { }
    }

    override fun deploy(workflow: Workflow, deployContext: AmazonDeployContext) {
        logger.info { "Starting to convert Workflow into a State Machine" }
        val content = nodeTraversor.traverse(contextVisitor, workflow, AmazonRenderingContext())
            .filterNot(String::isEmpty)
            .joinToStringNewLines()
        amazonStateMachineService.createStateMachine(
            roleArn = deployContext.roleArn,
            region = deployContext.region,
            tags = deployContext.tags,
            stateMachineName = deployContext.stateMachineName,
            stateMachineDefinition = content,
        )
    }

    class Builder {
        fun build() = AmazonCloudDeployer(
            nodeTraversor = DepthFirstNodeVisitorTraversor(),
            contextVisitor = NodeContextVisitor(createNodeRendererStrategyDecider()),
            amazonStateMachineService = AmazonStateMachineService()
        )
    }
}