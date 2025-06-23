package costaber.com.github.omniflow.provider

import costaber.com.github.omniflow.util.FileReader
import java.io.IOException

object OfficialWorkflowSamplesProvider {
    @Throws(IOException::class)
    fun google(): String {
        val fileReader = FileReader()
        return fileReader.readFileFromResources("samples/googleWorkflowSample.yml")
    }

    @Throws(IOException::class)
    fun amazon(): String {
        val fileReader = FileReader()
        return fileReader.readFileFromResources("samples/amazonWorkflowSample.json")
    }
}
