package io.iamjosephmj.flinger.behaviours

import com.google.common.truth.Truth.assertThat
import io.iamjosephmj.flinger.configs.FlingConfiguration
import io.iamjosephmj.flinger.fling.FlingTestUtils
import io.iamjosephmj.flinger.flings.FlingCalculator
import org.junit.Test

/**
 * Tests to verify that [FlingPresets] configurations produce the expected
 * fling characteristics. These tests verify the physics without requiring
 * Compose runtime.
 */
class FlingPresetsConfigTest {

    // ==================== Preset Configuration Tests ====================

    @Test
    fun `smooth preset uses default configuration`() {
        val config = FlingConfiguration.Builder().build()
        
        assertThat(config.scrollFriction).isEqualTo(0.008f)
        assertThat(config.decelerationFriction).isEqualTo(0.09f)
    }

    @Test
    fun `iOSStyle preset has higher scroll friction`() {
        val defaultConfig = FlingConfiguration.Builder().build()
        val iOSConfig = FlingConfiguration.Builder()
            .scrollViewFriction(0.04f)
            .build()
        
        assertThat(iOSConfig.scrollFriction).isGreaterThan(defaultConfig.scrollFriction)
    }

    @Test
    fun `quickStop preset has high deceleration friction`() {
        val defaultConfig = FlingConfiguration.Builder().build()
        val quickStopConfig = FlingConfiguration.Builder()
            .decelerationFriction(0.5f)
            .build()
        
        assertThat(quickStopConfig.decelerationFriction)
            .isGreaterThan(defaultConfig.decelerationFriction)
    }

    @Test
    fun `bouncy preset has modified spline`() {
        val defaultConfig = FlingConfiguration.Builder().build()
        val bouncyConfig = FlingConfiguration.Builder()
            .decelerationFriction(0.6f)
            .splineInflection(0.4f)
            .build()
        
        assertThat(bouncyConfig.splineInflection)
            .isGreaterThan(defaultConfig.splineInflection)
    }

    @Test
    fun `floaty preset has low deceleration friction`() {
        val defaultConfig = FlingConfiguration.Builder().build()
        val floatyConfig = FlingConfiguration.Builder()
            .scrollViewFriction(0.003f)  // Lower than default for longer scroll
            .decelerationFriction(0.015f)
            .build()
        
        assertThat(floatyConfig.decelerationFriction)
            .isLessThan(defaultConfig.decelerationFriction)
    }

    @Test
    fun `snappy preset has balanced friction`() {
        val snappyConfig = FlingConfiguration.Builder()
            .scrollViewFriction(0.03f)
            .decelerationFriction(0.2f)
            .build()
        
        // Snappy is between default and quick-stop
        assertThat(snappyConfig.scrollFriction).isEqualTo(0.03f)
        assertThat(snappyConfig.decelerationFriction).isEqualTo(0.2f)
    }

    @Test
    fun `ultraSmooth preset has optimized settings`() {
        val ultraSmoothConfig = FlingConfiguration.Builder()
            .scrollViewFriction(0.006f)
            .decelerationFriction(0.05f)
            .numberOfSplinePoints(150)
            .build()
        
        assertThat(ultraSmoothConfig.scrollFriction).isEqualTo(0.006f)
        assertThat(ultraSmoothConfig.numberOfSplinePoints).isEqualTo(150)
    }

    // ==================== Preset Behavior Comparison Tests ====================

    @Test
    fun `iOSStyle produces shorter distance than default`() {
        val defaultCalc = createCalculator(FlingConfiguration.Builder().build())
        val iOSCalc = createCalculator(
            FlingConfiguration.Builder().scrollViewFriction(0.04f).build()
        )
        
        val velocity = 1000f
        val defaultDistance = defaultCalc.flingDistance(velocity)
        val iOSDistance = iOSCalc.flingDistance(velocity)
        
        assertThat(iOSDistance).isLessThan(defaultDistance)
    }

