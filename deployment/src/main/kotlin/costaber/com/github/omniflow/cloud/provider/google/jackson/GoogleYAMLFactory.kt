package costaber.com.github.omniflow.cloud.provider.google.jackson

import com.fasterxml.jackson.core.io.IOContext
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator
import org.yaml.snakeyaml.DumperOptions
import org.yaml.snakeyaml.DumperOptions.FlowStyle
import org.yaml.snakeyaml.DumperOptions.LineBreak
import java.io.IOException
import java.io.Writer

class GoogleYAMLFactory : YAMLFactory() {
    companion object {
        val default: YAMLFactory = GoogleYAMLFactory()
            .disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER)
            .enable(YAMLGenerator.Feature.MINIMIZE_QUOTES)
            .disable(YAMLGenerator.Feature.ALWAYS_QUOTE_NUMBERS_AS_STRINGS)
            .enable(YAMLGenerator.Feature.INDENT_ARRAYS_WITH_INDICATOR)
    }

    private fun buildDumperOptions(
        yamlFeatures: Int,
        version: DumperOptions.Version?
    ): DumperOptions {
        val opt = DumperOptions()
        if (YAMLGenerator.Feature.CANONICAL_OUTPUT.enabledIn(yamlFeatures)) {
            opt.isCanonical = true
        } else {
            opt.isCanonical = false
            opt.defaultFlowStyle = FlowStyle.BLOCK
        }

        opt.splitLines = YAMLGenerator.Feature.SPLIT_LINES.enabledIn(yamlFeatures)
        if (YAMLGenerator.Feature.INDENT_ARRAYS.enabledIn(yamlFeatures)) {
            opt.indicatorIndent = 1
            opt.indent = 2
        }

        if (YAMLGenerator.Feature.INDENT_ARRAYS_WITH_INDICATOR.enabledIn(yamlFeatures)) {
            opt.indicatorIndent = 2
            opt.indentWithIndicator = true
        }

        if (YAMLGenerator.Feature.USE_PLATFORM_LINE_BREAKS.enabledIn(yamlFeatures)) {
            opt.lineBreak = LineBreak.getPlatformLineBreak()
        }

        if (YAMLGenerator.Feature.ALLOW_LONG_KEYS.enabledIn(yamlFeatures)) {
            opt.maxSimpleKeyLength = 1024
        }

        opt.version = version

        return opt
    }

    // You MUST override _createGenerator to return your custom generator
    @Throws(IOException::class)
    override fun _createGenerator(out: Writer, ctxt: IOContext): YAMLGenerator {
        val feats = this._yamlGeneratorFeatures
        return if (this._dumperOptions == null) GoogleYAMLGenerator(
            ctxt,
            this._generatorFeatures,
            feats,
            this._quotingChecker,
            this._objectCodec,
            out,
            buildDumperOptions(feats, this._version)
        ) else GoogleYAMLGenerator(
            ctxt,
            this._generatorFeatures,
            feats,
            this._quotingChecker,
            this._objectCodec,
            out,
            this._dumperOptions
        )
    }
}

