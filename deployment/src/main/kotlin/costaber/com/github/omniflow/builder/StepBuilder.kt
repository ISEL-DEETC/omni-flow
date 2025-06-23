package costaber.com.github.omniflow.builder

import costaber.com.github.omniflow.model.Step

open class StepBuilder : Builder<Step> {

    private lateinit var name: String
    private lateinit var description: String
    private lateinit var contextBuilder: ContextBuilder
    private var next: String = ""

    fun name(value: String) = apply { this.name = value }

    fun description(value: String) = apply { this.description = value }

    fun context(value: ContextBuilder) = apply { this.contextBuilder = value }

    fun next(value: String) = apply { this.next = value }

    override fun build() = Step(
        name = name,
        description = description,
        type = contextBuilder.stepType(),
        context = contextBuilder.build(),
        next = next,
    )
}