package costaber.com.github.omniflow

import costaber.com.github.omniflow.cloud.provider.amazon.deployer.AmazonCloudDeployer
import costaber.com.github.omniflow.cloud.provider.amazon.deployer.AmazonDeployContext
import costaber.com.github.omniflow.cloud.provider.google.deployer.GoogleCloudDeployer
import costaber.com.github.omniflow.cloud.provider.google.deployer.GoogleDeployContext
import costaber.com.github.omniflow.dsl.*
import costaber.com.github.omniflow.model.HttpMethod.GET
import kotlin.test.Ignore
import kotlin.test.Test
import java.util.*

internal class WorkflowTest {

    private val generalWorkflow = workflow {
        name("calculatorWorkflow")
        description("Calculator example")
        steps(
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
    @Ignore
    fun `test google full deployment`() {
        val deployer = GoogleCloudDeployer.Builder().build()
        val googleDeployContext = GoogleDeployContext(
            projectId = "workflow-test-380423",
            zone = "us-east1",
            serviceAccount = "projects/workflow-test-380423/serviceAccounts/" +
                    "service-account-test@workflow-test-380423.iam.gserviceaccount.com",
            workflowId = "calculator_workflow",
            workflowDescription = "Calculator Workflow for testing",
            workflowLabels = mapOf("environment" to "testing", "app" to "omni-flow"),
        )
        deployer.deploy(generalWorkflow, googleDeployContext)
    }

    @Test
    @Ignore
    fun `test amazon full deployment`() {
        val deployer = AmazonCloudDeployer.Builder().build()
        val amazonDeployContext = AmazonDeployContext(
            roleArn = "arn:aws:iam::610299836666:role/aws-service-role/trustedadvisor.amazonaws.com/" +
                    "AWSServiceRoleForTrustedAdvisor",
            region = "us-east-1",
            tags = mapOf(
                "environment" to "testing",
                "app" to "omni-flow"
            ),
            stateMachineName = "state_machine_calculator"
        )
        deployer.deploy(generalWorkflow, amazonDeployContext)
    }
}