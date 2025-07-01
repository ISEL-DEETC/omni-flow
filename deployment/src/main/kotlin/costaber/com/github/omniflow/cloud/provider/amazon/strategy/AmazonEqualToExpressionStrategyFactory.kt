package costaber.com.github.omniflow.cloud.provider.amazon.strategy

import costaber.com.github.omniflow.cloud.provider.amazon.renderer.AmazonEqualToExpressionRenderer
import costaber.com.github.omniflow.cloud.provider.amazon.renderer.AmazonTermResolver
import costaber.com.github.omniflow.factory.NodeRendererStrategyFactory
import costaber.com.github.omniflow.model.EqualToExpression
import costaber.com.github.omniflow.model.Node
import costaber.com.github.omniflow.predicate.DefaultPredicate
import costaber.com.github.omniflow.renderer.NodeRenderer
import java.util.function.Predicate

class AmazonEqualToExpressionStrategyFactory : NodeRendererStrategyFactory<String> {

    override fun getMatcher(): Predicate<Node> =
        DefaultPredicate(EqualToExpression::class)

    override fun getRenderer(node: Node): NodeRenderer<String> =
        AmazonEqualToExpressionRenderer(node as EqualToExpression<*>, AmazonTermResolver)
}