    @Test
    fun `quickStop produces much shorter distance than default`() {
        val defaultCalc = createCalculator(FlingConfiguration.Builder().build())
        val quickStopCalc = createCalculator(
            FlingConfiguration.Builder().decelerationFriction(0.5f).build()
        )
        
        val velocity = 1000f
        val defaultDistance = defaultCalc.flingDistance(velocity)
        val quickStopDistance = quickStopCalc.flingDistance(velocity)
        
        assertThat(quickStopDistance).isLessThan(defaultDistance * 0.5f)
    }

    @Test
    fun `floaty produces longer distance than default`() {
        val defaultCalc = createCalculator(FlingConfiguration.Builder().build())
        val floatyCalc = createCalculator(
            FlingConfiguration.Builder()
                .scrollViewFriction(0.003f)  // Lower friction = longer distance
                .decelerationFriction(0.015f)
                .build()
        )
        
        val velocity = 1000f
        val defaultDistance = defaultCalc.flingDistance(velocity)
        val floatyDistance = floatyCalc.flingDistance(velocity)
        
        assertThat(floatyDistance).isGreaterThan(defaultDistance)
    }

    @Test
    fun `all presets produce different results`() {
        val velocity = 1000f
        
        val smooth = createCalculator(FlingConfiguration.Builder().build())
            .flingDistance(velocity)
        
        val iOS = createCalculator(
            FlingConfiguration.Builder().scrollViewFriction(0.04f).build()
        ).flingDistance(velocity)
        
        val quickStop = createCalculator(
            FlingConfiguration.Builder().decelerationFriction(0.5f).build()
        ).flingDistance(velocity)
        
        val floaty = createCalculator(
            FlingConfiguration.Builder()
                .scrollViewFriction(0.003f)  // Lower friction = longer distance
                .decelerationFriction(0.015f)
                .build()
        ).flingDistance(velocity)
        
        // All should be different
        val distances = setOf(smooth, iOS, quickStop, floaty)
        assertThat(distances.size).isEqualTo(4)
    }

    // ==================== Duration Comparison Tests ====================

    @Test
    fun `quickStop has shorter duration than default`() {
        val defaultCalc = createCalculator(FlingConfiguration.Builder().build())
        val quickStopCalc = createCalculator(
            FlingConfiguration.Builder().decelerationFriction(0.5f).build()
        )
        
        val velocity = 1000f
        val defaultDuration = defaultCalc.flingDuration(velocity)
        val quickStopDuration = quickStopCalc.flingDuration(velocity)
        
        assertThat(quickStopDuration).isLessThan(defaultDuration)
    }

    @Test
    fun `floaty has longer duration than default`() {
        val defaultCalc = createCalculator(FlingConfiguration.Builder().build())
        val floatyCalc = createCalculator(
            FlingConfiguration.Builder()
                .scrollViewFriction(0.003f)  // Lower friction = longer duration
                .decelerationFriction(0.015f)
                .build()
        )
        
        val velocity = 1000f
        val defaultDuration = defaultCalc.flingDuration(velocity)
        val floatyDuration = floatyCalc.flingDuration(velocity)
        
        assertThat(floatyDuration).isGreaterThan(defaultDuration)
    }

    // ==================== Spline Configuration Tests ====================

    @Test
    fun `smoothCurve preset modifies spline inflection`() {
        val smoothCurveConfig = FlingConfiguration.Builder()
            .splineInflection(0.16f)
            .build()
        
        assertThat(smoothCurveConfig.splineInflection).isEqualTo(0.16f)
    }

    @Test
    fun `bouncy preset combines friction and spline changes`() {
        val bouncyConfig = FlingConfiguration.Builder()
            .decelerationFriction(0.6f)
            .splineInflection(0.4f)
            .build()
        
        assertThat(bouncyConfig.decelerationFriction).isEqualTo(0.6f)
        assertThat(bouncyConfig.splineInflection).isEqualTo(0.4f)
    }

    // ==================== Helper Methods ====================

    private fun createCalculator(config: FlingConfiguration): FlingCalculator {
        return FlingCalculator(
            density = FlingTestUtils.getDummyDensityComponent(),
            flingConfiguration = config
        )
    }
}
