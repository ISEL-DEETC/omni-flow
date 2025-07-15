package costaber.com.github.omniflow.cloud.provider.google.provider

import costaber.com.github.omniflow.cloud.provider.google.strategy.*
import costaber.com.github.omniflow.factory.DefaultNodeRendererStrategyDecider
import costaber.com.github.omniflow.factory.NodeRendererStrategyDecider

object GoogleDefaultStrategyDeciderProvider {
    fun createNodeRendererStrategyDecider(): NodeRendererStrategyDecider {
        return DefaultNodeRendererStrategyDecider.Builder()
            .addRendererStrategy(GoogleAssignStrategyFactory())
            .addRendererStrategy(GoogleCallStrategyFactory())
            .addRendererStrategy(GoogleConditionStrategyFactory())
            .addRendererStrategy(GoogleEqualToExpressionStrategyFactory())
            .addRendererStrategy(GoogleGreaterThanExpressionStrategyFactory())
            .addRendererStrategy(GoogleGreaterThanOrEqualExpressionStrategyFactory())
            .addRendererStrategy(GoogleLessThanExpressionStrategyFactory())
            .addRendererStrategy(GoogleLessThanOrEqualExpressionStrategyFactory())
            .addRendererStrategy(GoogleNotEqualToExpressionStrategyFactory())
            .addRendererStrategy(GoogleStepStrategyFactory())
            .addRendererStrategy(GoogleSwitchStrategyFactory())
            .addRendererStrategy(GoogleVariableStrategyFactory())
            .addRendererStrategy(GoogleWorkflowStrategyFactory())
            .addRendererStrategy(GoogleParallelStrategyFactory())
            .addRendererStrategy(GoogleBranchStrategyFactory())
            .addRendererStrategy(GoogleIterationStrategyFactory())
            .build()
    }
}