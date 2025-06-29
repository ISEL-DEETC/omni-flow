package costaber.com.github.omniflow.renderer

import costaber.com.github.omniflow.builder.StepBuilder
import costaber.com.github.omniflow.cloud.provider.google.deployer.GoogleCloudDeployer
import costaber.com.github.omniflow.cloud.provider.google.renderer.GoogleRenderingContext
import costaber.com.github.omniflow.cloud.provider.google.renderer.GoogleTermContext
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
import org.junit.jupiter.api.Test

internal class GoogleRendererTest {

    private val nodeTraversor = DepthFirstNodeVisitorTraversor()
    private val contextVisitor = NodeContextVisitor(GoogleCloudDeployer.Builder().createNodeRendererStrategyDecider())
    private val renderingContext = GoogleRenderingContext(termContext = GoogleTermContext())

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
                        variables(
                            variable("listString") equalTo value(listOf("a", "b")),
                            variable("listNumber") equalTo value(listOf(1,2)),
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
            main:
                steps:
                    - Assign:
                        assign:
                            - listString: [a, b]
                            - listNumber: [1, 2]
                            - c: 'true'
                    - return_output:
                        return: ${"$"}{result}
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
                        result("sumResult")
                    }
                )
            }
        )

        val content = nodeTraversor.traverse(contextVisitor, w, renderingContext)
            .filterNot(String::isEmpty)
            .joinToStringNewLines()
        val expected = """
            main:
                steps:
                    - Sum:
                        call: http.get
                        args:
                            url: example.com/calculator
                            query:
                                number1: ${"$"}{a}
                                number2: ${"$"}{b}
                                op: 'add'
                        result: sumResult
                    - return_output:
                        return: ${"$"}{result}
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
            main:
                steps:
                    - Condition:
                        switch:
                            - condition:
                                ${"$"}{c == 0}
                              next: Assign1ToC
                            - condition:
                                ${"$"}{c > 0}
                              next: DivWithC
                        next: Assign1ToC
                    - return_output:
                        return: ${"$"}{result}
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
            main:
                steps:
                    - For:
                        for:
                            value: key
                            range: [1, 9]
                            steps:
                                - AssignIteration1:
                                    assign:
                                        - number: ${"$"}{key}
                                - AssignIteration2:
                                    assign:
                                        - number: ${"$"}{key}
                    - return_output:
                        return: ${"$"}{result}
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
            main:
                steps:
                    - For:
                        for:
                            value: key
                            in: ${"$"}{listString}
                            steps:
                                - AssignIteration1:
                                    assign:
                                        - number: ${"$"}{key}
                                - AssignIteration2:
                                    assign:
                                        - number: ${"$"}{key}
                    - return_output:
                        return: ${"$"}{result}
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
            main:
                steps:
                    - Parallel:
                        parallel:
                            branches:
                                - branch1:
                                    steps:
                                            - Assign step:
                                                assign:
                                                    - Hello: 'Hello'
                                - branch2:
                                    steps:
                                            - Assign step 2:
                                                assign:
                                                    - Hello: 'Hello'
                    - return_output:
                        return: ${"$"}{result}
        """.trimIndent()

        expectThat(content)
            .isEqualTo(expected)
    }

    @Test
    fun `test parallel with iteration step`(){
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
            main:
                steps:
                    - Parallel Iteration:
                        parallel:
                                for:
                                    value: key
                                    in: ${"$"}{listString}
                                    steps:
                                        - AssignParallelIteration1:
                                            assign:
                                                - d: ${"$"}{key}
                                        - AssignParallelIteration2:
                                            assign:
                                                - d: ${"$"}{key}
                    - return_output:
                        return: ${"$"}{result}
        """.trimIndent()

        expectThat(content)
            .isEqualTo(expected)
    }


}