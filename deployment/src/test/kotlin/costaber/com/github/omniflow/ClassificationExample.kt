package costaber.com.github.omniflow

import costaber.com.github.omniflow.cloud.provider.amazon.deployer.AmazonCloudDeployer
import costaber.com.github.omniflow.cloud.provider.amazon.renderer.AmazonRenderingContext
import costaber.com.github.omniflow.cloud.provider.google.deployer.GoogleCloudDeployer
import costaber.com.github.omniflow.cloud.provider.google.renderer.GoogleRenderingContext
import costaber.com.github.omniflow.cloud.provider.google.renderer.GoogleTermContext
import costaber.com.github.omniflow.dsl.*
import costaber.com.github.omniflow.model.HttpMethod
import costaber.com.github.omniflow.model.Value
import costaber.com.github.omniflow.resource.util.joinToStringNewLines
import costaber.com.github.omniflow.traversor.DepthFirstNodeVisitorTraversor
import costaber.com.github.omniflow.util.CONTENT_TYPE_APPLICATION_JSON
import costaber.com.github.omniflow.util.HEADER_CONTENT_TYPE
import costaber.com.github.omniflow.visitor.NodeContextVisitor
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

internal class ClassificationExample {

    val workflow = workflow {
        name("ParallelSentimentClassifier")
        description("Classify sentiment for multiple texts in parallel")
        steps(
            step {
                name("AssignInputs")
                description("Set the list of texts")
                context(
                    assign {
                        variables(
                            variable("inputTexts") equalTo value(listOf(
                                "Awesome!",
                                "This is bad.",
                                "Neutral Response."
                            ))
                        )
                        variables(variable("feedback") equalTo value(mapOf<String, Value<*>>()))
                    }
                )
            },
            step {
                name("ProcessSentiments")
                description("Run sentiment classification in parallel for each input")
                context(
                    parallel {
                        iteration {
                            key("textItem")
                            forEach(variable("inputTexts"))
                            steps(
                                step {
                                    name("CallSentimentAPI")
                                    description("Calls a sentiment classification API")
                                    context(
                                        call {
                                            method(HttpMethod.POST)
                                            host("https://sentiment.soik.eu")
                                            path("/analyze")
                                            header(
                                                HEADER_CONTENT_TYPE to value(CONTENT_TYPE_APPLICATION_JSON),
                                                "X-API-Key" to value("your-secret-api-key-here"),
                                            )
                                            body(mapOf("text" to variable("textItem")))
                                            result("sentiment")
                                        }
                                    )
                                },
                                step {
                                    name("ClassifyResult")
                                    description("Handles result")
                                    context(
                                        switch {
                                            conditions(
                                                condition {
                                                    match(variable("sentiment.body.sentiment_score") equalTo value(1))
                                                    jump("PositiveFeedback")
                                                },
                                                condition {
                                                    match(variable("sentiment.body.sentiment_score") equalTo value(-1))
                                                    jump("NegativeFeedback")
                                                }
                                            )
                                            default("NeutralFeedback")
                                        }
                                    )
                                },
                                step {
                                    name("PositiveFeedback")
                                    description("Sets feedback to positive")
                                    context(
                                        assign {
                                            variables(
                                                variable("feedback").withKey("textItem") equalTo value("Positive")
                                            )
                                        }
                                    )
                                    next("Continue")
                                },
                                step {
                                    name("NegativeFeedback")
                                    description("Sets feedback to negative")
                                    context(
                                        assign {
                                            variables(
                                                variable("feedback").withKey("textItem") equalTo value("Negative")
                                            )
                                        }
                                    )
                                    next("Continue")
                                },
                                step {
                                    name("NeutralFeedback")
                                    description("Sets feedback to neutral")
                                    context(
                                        assign {
                                            variables(
                                                variable("feedback").withKey("textItem") equalTo value("Neutral")
                                            )
                                        }
                                    )
                                    next("Continue")
                                },
                                step {
                                    name("Continue")
                                    description("Continue to next step")
                                    context(
                                        assign {
                                            variables(
                                                variable("_") equalTo value("")
                                            )
                                        }
                                    )
                                }
                            )
                        }
                    }
                )
            }
        )

        result("feedback")
    }

