package costaber.com.github.omniflow.cloud.provider.google.strategy

import costaber.com.github.omniflow.cloud.provider.google.renderer.GoogleGreaterThanOrEqualExpressionRenderer
import costaber.com.github.omniflow.cloud.provider.google.renderer.GoogleTermResolver
import costaber.com.github.omniflow.factory.NodeRendererStrategyFactory
import costaber.com.github.omniflow.model.GreaterThanOrEqualExpression
import costaber.com.github.omniflow.model.Node
import costaber.com.github.omniflow.predicate.DefaultPredicate
import costaber.com.github.omniflow.renderer.NodeRenderer
import java.util.function.Predicate

class GoogleGreaterThanOrEqualExpressionStrategyFactory : NodeRendererStrategyFactory<String> {

    override fun getMatcher(): Predicate<Node> =
        DefaultPredicate(GreaterThanOrEqualExpression::class)

    override fun getRenderer(node: Node): NodeRenderer<String> =
        GoogleGreaterThanOrEqualExpressionRenderer(node as GreaterThanOrEqualExpression<*>, GoogleTermResolver)
}