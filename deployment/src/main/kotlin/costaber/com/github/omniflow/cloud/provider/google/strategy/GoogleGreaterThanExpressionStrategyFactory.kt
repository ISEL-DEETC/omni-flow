package costaber.com.github.omniflow.cloud.provider.google.strategy

import costaber.com.github.omniflow.cloud.provider.google.renderer.GoogleGreaterThanExpressionRenderer
import costaber.com.github.omniflow.cloud.provider.google.renderer.GoogleTermResolver
import costaber.com.github.omniflow.factory.NodeRendererStrategyFactory
import costaber.com.github.omniflow.model.GreaterThanExpression
import costaber.com.github.omniflow.model.Node
import costaber.com.github.omniflow.predicate.DefaultPredicate
import costaber.com.github.omniflow.renderer.NodeRenderer
import java.util.function.Predicate

class GoogleGreaterThanExpressionStrategyFactory : NodeRendererStrategyFactory<String> {

    override fun getMatcher(): Predicate<Node> =
        DefaultPredicate(GreaterThanExpression::class)

    override fun getRenderer(node: Node): NodeRenderer<String> =
        GoogleGreaterThanExpressionRenderer(node as GreaterThanExpression<*>, GoogleTermResolver)
}