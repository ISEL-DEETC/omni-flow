package costaber.com.github.omniflow.cloud.provider.google.jackson

import com.fasterxml.jackson.core.ObjectCodec
import com.fasterxml.jackson.core.io.IOContext
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator
import com.fasterxml.jackson.dataformat.yaml.util.StringQuotingChecker
import org.yaml.snakeyaml.DumperOptions
import java.io.IOException
import java.io.Writer

class GoogleYAMLGenerator(
    ctxt: IOContext,
    jsonFeatures: Int,
    yamlFeatures: Int,
    quotingChecker: StringQuotingChecker,
    codec: ObjectCodec,
    out: Writer,
    dumperOptions: DumperOptions,
) : YAMLGenerator(ctxt, jsonFeatures, yamlFeatures, quotingChecker, codec, out, dumperOptions) {

    @Throws(IOException::class)
    override fun writeRawValue(text: String) {
        this._writeScalar(text, "string", DumperOptions.ScalarStyle.SINGLE_QUOTED)
    }
}
