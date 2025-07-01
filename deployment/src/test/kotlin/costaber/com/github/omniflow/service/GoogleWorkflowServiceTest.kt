package costaber.com.github.omniflow.service

import costaber.com.github.omniflow.cloud.provider.google.service.GoogleWorkflowService
import costaber.com.github.omniflow.util.getResourceContent
import org.junit.Ignore
import org.junit.jupiter.api.Test

internal class GoogleWorkflowServiceTest {

    @Test
    @Ignore
    fun `deploy test happy path`() {
        val workflowDefinition = this::class.java.classLoader
            .getResourceContent("workflows/GcpWorkflow.yaml")
        val googleWorkflowService = GoogleWorkflowService()
        googleWorkflowService.deploy(
            projectId = "workflow-test-380423",
            zone = "us-east1",
            serviceAccount = "projects/workflow-test-380423/serviceAccounts/" +
                    "service-account-test@workflow-test-380423.iam.gserviceaccount.com",
            workflowId = "testing_client_library_3",
            workflowDescription = "Testing the creation of a workflow using Client Library",
            workflowLabels = mapOf("environment" to "testing", "app" to "omni-flow"),
            workflowSourceContents = workflowDefinition
        )
    }

}