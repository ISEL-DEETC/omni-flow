package costaber.com.github.omniflow.cloud.provider.amazon.traversor

import costaber.com.github.omniflow.dsl.*
import costaber.com.github.omniflow.model.*
import costaber.com.github.omniflow.traversor.DepthFirstNodeVisitorTraversor
import costaber.com.github.omniflow.visitor.ContextVisitor

class AmazonTraversor : DepthFirstNodeVisitorTraversor() {

    override fun <K, R> traverseNode(
        visitor: ContextVisitor<Node, K, R>,
        node: Node,
        context: K,
        visitResults: MutableList<R>,
    ) {
        val newNode = when (node) {
            is Step -> when (node.context) {
                is IterationRangeContext -> node.copy(context = node.context.childNodes(node.name))
                is IterationForEachContext -> node.copy(context = node.context.childNodes(node.name))
                is ParallelIterationContext -> when (node.context.iterationContext) {
                    is IterationRangeContext -> node.copy(
                        context = toParallelBranchContext(
                            node.name,
                            node.context.iterationContext
                        )
                    )

                    else -> node
                }

                else -> node

            }

            else -> node
        }
        super.traverseNode(visitor, newNode, context, visitResults)
    }

}

private fun toParallelBranchContext(
    prefix: String,
    iterationRangeContext: IterationRangeContext
): ParallelBranchContext =
    ParallelBranchContext(
        (iterationRangeContext.range.min..iterationRangeContext.range.max).map {
            BranchContext(
                prefix, null, listOf(
                    step {
                        name("${prefix}InitializeCounter")
                        description("Auto generated")
                        context(
                            assign {
                                variables(
                                    variable("${iterationRangeContext.value}.$") equalTo value(
                                        it
                                    )
                                )
                            }
                        )
                    }.build()
                ).plus(iterationRangeContext.steps)
            )
        }
    )


private fun IterationForEachContext.childNodes(prefix: String): IterationForEachContext = this.copy(
    steps = listOf(
        step {
            name("${prefix}InitializeCounter")
            description("Auto generated")
            context(
                assign {
                    variables(
                        variable("Index.$") equalTo value(-1),
                        variable("ArraySize.$") equalTo variable("States.ArraySize($.${forEachVariable.name})")
                    )
                }
            )
        },
        step {
            name("${prefix}IncrementCounter")
            description("Auto generated")
            context(
                assign {
                    variables(
                        variable("Index.$") equalTo value("States.MathAdd($.Index, 1)"),
                        variable("${value}.$") equalTo value("States.ArrayGet($.${forEachVariable.name}, $.Index)")
                    )
                }
            )
        }).map { it.build() }
        .plus(steps)
        .plus(
            listOf(
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
                        assign { }
                    )
                }).map { it.build() })
)


private fun IterationRangeContext.childNodes(prefix: String): IterationRangeContext = this.copy(
    steps = listOf(
        step {
            name("${prefix}InitializeCounter")
            description("Auto generated")
            context(
                assign {
                    variables(variable("${value}.$") equalTo Value(range.min - 1))
                }
            )
        },
        step {
            name("${prefix}IncrementCounter")
            description("Auto generated")
            context(
                assign {
                    variables(variable("${value}.$") equalTo value("States.MathAdd($.${value}, 1)"))
                }
            )
        }).map { it.build() }
        .plus(steps)
        .plus(
            listOf(
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
                        assign { }
                    )
                }).map { it.build() })

)
