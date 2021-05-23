package io.iamjosephmj.flinger.fling

import androidx.compose.ui.unit.Density
import io.iamjosephmj.flinger.configs.FlingConfiguration
import io.iamjosephmj.flinger.flings.FlingCalculator

object FlingTestUtils {

    fun getFlingCalculatorObject(
        density: Density,
        flingConfiguration: FlingConfiguration
    ): FlingCalculator {
        return FlingCalculator(
            density,
            flingConfiguration
        )
    }

    fun getDefaultScrollConfiguration(): FlingConfiguration {
        return FlingConfiguration.Builder()
            .build()
    }

    fun getDummyDensityComponent(): DummyDensitySimulator {
        return DummyDensitySimulator()
    }

}