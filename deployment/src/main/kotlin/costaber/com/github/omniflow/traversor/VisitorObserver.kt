package costaber.com.github.omniflow.traversor

import costaber.com.github.omniflow.model.Node
import costaber.com.github.omniflow.visitor.ContextVisitor

class VisitorObserver : NodeVisitorTraversorObserver {

    private val visitedNodes = mutableListOf<String>()
    private val visitedContexts = mutableListOf<String>()

    override fun <K, R> onBeginVisit(
        visitor: ContextVisitor<Node, K, R>,
        node: Node,
        context: K,
        visitResults: MutableList<R>
    ) {
        visitedNodes.add("Begin: $node")
        visitedContexts.add("Begin: ${context.toString()}")

    }

    override fun <K, R> onEndVisit(
        visitor: ContextVisitor<Node, K, R>,
        node: Node,
        context: K,
        visitResults: MutableList<R>
    ) {
        visitedNodes.add("End: $node")
        visitedContexts.add("End: ${context.toString()}")
    }

    override fun close() {
        visitedNodes.zip(visitedContexts).forEach { (node, context) ->
            println(node)
            println(context)
        }
    }
}