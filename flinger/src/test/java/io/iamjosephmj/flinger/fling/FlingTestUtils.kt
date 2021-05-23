package io.iamjosephmj.flinger.fling

import androidx.compose.ui.unit.Density
import io.iamjosephmj.flinger.configs.FlingConfiguration
import io.iamjosephmj.flinger.flings.FlingCalculator
import org.mockito.Mockito

object FlingTestUtils {

    fun mockDensity(density: Density) {
        Mockito.`when`(density.density).thenReturn(221f)
    }

    fun getFlingCalculatorObject(
        density: Density,
        flingConfiguration: FlingConfiguration
    ): FlingCalculator {
        return FlingCalculator(
            density,
            getDefaultScrollConfiguration()
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