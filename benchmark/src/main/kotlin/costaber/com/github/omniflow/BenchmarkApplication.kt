package costaber.com.github.omniflow

import org.openjdk.jmh.profile.JavaFlightRecorderProfiler
import org.openjdk.jmh.results.format.ResultFormatType
import org.openjdk.jmh.runner.Runner
import org.openjdk.jmh.runner.RunnerException
import org.openjdk.jmh.runner.options.OptionsBuilder

object BenchmarkApplication {
    @Throws(RunnerException::class)
    @JvmStatic
    fun main(args: Array<String>) {
        require(args.isNotEmpty()) { "Argument corresponding to the file path is missing" }

        val filePath = args[0]
        require(filePath.endsWith(".json")) { "The file format must be json" }

        val opt = OptionsBuilder()
            .addProfiler(JavaFlightRecorderProfiler::class.java)
            .shouldDoGC(true)
            .resultFormat(ResultFormatType.JSON)
            .result(filePath)
            .build()

        Runner(opt).run()
    }
}