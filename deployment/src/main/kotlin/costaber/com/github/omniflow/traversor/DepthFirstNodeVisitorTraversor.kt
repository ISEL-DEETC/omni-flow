package costaber.com.github.omniflow.traversor

import costaber.com.github.omniflow.model.Node
import costaber.com.github.omniflow.visitor.ContextVisitor

open class DepthFirstNodeVisitorTraversor : NodeVisitorTraversor {

    protected val observers: MutableList<NodeVisitorTraversorObserver> = mutableListOf()

    override fun <K, R> traverse(
        visitor: ContextVisitor<Node, K, R>,
        root: Node,
        context: K
    ): List<R> {
        val result = mutableListOf<R>()

        traverseNode(visitor, root, context, result)

        return result
    }

    override fun registerObserver(observer: NodeVisitorTraversorObserver): NodeVisitorTraversor {
        observers.add(observer)
        return this
    }

    protected open fun <K, R> traverseNode(
        visitor: ContextVisitor<Node, K, R>,
        node: Node,
        context: K,
        visitResults: MutableList<R>,
    ) {
        try {
            val beginVisitResult = visitor.beginVisit(node, context)
            visitResults.add(beginVisitResult)
            observers.forEach { it.onBeginVisit(visitor, node, context, visitResults) }

            node.childNodes().forEach {
                traverseNode(visitor, it, context, visitResults)
            }

            val endVisitResult = visitor.endVisit(node, context)
            visitResults.add(endVisitResult)
            observers.forEach { it.onEndVisit(visitor, node, context, visitResults) }
        } catch (e: Exception) {
            observers.forEach { it.close() }
            throw e
        }
    }
}