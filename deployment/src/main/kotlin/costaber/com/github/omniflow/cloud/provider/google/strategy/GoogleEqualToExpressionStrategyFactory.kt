package costaber.com.github.omniflow.cloud.provider.google.strategy

import costaber.com.github.omniflow.cloud.provider.google.renderer.GoogleEqualToExpressionRenderer
import costaber.com.github.omniflow.cloud.provider.google.renderer.GoogleTermResolver
import costaber.com.github.omniflow.factory.NodeRendererStrategyFactory
import costaber.com.github.omniflow.model.EqualToExpression
import costaber.com.github.omniflow.model.Node
import costaber.com.github.omniflow.predicate.DefaultPredicate
import costaber.com.github.omniflow.renderer.NodeRenderer
import java.util.function.Predicate

class GoogleEqualToExpressionStrategyFactory : NodeRendererStrategyFactory<String> {

    override fun getMatcher(): Predicate<Node> =
        DefaultPredicate(EqualToExpression::class)

    override fun getRenderer(node: Node): NodeRenderer<String> =
        GoogleEqualToExpressionRenderer(node as EqualToExpression<*>, GoogleTermResolver)
}