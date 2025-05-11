package costaber.com.github.omniflow.renderer

import costaber.com.github.omniflow.builder.StepBuilder
import costaber.com.github.omniflow.cloud.provider.amazon.deployer.AmazonCloudDeployer
import costaber.com.github.omniflow.cloud.provider.amazon.renderer.AmazonRenderingContext
import costaber.com.github.omniflow.cloud.provider.amazon.traversor.AmazonTraversor
import costaber.com.github.omniflow.dsl.assign
import costaber.com.github.omniflow.dsl.branch
import costaber.com.github.omniflow.dsl.call
import costaber.com.github.omniflow.dsl.condition
import costaber.com.github.omniflow.dsl.iteration
import costaber.com.github.omniflow.dsl.parallel
import costaber.com.github.omniflow.dsl.step
import costaber.com.github.omniflow.dsl.switch
import costaber.com.github.omniflow.dsl.value
import costaber.com.github.omniflow.dsl.variable
import costaber.com.github.omniflow.dsl.workflow
import costaber.com.github.omniflow.model.HttpMethod.GET
import costaber.com.github.omniflow.resource.util.joinToStringNewLines
import costaber.com.github.omniflow.traversor.DepthFirstNodeVisitorTraversor
import costaber.com.github.omniflow.visitor.NodeContextVisitor
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import kotlin.test.Test

internal class AmazonRendererTest {

    private val nodeTraversor = AmazonTraversor()
    private val contextVisitor = NodeContextVisitor(AmazonCloudDeployer.Builder().createNodeRendererStrategyDecider())
    private val renderingContext = AmazonRenderingContext()

    private fun createWorkflow(vararg s: StepBuilder) = workflow {
        name("Name")
        description("Description")
        steps(*s)
        result("result")
    }

    @Test
    fun `test assign step`() {
        val w  = createWorkflow(
            step {
                name("Assign")
                description("Initialize variables")
                context(
                    assign {
                        variable("listString" equal listOf("a", "b"))
                        variable("listNumber" equal listOf(1,2))
                        variable("c" equal "true")
                    }
                )
            }
        )

        val content = nodeTraversor.traverse(contextVisitor, w, renderingContext)
            .filterNot(String::isEmpty)
            .joinToStringNewLines()

        val expected = """
            {
                "Comment": "Description",
                "StartAt": "Assign",
                "States": {
                    "Assign": {
                        "Comment": "Initialize variables",
                        "Type": "Pass",
                        "Result": {
                            "listString": ["a","b"],
                            "listNumber": [1,2],
                            "c": "true"
                        },
                        "End": true
                    }
                }
            }
        """.trimIndent()

        expectThat(content)
            .isEqualTo(expected)
    }

    @Test
    fun `test call step`() {
        val w = createWorkflow(
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
            }
        )

        val content = nodeTraversor.traverse(contextVisitor, w, renderingContext)
            .filterNot(String::isEmpty)
            .joinToStringNewLines()
        val expected = """
            {
                "Comment": "Description",
                "StartAt": "Sum",
                "States": {
                    "Sum": {
                        "Comment": "Sum 2 random numbers",
                        "Type": "Task",
                        "Resource": "arn:aws:states:::apigateway:invoke",
                        "InputPath": "${'$'}",
                        "Parameters": {
                            "ApiEndpoint": "r1ro8xa7y8.execute-api.us-east-1.amazonaws.com",
                            "Method": "GET",
                            "Path": "/default/calculator",
                            "QueryParameters": {
                                "number1.${'$'}": "States.Array(States.Format('{}', ${'$'}.a))",
                                "number2.${'$'}": "States.Array(States.Format('{}', ${'$'}.b))",
                                "op.${'$'}": "States.Array(States.Format('{}', 'add'))"
                            }
                        },
                        "ResultSelector": {
                            "sumResult.${'$'}": "${'$'}.ResponseBody"
                        },
                        "ResultPath": "${'$'}.Sum",
                        "End": true
                    }
                }
            }
        """.trimIndent()

