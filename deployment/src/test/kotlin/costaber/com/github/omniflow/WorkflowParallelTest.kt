package costaber.com.github.omniflow

import costaber.com.github.omniflow.cloud.provider.amazon.provider.AmazonDefaultStrategyDeciderProvider
import costaber.com.github.omniflow.cloud.provider.amazon.renderer.AmazonRenderingContext
import costaber.com.github.omniflow.cloud.provider.google.provider.GoogleDefaultStrategyDeciderProvider
import costaber.com.github.omniflow.cloud.provider.google.renderer.GoogleRenderingContext
import costaber.com.github.omniflow.cloud.provider.google.renderer.GoogleTermContext
import costaber.com.github.omniflow.dsl.*
import costaber.com.github.omniflow.model.HttpMethod.GET
import costaber.com.github.omniflow.resource.util.joinToStringNewLines
import costaber.com.github.omniflow.traversor.DepthFirstNodeVisitorTraversor
import costaber.com.github.omniflow.visitor.NodeContextVisitor
import java.util.*
import kotlin.test.Test

internal class WorkflowParallelTest {

    private val generalWorkflow = workflow {
        name("calculatorWorkflow")
        description("Calculator example")
        steps(
            step {
                name("ListAssign")
                description("Initialize variables")
                context(
                    assign {
                        variables(
                            variable("listString") equalTo value(listOf("a", "b")),
                            variable("listNumber") equalTo value(listOf(1, 2)),
                            variable("c") equalTo value("true")
                        )

                    }
                )
            },
            step {
                name("Parallel")
                description("Initialize variables")
                context(
                    parallel {
                        // next("") // can implicit assume next step to be the next to run
                        branches(
                            // not sure how to share data between branches
                            branch {
                                //# each branch missing a steps
                                name("branch1")
                                steps(
                                    step {
                                        name("Assign step")
                                        description("Initialize variables")
                                        assign {
                                            context(
                                                variables(variable("Hello") equalTo value("Hello"))
                                            )
                                        }
                                    }
                                )
                            },
                            branch {
                                name("branch2")
                                steps(
                                    step {
                                        name("Assign step 2")
                                        description("Initialize variables")
                                        assign {
                                            context(
                                                variables(variable("Hello") equalTo value("Hello"))
                                            )
                                        }
                                    }
                                )
                            }
                        )
                    }
                )
            },
            step {
                name("Parallel Iteration")
                description("Initialize variables")
                context(
                    parallel {
                        // for loop unrolling might be a solution for aws, gcp already supports suporta
                        // loop unrolling
                        iteration {
                            key("key")
                            forEach(variable("listString")) // range(1, 9)
                            //loop listas e chaves de um hashmap // for map goes through keys
                            steps(
                                step {
                                    name("AssignParallelIteration")
                                    description("Initialize variables")
                                    context(
                                        assign {
                                            variables(variable("d") equalTo variable("key"))
                                        }
                                    )
                                }
                            )
                        }
                    }
                )
            },
            step {
                name("For")
                description("For example")
                context(
                    iteration {
                        key("key")
                        range(1, 9) //|| forEach(variable("listString"))
                        // lists and keys of hashmap // forEach in hashmap goes through keys
                        // TODO default does what in iteration??
                        steps(
                            step {
                                name("AssignIteration")
                                description("Initialize variables")
                                context(
                                    assign {
                                        variables(variable("number") equalTo variable("key"))
                                    }
                                )
                            }
                        )
                    }
                )
            },
            step {
                name("InitVariables")
                description("Initialize variables")
                context(
                    assign {
                        variables(
                            variable("a") equalTo value(Random().nextInt()),
                            variable("b") equalTo value(Random().nextInt()),
                            variable("c") equalTo value(Random().nextInt())
                        )
                    }
                )
            },
            step {
                name("Sum")
                description("Sum 2 random numbers")
                context(
                    call {
                        method(GET)
                        host("r1ro8xa7y8.execute-api.us-east-1.amazonaws.com")
                        path("/default/calculator")
                        query(
                            "number1" to variable("a"),
                            "number2" to variable("b"),
                            "op" to value("add")
                        )
                        result("sumResult")
                    }
                )
            },
            step {
                name("Condition")
                description("condition")
                context(
                    switch {
                        conditions(
                            condition {
                                match(variable("c") equalTo value(0))
                                jump("Assign1ToC")
                            },
                            condition {
                                match(variable("c") greaterThan value(0))
                                jump("DivWithC")
                            }
                        )
                        default("Assign1ToC")
                    }
                )
            },
            step {
                name("Assign1ToC")
                description("If c equal to 0 affect C with 1")
                context(
                    assign {
                        variables(variable("c") equalTo value(1))
                    }
                )
            },
            step {
                name("DivWithC")
                description("Divide the previous result by a random value")
                context(
                    call {
                        method(GET)
                        host("r1ro8xa7y8.execute-api.us-east-1.amazonaws.com")
                        path("/default/calculator")
                        query(
                            "number1" to variable("sumResult"),
                            "number2" to variable("c"),
                            "op" to value("div")
                        )
                        result("divResult")
                    }
                )
            }
        )
        result("divResult")
    }


    @Test
    fun `test google full deployment`() {
        val nodeTraversor = DepthFirstNodeVisitorTraversor()
        val contextVisitor = NodeContextVisitor(GoogleDefaultStrategyDeciderProvider.createNodeRendererStrategyDecider())
        val renderingContext = GoogleRenderingContext(termContext = GoogleTermContext())
        val content = nodeTraversor.traverse(contextVisitor, generalWorkflow, renderingContext)
            .filterNot(String::isEmpty)
            .joinToStringNewLines()
        println(content)
    }

    @Test
    fun `test amazon full deployment`() {
        val nodeTraversor = DepthFirstNodeVisitorTraversor()
        val contextVisitor = NodeContextVisitor(AmazonDefaultStrategyDeciderProvider.createNodeRendererStrategyDecider())
        val content = nodeTraversor.traverse(contextVisitor, generalWorkflow, AmazonRenderingContext())
            .filterNot(String::isEmpty)
            .joinToStringNewLines()
        println(content)
    }
}