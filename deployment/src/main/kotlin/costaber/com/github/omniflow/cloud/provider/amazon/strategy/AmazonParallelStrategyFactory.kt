package costaber.com.github.omniflow.cloud.provider.amazon.strategy

import costaber.com.github.omniflow.cloud.provider.amazon.renderer.AmazonParallelIterationRenderer
import costaber.com.github.omniflow.cloud.provider.amazon.renderer.AmazonParallelRenderer
import costaber.com.github.omniflow.factory.NodeRendererStrategyFactory
import costaber.com.github.omniflow.model.Node
import costaber.com.github.omniflow.model.ParallelBranchContext
import costaber.com.github.omniflow.model.ParallelContext
import costaber.com.github.omniflow.model.ParallelIterationContext
import costaber.com.github.omniflow.predicate.DefaultPredicate
import costaber.com.github.omniflow.renderer.NodeRenderer
import java.util.function.Predicate

class AmazonParallelStrategyFactory : NodeRendererStrategyFactory<String> {

    override fun getMatcher(): Predicate<Node> =
        DefaultPredicate(ParallelContext::class)

    override fun getRenderer(node: Node): NodeRenderer<String> = when (node) {
        is ParallelBranchContext -> AmazonParallelRenderer(node)
        is ParallelIterationContext -> AmazonParallelIterationRenderer(node)
        else -> throw UnsupportedOperationException("ParallelBranchContext $node is not supported.")
    }

}