package costaber.com.github.omniflow

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
        require(filePath.endsWith(".txt")) { "The file format must be TXT" }

        val opt = OptionsBuilder()
            .shouldDoGC(true)
            .resultFormat(ResultFormatType.TEXT)
            .result(filePath)
            .build()

        Runner(opt).run()
    }
}