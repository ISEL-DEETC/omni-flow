package costaber.com.github.omniflow.cloud.provider.amazon.traversor

import costaber.com.github.omniflow.dsl.assign
import costaber.com.github.omniflow.dsl.condition
import costaber.com.github.omniflow.dsl.step
import costaber.com.github.omniflow.dsl.switch
import costaber.com.github.omniflow.dsl.variable
import costaber.com.github.omniflow.model.BranchContext
import costaber.com.github.omniflow.model.IterationForEachContext
import costaber.com.github.omniflow.model.IterationRangeContext
import costaber.com.github.omniflow.model.Node
import costaber.com.github.omniflow.model.ParallelBranchContext
import costaber.com.github.omniflow.model.ParallelIterationContext
import costaber.com.github.omniflow.model.Step
import costaber.com.github.omniflow.model.Value
import costaber.com.github.omniflow.model.Variable
import costaber.com.github.omniflow.traversor.DepthFirstNodeVisitorTraversor
import costaber.com.github.omniflow.visitor.ContextVisitor
import kotlin.collections.plus

class AmazonTraversor: DepthFirstNodeVisitorTraversor() {

    override fun <K, R> traverseNode(
        visitor: ContextVisitor<Node, K, R>,
        node: Node,
        context: K,
        visitResults: MutableList<R>,
    ) {
        val newNode = when(node) {
            is Step -> when(node.context) {
                is IterationRangeContext -> node.copy(context = node.context.childNodes(node.name))
                is IterationForEachContext -> node.copy(context = node.context.childNodes(node.name))
                is ParallelIterationContext -> when(node.context.iterationContext) {
                    is IterationRangeContext -> node.copy(context = node.context.toParallelBranchContext(node.name))
                    else -> node
                }
                else -> node

            }

            else -> node
        }
        super.traverseNode(visitor, newNode, context, visitResults)
    }

}

private fun ParallelIterationContext.toParallelBranchContext(prefix: String): ParallelBranchContext = ParallelBranchContext(
    when (this.iterationContext) {
        is IterationRangeContext -> (this.iterationContext.range.min..this.iterationContext.range.max).map {
            BranchContext(
                prefix, null, listOf(
                    step {
                        name("${prefix}InitializeCounter")
                        description("Auto generated")
                        context(
                            assign {
                                variable("${this@toParallelBranchContext.iterationContext.value}.$" equal it)
                            }
                        )
                    }.build()
                ).plus(this@toParallelBranchContext.iterationContext.steps)
            )
        }
        else -> listOf(
            BranchContext(prefix, null, this.iterationContext.steps),
        )
    }

)


private fun IterationForEachContext.childNodes(prefix: String): IterationForEachContext = this.copy(
    steps=listOf(
        step {
            name("${prefix}InitializeCounter")
            description("Auto generated")
            context(
                assign {
                    variable("Index.$" equal -1)
                    variable("ArraySize.$" equal variable("States.ArraySize($.${forEachVariable.name})"))
                }
            )
        },
        step {
            name("${prefix}IncrementCounter")
            description("Auto generated")
            context(
                assign {
                    variable("Index.$" equal "States.MathAdd($.Index, 1)")
                    variable("${value}.$" equal "States.ArrayGet($.${forEachVariable.name}, $.Index)")
                }
            )
        }).map { it.build() }
        .plus(steps)
        .plus(listOf(
            step {
                name("${prefix}Loop?")
                description("Auto generated")
                context(
                    switch {
                        conditions(
                            condition {
                                match(variable("Index") lessThan variable("ArraySize"))
                                jump("${prefix}IncrementCounter")
                            },
                        )
                        default("${prefix}EndLoop")
                    }
                )
            },
            step {
                name("${prefix}EndLoop")
                description("Auto generated")
                context(
                    assign {  }
                )
            }).map { it.build() })
)


private fun IterationRangeContext.childNodes(prefix: String): IterationRangeContext = this.copy(
    steps=listOf(
        step {
            name("${prefix}InitializeCounter")
            description("Auto generated")
            context(
                assign {
                    variable("${value}.$" equal (range.min - 1))
                }
            )
        },
        step {
            name("${prefix}IncrementCounter")
            description("Auto generated")
            context(
                assign {
                   variable( "${value}.$" equal "States.MathAdd($.${value}, 1)")
                }
            )
        }).map { it.build() }
        .plus(steps)
        .plus(listOf(
            step {
                name("${prefix}Loop?")
                description("Auto generated")
                context(
                    switch {
                        conditions(
                            condition {
                                match(variable(value) lessThan Value(range.max))
                                jump("${prefix}IncrementCounter")
                            },
                        )
                        default("${prefix}EndLoop")
                    }
                )
            },
            step {
                name("${prefix}EndLoop")
                description("Auto generated")
                context(
                    assign {  }
                )
            }).map { it.build() })

)
