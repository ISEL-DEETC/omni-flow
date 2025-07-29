package costaber.com.github.omniflow.cloud.provider.amazon.strategy

import costaber.com.github.omniflow.cloud.provider.amazon.renderer.binaryexpression.AmazonNotEqualToExpressionRenderer
import costaber.com.github.omniflow.cloud.provider.amazon.renderer.AmazonTermResolver
import costaber.com.github.omniflow.factory.NodeRendererStrategyFactory
import costaber.com.github.omniflow.model.Node
import costaber.com.github.omniflow.model.NotEqualToExpression
import costaber.com.github.omniflow.predicate.DefaultPredicate
import costaber.com.github.omniflow.renderer.NodeRenderer
import java.util.function.Predicate

class AmazonNotEqualToExpressionStrategyFactory : NodeRendererStrategyFactory<String> {

    override fun getMatcher(): Predicate<Node> =
        DefaultPredicate(NotEqualToExpression::class)

    override fun getRenderer(node: Node): NodeRenderer<String> =
        AmazonNotEqualToExpressionRenderer(node as NotEqualToExpression<*>, AmazonTermResolver)
}