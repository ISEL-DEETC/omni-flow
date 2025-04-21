package costaber.com.github.omniflow

import costaber.com.github.omniflow.cloud.provider.amazon.deployer.AmazonCloudDeployer
import costaber.com.github.omniflow.cloud.provider.amazon.renderer.AmazonRenderingContext
import costaber.com.github.omniflow.cloud.provider.google.deployer.GoogleCloudDeployer
import costaber.com.github.omniflow.cloud.provider.google.renderer.GoogleTermContext
import costaber.com.github.omniflow.dsl.*
import costaber.com.github.omniflow.model.HttpMethod.GET
import costaber.com.github.omniflow.renderer.IndentedRenderingContext
import costaber.com.github.omniflow.resource.util.joinToStringNewLines
import costaber.com.github.omniflow.traversor.DepthFirstNodeVisitorTraversor
import costaber.com.github.omniflow.visitor.NodeContextVisitor
import org.junit.Test
import java.util.*

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
                        variable("listString" equal listOf("a", "b"))
                        variable("listNumber" equal listOf(1,2))
                        variable("c" equal "true")
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
                                                variable("Hello"  equal "Hello")
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
                                                variable("Hello"  equal "Hello")
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
                            value("key")
                            forEach(variable("listString")) // range(1, 9)
                            //loop listas e chaves de um hashmap // for map goes through keys
                            steps(
                                step {
                                    name("AssignParallelIteration")
                                    description("Initialize variables")
                                    context(
                                        assign {
                                            variable("d" equal variable("key"))
                                        }
                                    )
                                }
                            )
                        }
                        // next("") // can implicit assume next step to be the next to run
//                        branches(
//                            // not sure how to share data between branches
//                            steps(
//                                step {
//                                    assign {
//                                        context(
//                                            variable("Hello"  equal "Hello")
//                                        )
//                                    }
//                                }
//                            ),
//                            steps(
//                                step {
//                                    assign {
//                                        variable("World" equal "Hello")
//                                    }
//                                }
//                            )
//                        )
                    }
                )
            },
            step {
                name("For")
                description("For example")
                context(
                    iteration {
                        value("key")
                        range(1, 9) //|| forEach(variable("listString"))
                        // lists and keys of hashmap // forEach in hashmap goes through keys
                        // TODO default does what in iteration??
                        steps(
                            step {
                                name("AssignIteration")
                                description("Initialize variables")
                                context(
                                    assign {
                                        variable("number" equal variable("key"))
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
                        variable("a" equal Random().nextInt())
                        variable("b" equal Random().nextInt())
                        variable("c" equal Random().nextInt())
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
                        variable("c" equal 1)
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
        val contextVisitor = NodeContextVisitor(GoogleCloudDeployer.Builder().createNodeRendererStrategyDecider())
        val renderingContext = IndentedRenderingContext(termContext = GoogleTermContext())
        val content = nodeTraversor.traverse(contextVisitor, generalWorkflow, renderingContext)
            .filterNot(String::isEmpty)
            .joinToStringNewLines()
        println(content)
    }

    @Test
    fun `test amazon full deployment`() {
        val nodeTraversor = DepthFirstNodeVisitorTraversor()
        val contextVisitor = NodeContextVisitor(AmazonCloudDeployer.Builder().createNodeRendererStrategyDecider())
        val content = nodeTraversor.traverse(contextVisitor, generalWorkflow, AmazonRenderingContext())
            .filterNot(String::isEmpty)
            .joinToStringNewLines()
        println(content)
    }
}