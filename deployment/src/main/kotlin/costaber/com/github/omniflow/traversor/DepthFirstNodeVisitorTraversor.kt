package costaber.com.github.omniflow.traversor

import costaber.com.github.omniflow.model.Node
import costaber.com.github.omniflow.visitor.ContextVisitor

open class DepthFirstNodeVisitorTraversor : NodeVisitorTraversor {

    override fun <K, R> traverse(
        visitor: ContextVisitor<Node, K, R>,
        root: Node,
        context: K
    ): List<R> {
        val result = mutableListOf<R>()

        traverseNode(visitor, root, context, result)

        return result
    }

    protected open fun <K, R> traverseNode(
        visitor: ContextVisitor<Node, K, R>,
        node: Node,
        context: K,
        visitResults: MutableList<R>,
    ) {
        val beginVisitResult = visitor.beginVisit(node, context)
        visitResults.add(beginVisitResult)

        node.childNodes().forEach {
            traverseNode(visitor, it, context, visitResults)
        }

        val endVisitResult = visitor.endVisit(node, context)
        visitResults.add(endVisitResult)
    }
}