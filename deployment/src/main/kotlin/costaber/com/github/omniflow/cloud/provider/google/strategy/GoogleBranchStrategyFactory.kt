package costaber.com.github.omniflow.cloud.provider.google.strategy

import costaber.com.github.omniflow.cloud.provider.google.renderer.GoogleBranchRenderer
import costaber.com.github.omniflow.cloud.provider.google.renderer.GoogleParallelRenderer
import costaber.com.github.omniflow.cloud.provider.google.renderer.GoogleTermResolver
import costaber.com.github.omniflow.cloud.provider.google.renderer.GoogleWorkflowRenderer
import costaber.com.github.omniflow.factory.NodeRendererStrategyFactory
import costaber.com.github.omniflow.model.BranchContext
import costaber.com.github.omniflow.model.Node
import costaber.com.github.omniflow.model.ParallelContext
import costaber.com.github.omniflow.model.Workflow
import costaber.com.github.omniflow.predicate.DefaultPredicate
import costaber.com.github.omniflow.renderer.NodeRenderer
import java.util.function.Predicate

class GoogleBranchStrategyFactory : NodeRendererStrategyFactory<String> {

    override fun getMatcher(): Predicate<Node> =
        DefaultPredicate(BranchContext::class)

    override fun getRenderer(node: Node): NodeRenderer<String> =
        GoogleBranchRenderer(node as BranchContext)
}