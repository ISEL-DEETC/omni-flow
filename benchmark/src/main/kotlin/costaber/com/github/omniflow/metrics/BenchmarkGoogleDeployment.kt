package costaber.com.github.omniflow.metrics

import costaber.com.github.omniflow.cloud.provider.google.provider.GoogleDefaultStrategyDeciderProvider
import costaber.com.github.omniflow.cloud.provider.google.renderer.GoogleRenderingContext
import costaber.com.github.omniflow.cloud.provider.google.renderer.GoogleTermContext
import costaber.com.github.omniflow.cloud.provider.google.service.GoogleWorkflowService
import costaber.com.github.omniflow.generator.WorkflowGenerator.textTranslator
import costaber.com.github.omniflow.provider.OfficialWorkflowSamplesProvider
import costaber.com.github.omniflow.traversor.DepthFirstNodeVisitorTraversor
import costaber.com.github.omniflow.util.Constants
import costaber.com.github.omniflow.util.ListUtils
import costaber.com.github.omniflow.visitor.NodeContextVisitor
import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.Setup
import java.io.IOException

open class BenchmarkGoogleDeployment : BenchmarkWorkflowDeployment() {
    private lateinit var googleWorkflowService: GoogleWorkflowService

    @Setup
    @Throws(IOException::class)
    fun setup() {
        val traversor = DepthFirstNodeVisitorTraversor()
        val googleContextVisitor = NodeContextVisitor(
            GoogleDefaultStrategyDeciderProvider.createNodeRendererStrategyDecider()
        )
        val googleRenderingContext = GoogleRenderingContext(
            0, StringBuilder(), GoogleTermContext()
        )
        val workflow = textTranslator()
        generatedWorkflow = ListUtils.collectListString(
            traversor.traverse(
                googleContextVisitor,
                workflow,
                googleRenderingContext
            ).toMutableList()
        )
        exampleWorkflow = OfficialWorkflowSamplesProvider.google()
        googleWorkflowService = GoogleWorkflowService()
    }

    @Benchmark
    override fun benchmarkGeneratedWorkflowDeployment() {
        googleWorkflowService.deploy(
            PROJECT_ID,
            ZONE,
            SERVICE_ACCOUNT,
            Constants.GENERATED_WORKFLOW_NAME,
            Constants.GOOGLE_GENERATED_WORKFLOW_DESC,
            LABELS,
            generatedWorkflow
        )
    }

    //@Benchmark
    override fun benchmarkExampleWorkflowDeployment() {
        googleWorkflowService.deploy(
            PROJECT_ID,
            ZONE,
            SERVICE_ACCOUNT,
            Constants.EXAMPLE_WORKFLOW_NAME,
            Constants.GOOGLE_EXAMPLE_WORKFLOW_DESC,
            LABELS,
            exampleWorkflow
        )
    }

    companion object {
        private val PROJECT_ID: String = System.getenv(Constants.GOOGLE_PROJECT_ID_ENV_VAR)
        private val ZONE: String = System.getenv(Constants.GOOGLE_ZONE_ENV_VAR)
        private val SERVICE_ACCOUNT: String = System.getenv(Constants.GOOGLE_SERVICE_ACCOUNT_ENV_VAR)
        private val LABELS: Map<String, String> = mapOf(
            Constants.ORIGIN_LABEL to Constants.OMNI_FLOW_NAME,
            Constants.CLOUD_LABEL to Constants.GOOGLE,
            Constants.ENV_STR to Constants.ENVIRONMENT
        )
    }
}
