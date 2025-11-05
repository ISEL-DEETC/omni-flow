package costaber.com.github.omniflow.traversor

import costaber.com.github.omniflow.model.Node
import costaber.com.github.omniflow.visitor.ContextVisitor

interface NodeVisitorTraversorObserver {
    fun <K, R> onBeginVisit(visitor: ContextVisitor<Node, K, R>, node: Node, context: K, visitResults: MutableList<R>)
    fun <K, R> onEndVisit(visitor: ContextVisitor<Node, K, R>, node: Node, context: K, visitResults: MutableList<R>)
    fun close()
}