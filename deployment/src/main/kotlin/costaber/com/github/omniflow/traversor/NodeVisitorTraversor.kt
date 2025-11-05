package costaber.com.github.omniflow.traversor

import costaber.com.github.omniflow.model.Node
import costaber.com.github.omniflow.visitor.ContextVisitor

/**
 * A generic interface for traversing nodes
 */
interface NodeVisitorTraversor {
    /**
     * Traverses the root node making use
     * of the given visitor using the given context
     *
     * @param visitor the visitor to use while traversing
     * @param root    the node to traverse
     * @param context the context to use while visiting
     * @param <K>     the context type </K>
     **/
    fun <K, R> traverse(
        visitor: ContextVisitor<Node, K, R>,
        root: Node, context: K
    ): List<R>

    fun registerObserver(observer: NodeVisitorTraversorObserver): NodeVisitorTraversor
}