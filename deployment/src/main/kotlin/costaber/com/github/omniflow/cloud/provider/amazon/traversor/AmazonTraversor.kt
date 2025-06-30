package costaber.com.github.omniflow.cloud.provider.amazon.traversor

import costaber.com.github.omniflow.dsl.assign
import costaber.com.github.omniflow.dsl.condition
import costaber.com.github.omniflow.dsl.step
import costaber.com.github.omniflow.dsl.switch
import costaber.com.github.omniflow.dsl.value
import costaber.com.github.omniflow.dsl.variable
import costaber.com.github.omniflow.model.BranchContext
import costaber.com.github.omniflow.model.IterationForEachContext
import costaber.com.github.omniflow.model.IterationRangeContext
import costaber.com.github.omniflow.model.Node
import costaber.com.github.omniflow.model.ParallelBranchContext
import costaber.com.github.omniflow.model.ParallelIterationContext
import costaber.com.github.omniflow.model.Step
import costaber.com.github.omniflow.model.Value
import costaber.com.github.omniflow.traversor.DepthFirstNodeVisitorTraversor
import costaber.com.github.omniflow.visitor.ContextVisitor
import kotlin.collections.plus

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
                    is IterationRangeContext -> node.copy(context = toParallelBranchContext(
                        node.name,
                        node.context.iterationContext
                    ))
                    else -> node
                }

                else -> node

            }

            else -> node
        }
        super.traverseNode(visitor, newNode, context, visitResults)
    }

}

private fun toParallelBranchContext(prefix: String, iterationRangeContext: IterationRangeContext): ParallelBranchContext =
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
