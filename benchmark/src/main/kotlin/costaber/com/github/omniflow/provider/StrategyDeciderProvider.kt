package costaber.com.github.omniflow.provider

import costaber.com.github.omniflow.cloud.provider.amazon.strategy.*
import costaber.com.github.omniflow.cloud.provider.google.strategy.*
import costaber.com.github.omniflow.factory.DefaultNodeRendererStrategyDecider
import costaber.com.github.omniflow.factory.NodeRendererStrategyDecider

object StrategyDeciderProvider {
    fun googleNodeRendererStrategyDecider(): NodeRendererStrategyDecider {
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
            .build()
    }

    fun amazonNodeRendererStrategyDecider(): NodeRendererStrategyDecider {
        return DefaultNodeRendererStrategyDecider.Builder()
            .addRendererStrategy(AmazonChoiceStrategyFactory())
            .addRendererStrategy(AmazonConditionStrategyFactory())
            .addRendererStrategy(AmazonEqualToExpressionStrategyFactory())
            .addRendererStrategy(AmazonGreaterThanExpressionStrategyFactory())
            .addRendererStrategy(AmazonGreaterThanOrEqualExpressionStrategyFactory())
            .addRendererStrategy(AmazonLessThanExpressionStrategyFactory())
            .addRendererStrategy(AmazonLessThanOrEqualExpressionStrategyFactory())
            .addRendererStrategy(AmazonNotEqualToExpressionStrategyFactory())
            .addRendererStrategy(AmazonPassStrategyFactory())
            .addRendererStrategy(AmazonStateMachineStrategyFactory())
            .addRendererStrategy(AmazonStateStrategyFactory())
            .addRendererStrategy(AmazonTaskStrategyFactory())
            .addRendererStrategy(AmazonVariableStrategyFactory())
            .build()
    }
}