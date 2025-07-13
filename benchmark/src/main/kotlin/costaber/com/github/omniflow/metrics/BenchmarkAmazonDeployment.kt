package costaber.com.github.omniflow.metrics

import costaber.com.github.omniflow.cloud.provider.amazon.renderer.AmazonRenderingContext
import costaber.com.github.omniflow.cloud.provider.amazon.service.AmazonStateMachineService
import costaber.com.github.omniflow.generator.WorkflowGenerator.saveAndGetPetFromStore
import costaber.com.github.omniflow.provider.OfficialWorkflowSamplesProvider
import costaber.com.github.omniflow.provider.StrategyDeciderProvider
import costaber.com.github.omniflow.traversor.DepthFirstNodeVisitorTraversor
import costaber.com.github.omniflow.util.Constants
import costaber.com.github.omniflow.util.ListUtils
import costaber.com.github.omniflow.visitor.NodeContextVisitor
import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.Setup
import java.io.IOException

open class BenchmarkAmazonDeployment : BenchmarkWorkflowDeployment() {
    private lateinit var amazonStateMachineService: AmazonStateMachineService

    @Setup
    @Throws(IOException::class)
    fun setup() {
        val traversor = DepthFirstNodeVisitorTraversor()
        val amazonContextVisitor = NodeContextVisitor(
            StrategyDeciderProvider.amazonNodeRendererStrategyDecider()
        )
        val amazonRenderingContext = AmazonRenderingContext()
        val saveAndGetPetFromStoreWorkflow = saveAndGetPetFromStore()
        generatedWorkflow = ListUtils.collectListString(
            traversor.traverse(
                amazonContextVisitor,
                saveAndGetPetFromStoreWorkflow,
                amazonRenderingContext
            ).toMutableList()
        )
        exampleWorkflow = OfficialWorkflowSamplesProvider.amazon()
        amazonStateMachineService = AmazonStateMachineService()
    }

    @Benchmark
    override fun benchmarkGeneratedWorkflowDeployment() {
        amazonStateMachineService.createStateMachine(
            ARN,
            REGION,
            TAGS,
            Constants.GENERATED_WORKFLOW_NAME,
            generatedWorkflow
        )
    }

    @Benchmark
    override fun benchmarkExampleWorkflowDeployment() {
        amazonStateMachineService.createStateMachine(
            ARN,
            REGION,
            TAGS,
            Constants.EXAMPLE_WORKFLOW_NAME,
            exampleWorkflow
        )
    }

    companion object {
        private val ARN: String = System.getenv(Constants.AWS_ROLE_ARN_ENV_VAR)
        private val REGION: String = System.getenv(Constants.AWS_REGION_ENV_VAR)
        private val TAGS: Map<String, String> = mapOf(
            Constants.ORIGIN_LABEL to Constants.OMNI_FLOW_NAME,
            Constants.CLOUD_LABEL to Constants.AMAZON,
            Constants.ENV_STR to Constants.ENVIRONMENT
        )
    }
}

