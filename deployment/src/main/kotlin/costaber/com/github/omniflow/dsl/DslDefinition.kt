package costaber.com.github.omniflow.dsl

import costaber.com.github.omniflow.builder.*
import costaber.com.github.omniflow.model.Value
import costaber.com.github.omniflow.model.Variable
import costaber.com.github.omniflow.model.Workflow

fun workflow(workflowBuilder: WorkflowBuilder.() -> Unit): Workflow {
    return WorkflowBuilder().apply(workflowBuilder).build()
}

fun step(stepBuilder: StepBuilder.() -> Unit): StepBuilder {
    return StepBuilder().apply(stepBuilder)
}

fun call(callContextBuilder: CallContextBuilder.() -> Unit): CallContextBuilder {
    return CallContextBuilder().apply(callContextBuilder)
}

fun authentication(authenticationBuilder: AuthenticationBuilder.() -> Unit): AuthenticationBuilder {
    return AuthenticationBuilder().apply(authenticationBuilder)
}

fun assign(assignContextBuilder: AssignContextBuilder.() -> Unit): AssignContextBuilder {
    return AssignContextBuilder().apply(assignContextBuilder)
}

fun variable(name: String): Variable = Variable(name)

fun <T : Any> value(value: T): Value<T> = Value(value)

fun switch(switchContextBuilder: SwitchContextBuilder.() -> Unit): SwitchContextBuilder {
    return SwitchContextBuilder().apply(switchContextBuilder)
}

fun condition(switchConditionBuilder: SwitchConditionBuilder.() -> Unit): SwitchConditionBuilder {
    return SwitchConditionBuilder().apply(switchConditionBuilder)
}

fun parallel(parallelContextBuilder: ParallelContextBuilder.() -> Unit): ParallelContextBuilder {
    return ParallelContextBuilder().apply(parallelContextBuilder)
}

fun iteration(iterationContextBuilder: IterationContextBuilder.() -> Unit): IterationContextBuilder {
    return IterationContextBuilder().apply(iterationContextBuilder)
}

fun branch(branchContextBuilder: BranchContextBuilder.() -> Unit): BranchContextBuilder {
    return BranchContextBuilder().apply(branchContextBuilder)
}
