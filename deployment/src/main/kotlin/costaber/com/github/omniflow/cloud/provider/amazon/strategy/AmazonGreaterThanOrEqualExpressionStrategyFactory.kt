package costaber.com.github.omniflow.cloud.provider.amazon.strategy

import costaber.com.github.omniflow.cloud.provider.amazon.renderer.binaryexpression.AmazonGreaterThanOrEqualExpressionRenderer
import costaber.com.github.omniflow.cloud.provider.amazon.renderer.AmazonTermResolver
import costaber.com.github.omniflow.factory.NodeRendererStrategyFactory
import costaber.com.github.omniflow.model.GreaterThanOrEqualExpression
import costaber.com.github.omniflow.model.Node
import costaber.com.github.omniflow.predicate.DefaultPredicate
import costaber.com.github.omniflow.renderer.NodeRenderer
import java.util.function.Predicate

class AmazonGreaterThanOrEqualExpressionStrategyFactory : NodeRendererStrategyFactory<String> {

    override fun getMatcher(): Predicate<Node> =
        DefaultPredicate(GreaterThanOrEqualExpression::class)

    override fun getRenderer(node: Node): NodeRenderer<String> =
        AmazonGreaterThanOrEqualExpressionRenderer(node as GreaterThanOrEqualExpression<*>, AmazonTermResolver)
}