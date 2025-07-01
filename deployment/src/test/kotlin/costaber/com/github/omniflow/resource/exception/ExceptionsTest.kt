package costaber.com.github.omniflow.resource.exception

import costaber.com.github.omniflow.util.WORKFLOW_NAME_1
import kotlin.test.Test
import strikt.api.expectThat
import strikt.assertions.isA
import strikt.assertions.isEqualTo

internal class ExceptionsTest {

    @Test
    fun `ExternalCloudClientException is correct`() {
        val cloudProvider = "MyCloudProvider"
        val message = "Error message"
        val throwable = IllegalStateException()
        val externalCloudClientException = ExternalCloudClientException(
            workflowName = WORKFLOW_NAME_1,
            cloudProvider = cloudProvider,
            message = message,
            throwable = throwable,
        )
        expectThat(externalCloudClientException)
            .isA<ExternalCloudClientException>()
            .and {
                get { localizedMessage }.isEqualTo(
                    "Error while deploying $WORKFLOW_NAME_1 for cloud provider $cloudProvider: $message"
                )
                get { throwable }.isEqualTo(throwable)
            }
    }
}