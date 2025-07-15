package costaber.com.github.omniflow.factory

import costaber.com.github.omniflow.model.Node
import costaber.com.github.omniflow.renderer.NodeRenderer
import kotlin.jvm.optionals.getOrNull

class DefaultNodeRendererStrategyDecider internal constructor(
    private val rendererStrategyFactories: List<NodeRendererStrategyFactory<*>>,
) : NodeRendererStrategyDecider {

    override fun decideRenderer(node: Node): NodeRenderer<*> {
        return rendererStrategyFactories.stream()
            .filter { it.getMatcher().test(node) }
            .findFirst()
            .map { it.getRenderer(node) }
            .getOrNull() ?: throw NoSuchElementException("No renderer found for node: $node")
    }

    class Builder {
        private val factories: MutableList<NodeRendererStrategyFactory<*>> = mutableListOf()

        fun addRendererStrategy(
            elementRendererStrategyFactory: NodeRendererStrategyFactory<*>
        ): Builder = apply { factories.add(elementRendererStrategyFactory) }

        fun build() = DefaultNodeRendererStrategyDecider(factories)
    }
}