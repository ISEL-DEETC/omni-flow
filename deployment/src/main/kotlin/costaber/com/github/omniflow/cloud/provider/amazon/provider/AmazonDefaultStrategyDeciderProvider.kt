package costaber.com.github.omniflow.cloud.provider.amazon.provider

import costaber.com.github.omniflow.cloud.provider.amazon.strategy.AmazonBranchStrategyFactory
import costaber.com.github.omniflow.cloud.provider.amazon.strategy.AmazonCallStrategyFactory
import costaber.com.github.omniflow.cloud.provider.amazon.strategy.AmazonChoiceStrategyFactory
import costaber.com.github.omniflow.cloud.provider.amazon.strategy.AmazonConditionStrategyFactory
import costaber.com.github.omniflow.cloud.provider.amazon.strategy.AmazonEqualToExpressionStrategyFactory
import costaber.com.github.omniflow.cloud.provider.amazon.strategy.AmazonGreaterThanExpressionStrategyFactory
import costaber.com.github.omniflow.cloud.provider.amazon.strategy.AmazonGreaterThanOrEqualExpressionStrategyFactory
import costaber.com.github.omniflow.cloud.provider.amazon.strategy.AmazonIterationStrategyFactory
import costaber.com.github.omniflow.cloud.provider.amazon.strategy.AmazonLessThanExpressionStrategyFactory
import costaber.com.github.omniflow.cloud.provider.amazon.strategy.AmazonLessThanOrEqualExpressionStrategyFactory
import costaber.com.github.omniflow.cloud.provider.amazon.strategy.AmazonParallelStrategyFactory
import costaber.com.github.omniflow.cloud.provider.amazon.strategy.AmazonPassStrategyFactory
import costaber.com.github.omniflow.cloud.provider.amazon.strategy.AmazonStateMachineStrategyFactory
import costaber.com.github.omniflow.cloud.provider.amazon.strategy.AmazonStateStrategyFactory
import costaber.com.github.omniflow.cloud.provider.amazon.strategy.AmazonVariableStrategyFactory
import costaber.com.github.omniflow.factory.DefaultNodeRendererStrategyDecider
import costaber.com.github.omniflow.factory.NodeRendererStrategyDecider

object AmazonDefaultStrategyDeciderProvider {
    fun createNodeRendererStrategyDecider(): NodeRendererStrategyDecider {
        return DefaultNodeRendererStrategyDecider.Builder()
            .addRendererStrategy(AmazonChoiceStrategyFactory())
            .addRendererStrategy(AmazonConditionStrategyFactory())
            .addRendererStrategy(AmazonEqualToExpressionStrategyFactory())
            .addRendererStrategy(AmazonGreaterThanExpressionStrategyFactory())
            .addRendererStrategy(AmazonGreaterThanOrEqualExpressionStrategyFactory())
            .addRendererStrategy(AmazonLessThanExpressionStrategyFactory())
            .addRendererStrategy(AmazonLessThanOrEqualExpressionStrategyFactory())
            .addRendererStrategy(AmazonPassStrategyFactory())
            .addRendererStrategy(AmazonStateMachineStrategyFactory())
            .addRendererStrategy(AmazonStateStrategyFactory())
            .addRendererStrategy(AmazonCallStrategyFactory())
            .addRendererStrategy(AmazonVariableStrategyFactory())
            .addRendererStrategy(AmazonParallelStrategyFactory())
            .addRendererStrategy(AmazonBranchStrategyFactory())
            .addRendererStrategy(AmazonIterationStrategyFactory())
            .build()
    }
}