    @Test
    fun `test classification workflow for gcp`() {
        val nodeTraversor = DepthFirstNodeVisitorTraversor()
        val contextVisitor = NodeContextVisitor(GoogleCloudDeployer.Builder().createNodeRendererStrategyDecider())
        val renderingContext = GoogleRenderingContext(termContext = GoogleTermContext())
        val content = nodeTraversor.traverse(contextVisitor, workflow, renderingContext)
            .filterNot(String::isEmpty)
            .joinToStringNewLines()
        val expected = """
            main:
                steps:
                    - AssignInputs:
                        assign:
                            - inputTexts: [Awesome!, This is bad., Neutral Response.]
                            - feedback: {}
                    - ProcessSentiments:
                        parallel:
                                for:
                                    value: textItem
                                    in: ${"$"}{inputTexts}
                                    steps:
                                        - CallSentimentAPI:
                                            call: http.post
                                            args:
                                                url: https://sentiment.soik.eu/analyze
                                                headers:
                                                    Content-Type: 'application/json'
                                                    X-API-Key: 'your-secret-api-key-here'
                                                body:
                                                    text: '${"$"}{textItem}'
                                            result: sentiment
                                        - ClassifyResult:
                                            switch:
                                                - condition:
                                                    ${"$"}{sentiment.body.sentiment_score == 1}
                                                  next: PositiveFeedback
                                                - condition:
                                                    ${"$"}{sentiment.body.sentiment_score == -1}
                                                  next: NegativeFeedback
                                            next: NeutralFeedback
                                        - PositiveFeedback:
                                            next: Continue
                                            assign:
                                                - feedback[textItem]: 'Positive'
                                        - NegativeFeedback:
                                            next: Continue
                                            assign:
                                                - feedback[textItem]: 'Negative'
                                        - NeutralFeedback:
                                            next: Continue
                                            assign:
                                                - feedback[textItem]: 'Neutral'
                                        - Continue:
                                            assign:
                                                - _: ''
                                shared: [feedback]
                    - return_output:
                        return: ${"$"}{feedback}
        """.trimIndent()

        expectThat(content).isEqualTo(expected)
    }

    @Test
    fun `test classification workflow for aws`() {
        val nodeTraversor = DepthFirstNodeVisitorTraversor()
        val contextVisitor = NodeContextVisitor(AmazonCloudDeployer.Builder().createNodeRendererStrategyDecider())
        val renderingContext = AmazonRenderingContext()
        val content = nodeTraversor.traverse(contextVisitor, workflow, renderingContext)
            .filterNot(String::isEmpty)
            .joinToStringNewLines()
        val expected = """
            {
                "Comment": "Classify sentiment for multiple texts in parallel",
                "StartAt": "AssignInputs",
                "States": {
                    "AssignInputs": {
                        "Comment": "Set the list of texts",
                        "Type": "Pass",
                        "Result": {
                            "inputTexts": ["Awesome!","This is bad.","Neutral Response."],
                            "feedback": {}
                        },
                        "Next": "ProcessSentiments"
                    },
                    "ProcessSentiments": {
                        "Comment": "Run sentiment classification in parallel for each input",
                        "Type": "Map",
                        "ItemsPath": "$.inputTexts",
                        "ItemSelector": {
                            "textItem.$": "$$.Map.Item.Value"
                        },
                        "ItemProcessor": {
                            "StartAt": "InnerMapProcessSentiments",
                            "States": {
                                "InnerMapProcessSentiments": {
                                    "Type": "Parallel",
                                    "Branches": [
                                        {
                                            "StartAt": "CallSentimentAPI",
                                            "States": {
                                                "CallSentimentAPI": {
                                                    "Comment": "Calls a sentiment classification API",
                                                    "Type": "Task",
                                                    "Resource": "arn:aws:states:::apigateway:invoke",
                                                    "InputPath": "$",
                                                    "Parameters": {
                                                        "ApiEndpoint": "xnivwfynoh.execute-api.eu-north-1.amazonaws.com",
                                                        "Method": "POST",
                                                        "Path": "/analyze",
                                                        "Headers": {
                                                            "Content-Type.$": "States.Array(States.Format('{}', 'application/json'))",
                                                            "X-API-Key.$": "States.Array(States.Format('{}', 'your-secret-api-key-here'))"
                                                        },
                                                        "RequestBody": {
                                                            "text.$":"$.textItem"
                                                        }
                                                    },
                                                    "ResultSelector": {
                                                        "sentiment.$": "$.ResponseBody"
                                                    },
                                                    "ResultPath": "$.CallSentimentAPI",
                                                    "Next": "ClassifyResult"
                                                },
                                                "ClassifyResult": {
                                                    "Comment": "Handles result",
                                                    "Type": "Choice",
                                                    "Choices": [
                                                        {
                                                            "Variable": "$.CallSentimentAPI.sentiment.sentiment_score",
                                                            "NumericEquals": 1,
                                                            "Next": "PositiveFeedback"
                                                        },
                                                        {
                                                            "Variable": "$.CallSentimentAPI.sentiment.sentiment_score",
                                                            "NumericEquals": -1,
                                                            "Next": "NegativeFeedback"
                                                        }
                                                    ],
                                                    "Default": "NeutralFeedback"
                                                },
                                                "PositiveFeedback": {
                                                    "Comment": "Sets feedback to positive",
                                                    "Type": "Pass",
                                                    "Result": {
                                                        "feedback": "Positive"
                                                    },
                                                    "Next": "Continue"
                                                },
                                                "NegativeFeedback": {
                                                    "Comment": "Sets feedback to negative",
                                                    "Type": "Pass",
                                                    "Result": {
                                                        "feedback": "Negative"
                                                    },
                                                    "Next": "Continue"
                                                },
                                                "NeutralFeedback": {
                                                    "Comment": "Sets feedback to neutral",
                                                    "Type": "Pass",
                                                    "Result": {
                                                        "feedback": "Neutral"
                                                    },
                                                    "Next": "Continue"
                                                },
                                                "Continue": {
                                                    "Comment": "Continue to next step",
                                                    "Type": "Pass",
                                                    "Result": {
                                                        "_": ""
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

        expectThat(content).isEqualTo(expected)
    }


}
