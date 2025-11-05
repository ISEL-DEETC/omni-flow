package costaber.com.github.omniflow.renderer

import costaber.com.github.omniflow.builder.StepBuilder
import costaber.com.github.omniflow.cloud.provider.amazon.provider.AmazonDefaultStrategyDeciderProvider.createNodeRendererStrategyDecider
import costaber.com.github.omniflow.cloud.provider.amazon.renderer.AmazonRenderingContext
import costaber.com.github.omniflow.cloud.provider.amazon.traversor.AmazonTraversor
import costaber.com.github.omniflow.dsl.*
import costaber.com.github.omniflow.model.HttpMethod.GET
import costaber.com.github.omniflow.resource.util.joinToStringNewLines
import costaber.com.github.omniflow.traversor.VisitorObserver
import costaber.com.github.omniflow.visitor.NodeContextVisitor
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import kotlin.test.Test

internal class AmazonRendererTest {
    private val observer = VisitorObserver()
    private val nodeTraversor = AmazonTraversor().registerObserver(observer)
    private val contextVisitor = NodeContextVisitor(createNodeRendererStrategyDecider())
    private val renderingContext = AmazonRenderingContext()

    private fun createWorkflow(vararg s: StepBuilder) = workflow {
        name("Name")
        description("Description")
        steps(*s)
        result("result")
    }

    @Test
    fun `test assign step`() {
        val w = createWorkflow(
            step {
                name("Assign")
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
                        host("example.com")
                        path("/calculator")
                        query(
                            "number1" to variable("a"),
                            "number2" to variable("b"),
                            "op" to value("add")
                        )
                        body(
                            "a" to value(1),
                            "b" to value(2)
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
                        "InputPath": "$",
                        "Parameters": {
                            "ApiEndpoint": "example.com",
                            "Method": "GET",
                            "Path": "/calculator",
                            "QueryParameters": {
                                "number1.$": "States.Array(States.Format('{}', $.a))",
                                "number2.$": "States.Array(States.Format('{}', $.b))",
                                "op.$": "States.Array(States.Format('{}', 'add'))"
                            },
                            "RequestBody": {
                                "a":1,
                                "b":2
                            }
                        },
                        "ResultSelector": {
                            "sumResult.$": "$.ResponseBody"
                        },
                        "ResultPath": "$.Sum",
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
                        key("key")
                        range(1, 9)
                        steps(
                            step {
                                name("AssignIteration1")
                                description("Initialize variables")
                                context(
                                    assign {
                                        variables(variable("number") equalTo variable("key"))
                                    }
                                )
                            },
                            step {
                                name("AssignIteration2")
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
                        key("key")
                        forEach(variable("listString"))
                        steps(
                            step {
                                name("AssignIteration1")
                                description("Initialize variables")
                                context(
                                    assign {
                                        variables(variable("number") equalTo variable("key"))
                                    }
                                )
                            },
                            step {
                                name("AssignIteration2")
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
                                        "Index.$": -1,
                                        "ArraySize.$": "$.States.ArraySize($.listString)"
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
                                            "NumericLessThanPath": "$.ArraySize",
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
                            branch {
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
    fun `test parallel with iteration step with forEach`() {
        val w = createWorkflow(
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
                                    name("AssignParallelIteration1")
                                    description("Initialize variables")
                                    context(
                                        assign {
                                            variables(variable("d") equalTo variable("key"))
                                        }
                                    )
                                },
                                step {
                                    name("AssignParallelIteration2")
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
                    "Type": "Map",
                    "ItemsPath": "$.listString",
                    "ItemSelector": {
                        "key.$": "$$.Map.Item.Value"
                    },
                    "ItemProcessor": {
                        "StartAt": "InnerMapParallel Iteration",
                        "States": {
                            "InnerMapParallel Iteration": {
                                "Type": "Parallel",
                                "Branches": [
                                    {
                                        "StartAt": "AssignParallelIteration1",
                                        "States": {
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
    fun `test parallel with iteration step with range`() {
        val w = createWorkflow(
            step {
                name("Parallel Iteration")
                description("Initialize variables")
                context(
                    parallel {
                        iteration {
                            key("key")
                            range(1, 3)
                            steps(
                                step {
                                    name("AssignParallelIteration1")
                                    description("Initialize variables")
                                    context(
                                        assign {
                                            variables(variable("d") equalTo variable("key"))
                                        }
                                    )
                                },
                                step {
                                    name("AssignParallelIteration2")
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
                                        "key.$": 1
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
                                        "key.$": 2
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
                                        "key.$": 3
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