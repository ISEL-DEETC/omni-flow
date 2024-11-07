package costaber.com.github.omniflow

import costaber.com.github.omniflow.dsl.*
import costaber.com.github.omniflow.model.HttpMethod.GET
import java.util.*

internal class WorkflowParallelTest {

    private val generalWorkflow = workflow {
        name("calculatorWorkflow")
        description("Calculator example")
        steps(
            step {
                name("Parallel")
                description("Initialize variables")
                context(
                    parallel {
                        // next("") // can implicit assume next step to be the next to run
                        branches(
                            // not sure how to share data between branches
                            steps(
                                step {}
                            ),
                            steps(
                                step {}
                            )
                        )
                    }
                )
            },
            step {
                name("ListAssign")
                description("Initialize variables")
                context(
                    assign {
                        variable("listString" equal listOf("a", "b"))
                    }
                )
            },
            step {
                name("For")
                description("For example")
                context(
                    loop {
                        value("value")
                        range(1, 9) or forEach(variable("listString"))
                        // for map goes through keys
                        steps()
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
}