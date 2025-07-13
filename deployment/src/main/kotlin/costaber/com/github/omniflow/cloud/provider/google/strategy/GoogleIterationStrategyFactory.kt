package costaber.com.github.omniflow.cloud.provider.google.strategy

import costaber.com.github.omniflow.cloud.provider.google.renderer.GoogleIterationRenderer
import costaber.com.github.omniflow.factory.NodeRendererStrategyFactory
import costaber.com.github.omniflow.model.IterationContext
import costaber.com.github.omniflow.model.Node
import costaber.com.github.omniflow.predicate.DefaultPredicate
import costaber.com.github.omniflow.renderer.NodeRenderer
import java.util.function.Predicate

class GoogleIterationStrategyFactory : NodeRendererStrategyFactory<String> {

    override fun getMatcher(): Predicate<Node> =
        DefaultPredicate(IterationContext::class)

    override fun getRenderer(node: Node): NodeRenderer<String> =
        GoogleIterationRenderer(node as IterationContext)
}