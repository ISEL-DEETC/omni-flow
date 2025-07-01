package costaber.com.github.omniflow.cloud.provider.google.strategy

import costaber.com.github.omniflow.cloud.provider.google.renderer.GoogleNotEqualToExpressionRenderer
import costaber.com.github.omniflow.cloud.provider.google.renderer.GoogleTermResolver
import costaber.com.github.omniflow.factory.NodeRendererStrategyFactory
import costaber.com.github.omniflow.model.Node
import costaber.com.github.omniflow.model.NotEqualToExpression
import costaber.com.github.omniflow.predicate.DefaultPredicate
import costaber.com.github.omniflow.renderer.NodeRenderer
import java.util.function.Predicate

class GoogleNotEqualToExpressionStrategyFactory : NodeRendererStrategyFactory<String> {

    override fun getMatcher(): Predicate<Node> =
        DefaultPredicate(NotEqualToExpression::class)

    override fun getRenderer(node: Node): NodeRenderer<String> =
        GoogleNotEqualToExpressionRenderer(node as NotEqualToExpression<*>, GoogleTermResolver)
}