        expectThat(content)
            .isEqualTo(expected)

    }

    @Test
    fun `test switch step`() {
        val w = createWorkflow(
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
            }
        )

        val content = nodeTraversor.traverse(contextVisitor, w, renderingContext)
            .filterNot(String::isEmpty)
            .joinToStringNewLines()
        val expected = """
            {
                "Comment": "Description",
                "StartAt": "Condition",
                "States": {
                    "Condition": {
                        "Comment": "condition",
                        "Type": "Choice",
                        "Choices": [
                            {
                                "Variable": "${'$'}.c",
                                "NumericEquals": 0,
                                "Next": "Assign1ToC"
                            },
                            {
                                "Variable": "${'$'}.c",
                                "NumericGreaterThan": 0,
                                "Next": "DivWithC"
                            }
                        ],
                        "Default": "Assign1ToC"
                    }
                }
            }
        """.trimIndent()

        expectThat(content)
            .isEqualTo(expected)

    }

    @Test
    fun `test iteration step with range`() {
        val w = createWorkflow(
            step {
                name("For")
                description("For example")
                context(
                    iteration {
                        value("key")
                        range(1, 9)
                        steps(
                            step {
                                name("AssignIteration1")
                                description("Initialize variables")
                                context(
                                    assign {
                                        variable("number" equal variable("key"))
                                    }
                                )
                            },
                            step {
                                name("AssignIteration2")
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
            }
        )

        val content = nodeTraversor.traverse(contextVisitor, w, renderingContext)
            .filterNot(String::isEmpty)
            .joinToStringNewLines()
        val expected = """
        {
            "Comment": "Description",
            "StartAt": "For",
            "States": {
                "For": {
                    "Comment": "For example",
                    "Type": "Parallel",
                    "Branches": [
                        {
                            "StartAt": "ForInitializeCounter",
                            "States": {
                            "ForInitializeCounter": {
                                "Comment": "Auto generated",
                                "Type": "Pass",
                                "Result": {
                                    "key.$": 0
                                },
                                "Next": "ForIncrementCounter"
                            },
                            "ForIncrementCounter": {
                                "Comment": "Auto generated",
                                "Type": "Pass",
                                "Result": {
                                    "key.$": "States.MathAdd($.key, 1)"
                                },
                                "Next": "AssignIteration1"
                            },
                            "AssignIteration1": {
                                "Comment": "Initialize variables",
                                "Type": "Pass",
                                "Result": {
                                    "number": "$.key"
                                },
                                "Next": "AssignIteration2"
                            },
                            "AssignIteration2": {
                                "Comment": "Initialize variables",
                                "Type": "Pass",
                                "Result": {
                                    "number": "$.key"
                                },
                                "Next": "ForLoop?"
                            },
                            "ForLoop?": {
                                "Comment": "Auto generated",
                                "Type": "Choice",
                                "Choices": [
                                    {
                                        "Variable": "$.key",
                                        "NumericLessThan": 9,
                                        "Next": "ForIncrementCounter"
                                    }
                                ],
                                "Default": "ForEndLoop"
                            },
                            "ForEndLoop": {
                                "Comment": "Auto generated",
                                "Type": "Pass",
                                "Result": {
                                },
                                "End": true
                            }
                        }
                    }
                    ],
                    "End": true
                }
            }
        }
        """.trimIndent()

        expectThat(content)
            .isEqualTo(expected)
    }

    @Test
    fun `test iteration step with forEach`() {
        val w = createWorkflow(
            step {
                name("For")
                description("For example")
                context(
                    iteration {
                        value("key")
                        forEach(variable("listString"))
                        steps(
                            step {
                                name("AssignIteration1")
                                description("Initialize variables")
                                context(
                                    assign {
                                        variable("number" equal variable("key"))
                                    }
                                )
                            },
                            step {
                                name("AssignIteration2")
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
            }
        )

        val content = nodeTraversor.traverse(contextVisitor, w, renderingContext)
            .filterNot(String::isEmpty)
            .joinToStringNewLines()
        val expected = """
        {
            "Comment": "Description",
            "StartAt": "For",
            "States": {
                "For": {
                    "Comment": "For example",
                    "Type": "Parallel",
                    "Branches": [
                        {
                            "StartAt": "ForInitializeCounter",
                            "States": {
                            "ForInitializeCounter": {
                                "Comment": "Auto generated",
                                "Type": "Pass",
                                "Result": {
                                    "Index": -1
                                },
                                "Next": "ForIncrementCounter"
                            },
                            "ForIncrementCounter": {
                                "Comment": "Auto generated",
                                "Type": "Pass",
                                "Result": {
                                    "Index.$": "States.MathAdd($.Index, 1)",
                                    "key.$": "States.ArrayGet($.listString, $.Index)"
                                },
                                "Next": "AssignIteration1"
                            },
                            "AssignIteration1": {
                                "Comment": "Initialize variables",
                                "Type": "Pass",
                                "Result": {
                                    "number": "$.key"
                                },
                                "Next": "AssignIteration2"
                            },
                            "AssignIteration2": {
                                "Comment": "Initialize variables",
                                "Type": "Pass",
                                "Result": {
                                    "number": "$.key"
                                },
                                "Next": "ForLoop?"
                            },
                            "ForLoop?": {
                                "Comment": "Auto generated",
                                "Type": "Choice",
                                "Choices": [
                                    {
                                        "Variable": "$.Index",
                                        "NumericLessThanPath": States.ArraySize($.listString),
                                        "Next": "ForIncrementCounter"
                                    }
                                ],
                                "Default": "ForEndLoop"
                            },
                            "ForEndLoop": {
                                "Comment": "Auto generated",
                                "Type": "Pass",
                                "Result": {
                                },
                                "End": true
                            }
                        }
                    }
                    ],
                    "End": true
                }
            }
        }
        """.trimIndent()

        expectThat(content)
            .isEqualTo(expected)
    }

    @Test
    fun `test parallel step`() {
        val w = createWorkflow(
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
            }
        )

        val content = nodeTraversor.traverse(contextVisitor, w, renderingContext)
            .filterNot(String::isEmpty)
            .joinToStringNewLines()
        val expected = """
            {
                "Comment": "Description",
                "StartAt": "Parallel",
                "States": {
                    "Parallel": {
                        "Comment": "Initialize variables",
                        "Type": "Parallel",
                        "Branches": [
                            {
                                "StartAt": "Assign step",
                                "States": {
                                    "Assign step": {
                                        "Comment": "Initialize variables",
                                        "Type": "Pass",
                                        "Result": {
                                            "Hello": "Hello"
                                        },
                                        "End": true
                                    }
                                }
                            },
                            {
                                "StartAt": "Assign step 2",
                                "States": {
                                    "Assign step 2": {
                                        "Comment": "Initialize variables",
                                        "Type": "Pass",
                                        "Result": {
                                            "Hello": "Hello"
                                        },
                                        "End": true
                                    }
                                }
                            }
                        ],
                        "End": true
                    }
                }
            }
        """.trimIndent()

        expectThat(content)
            .isEqualTo(expected)
    }

    @Test
    fun `test parallel with iteration step with forEach`(){
        val w = createWorkflow(
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
                                    name("AssignParallelIteration1")
                                    description("Initialize variables")
                                    context(
                                        assign {
                                            variable("d" equal variable("key"))
                                        }
                                    )
                                },
                                step {
                                    name("AssignParallelIteration2")
                                    description("Initialize variables")
                                    context(
                                        assign {
                                            variable("d" equal variable("key"))
                                        }
                                    )
                                }
                            )
                        }
                    }
                )
            }
        )

        val content = nodeTraversor.traverse(contextVisitor, w, renderingContext)
            .filterNot(String::isEmpty)
            .joinToStringNewLines()
        val expected = """
        """.trimIndent()

        expectThat(content)
            .isEqualTo(expected)
    }


    @Test
    fun `test parallel with iteration step with range`(){
        val w = createWorkflow(
            step {
                name("Parallel Iteration")
                description("Initialize variables")
                context(
                    parallel {
                        // for loop unrolling might be a solution for aws, gcp already supports suporta
                        // loop unrolling
                        iteration {
                            value("key")
                            range(1, 3)
                            //loop listas e chaves de um hashmap // for map goes through keys
                            steps(
                                step {
                                    name("AssignParallelIteration1")
                                    description("Initialize variables")
                                    context(
                                        assign {
                                            variable("d" equal variable("key"))
                                        }
                                    )
                                },
                                step {
                                    name("AssignParallelIteration2")
                                    description("Initialize variables")
                                    context(
                                        assign {
                                            variable("d" equal variable("key"))
                                        }
                                    )
                                }
                            )
                        }
                    }
                )
            }
        )

        val content = nodeTraversor.traverse(contextVisitor, w, renderingContext)
            .filterNot(String::isEmpty)
            .joinToStringNewLines()
        val expected = """
        {
            "Comment": "Description",
            "StartAt": "Parallel Iteration",
            "States": {
                "Parallel Iteration": {
                    "Comment": "Initialize variables",
                    "Type": "Parallel",
                    "Branches": [
                        {
                            "StartAt": "Parallel IterationInitializeCounter",
                            "States": {
                                "Parallel IterationInitializeCounter": {
                                    "Comment": "Auto generated",
                                    "Type": "Pass",
                                    "Result": {
                                        "key.${'$'}": 1
                                    },
                                    "Next": "AssignParallelIteration1"
                                },
                                "AssignParallelIteration1": {
                                    "Comment": "Initialize variables",
                                    "Type": "Pass",
                                    "Result": {
                                        "d": "$.key"
                                    },
                                    "Next": "AssignParallelIteration2"
                                },
                                "AssignParallelIteration2": {
                                    "Comment": "Initialize variables",
                                    "Type": "Pass",
                                    "Result": {
                                        "d": "$.key"
                                    },
                                    "End": true
                                }
                            }
                        },
                        {
                            "StartAt": "Parallel IterationInitializeCounter",
                            "States": {
                                "Parallel IterationInitializeCounter": {
                                    "Comment": "Auto generated",
                                    "Type": "Pass",
                                    "Result": {
                                        "key.${'$'}": 2
                                    },
                                    "Next": "AssignParallelIteration1"
                                },
                                "AssignParallelIteration1": {
                                    "Comment": "Initialize variables",
                                    "Type": "Pass",
                                    "Result": {
                                        "d": "$.key"
                                    },
                                    "Next": "AssignParallelIteration2"
                                },
                                "AssignParallelIteration2": {
                                    "Comment": "Initialize variables",
                                    "Type": "Pass",
                                    "Result": {
                                        "d": "$.key"
                                    },
                                    "End": true
                                }
                            }
                        },
                        {
                            "StartAt": "Parallel IterationInitializeCounter",
                            "States": {
                                "Parallel IterationInitializeCounter": {
                                    "Comment": "Auto generated",
                                    "Type": "Pass",
                                    "Result": {
                                        "key.${'$'}": 3
                                    },
                                    "Next": "AssignParallelIteration1"
                                },
                                "AssignParallelIteration1": {
                                    "Comment": "Initialize variables",
                                    "Type": "Pass",
                                    "Result": {
                                        "d": "$.key"
                                    },
                                    "Next": "AssignParallelIteration2"
                                },
                                "AssignParallelIteration2": {
                                    "Comment": "Initialize variables",
                                    "Type": "Pass",
                                    "Result": {
                                        "d": "$.key"
                                    },
                                    "End": true
                                }
                            }
                        }
                    ],
                    "End": true
                }
            }
        }
        """.trimIndent()

        expectThat(content)
            .isEqualTo(expected)
    }

}