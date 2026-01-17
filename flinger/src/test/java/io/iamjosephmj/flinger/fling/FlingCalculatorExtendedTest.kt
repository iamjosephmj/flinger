package io.iamjosephmj.flinger.fling

import com.google.common.truth.Truth.assertThat
import io.iamjosephmj.flinger.configs.FlingConfiguration
import io.iamjosephmj.flinger.flings.FlingCalculator
import org.junit.Test

/**
 * Extended tests for [FlingCalculator] covering edge cases and behavior verification.
 */
class FlingCalculatorExtendedTest {

    // ==================== Fling Distance Tests ====================

    @Test
    fun `flingDistance increases with higher velocity`() {
        val calculator = createCalculator()
        
        val distanceLow = calculator.flingDistance(100f)
        val distanceHigh = calculator.flingDistance(1000f)
        
        assertThat(distanceHigh).isGreaterThan(distanceLow)
    }

    @Test
    fun `flingDistance is positive for positive velocity`() {
        val calculator = createCalculator()
        
        val distance = calculator.flingDistance(500f)
        
        assertThat(distance).isGreaterThan(0f)
    }

    @Test
    fun `flingDistance is positive for negative velocity`() {
        val calculator = createCalculator()
        
        // flingDistance returns absolute distance regardless of direction
        val distance = calculator.flingDistance(-500f)
        
        assertThat(distance).isGreaterThan(0f)
    }

    @Test
    fun `flingDistance scales with velocity magnitude`() {
        val calculator = createCalculator()
        
        val distance1x = calculator.flingDistance(100f)
        val distance2x = calculator.flingDistance(200f)
        val distance4x = calculator.flingDistance(400f)
        
        // Higher velocity should result in longer distance
        assertThat(distance2x).isGreaterThan(distance1x)
        assertThat(distance4x).isGreaterThan(distance2x)
    }

    @Test
    fun `flingDistance affected by scroll friction`() {
        val lowFriction = createCalculator(
            FlingConfiguration.Builder().scrollViewFriction(0.001f).build()
        )
        val highFriction = createCalculator(
            FlingConfiguration.Builder().scrollViewFriction(0.1f).build()
        )
        
        val distanceLowFriction = lowFriction.flingDistance(1000f)
        val distanceHighFriction = highFriction.flingDistance(1000f)
        
        // Lower friction should result in longer distance
        assertThat(distanceLowFriction).isGreaterThan(distanceHighFriction)
    }

    // ==================== Fling Duration Tests ====================

    @Test
    fun `flingDuration increases with higher velocity`() {
        val calculator = createCalculator()
        
        val durationLow = calculator.flingDuration(100f)
        val durationHigh = calculator.flingDuration(1000f)
        
        assertThat(durationHigh).isGreaterThan(durationLow)
    }

    @Test
    fun `flingDuration is positive for positive velocity`() {
        val calculator = createCalculator()
        
        val duration = calculator.flingDuration(500f)
        
        assertThat(duration).isGreaterThan(0)
    }

    @Test
    fun `flingDuration is positive for negative velocity`() {
        val calculator = createCalculator()
        
        val duration = calculator.flingDuration(-500f)
        
        assertThat(duration).isGreaterThan(0)
    }

    @Test
    fun `flingDuration same for positive and negative velocity of same magnitude`() {
        val calculator = createCalculator()
        
        val durationPositive = calculator.flingDuration(500f)
        val durationNegative = calculator.flingDuration(-500f)
        
        assertThat(durationPositive).isEqualTo(durationNegative)
    }

    @Test
    fun `flingDuration affected by deceleration friction`() {
        val lowFriction = createCalculator(
            FlingConfiguration.Builder().decelerationFriction(0.01f).build()
        )
        val highFriction = createCalculator(
            FlingConfiguration.Builder().decelerationFriction(0.5f).build()
        )
        
        val durationLowFriction = lowFriction.flingDuration(1000f)
        val durationHighFriction = highFriction.flingDuration(1000f)
        
        // Lower friction should result in longer duration
        assertThat(durationLowFriction).isGreaterThan(durationHighFriction)
    }

    // ==================== Fling Info Tests ====================

    @Test
    fun `flingInfo returns consistent distance with flingDistance`() {
        val calculator = createCalculator()
        val velocity = 1000f
        
        val distance = calculator.flingDistance(velocity)
        val info = calculator.flingInfo(velocity)
        
        assertThat(info.distance).isEqualTo(distance)
    }

    @Test
    fun `flingInfo returns consistent duration with flingDuration`() {
        val calculator = createCalculator()
        val velocity = 1000f
        
        val duration = calculator.flingDuration(velocity)
        val info = calculator.flingInfo(velocity)
        
        assertThat(info.duration).isEqualTo(duration)
    }

    @Test
    fun `flingInfo stores initial velocity correctly`() {
        val calculator = createCalculator()
        val velocity = 1234f
        
        val info = calculator.flingInfo(velocity)
        
        assertThat(info.initialVelocity).isEqualTo(velocity)
    }

    @Test
    fun `flingInfo position at time 0 returns near 0`() {
        val calculator = createCalculator()
        val info = calculator.flingInfo(1000f)
        
        val position = info.position(0)
        
        assertThat(position).isAtMost(1f)
    }

    @Test
    fun `flingInfo position at end of duration returns near total distance`() {
        val calculator = createCalculator()
        val velocity = 1000f
        val info = calculator.flingInfo(velocity)
        
        val position = info.position(info.duration)
        
        // Position at end should be close to total distance (with direction sign)
        assertThat(kotlin.math.abs(position)).isWithin(info.distance * 0.1f).of(info.distance)
    }

    @Test
    fun `flingInfo position increases over time for positive velocity`() {
        val calculator = createCalculator()
        val info = calculator.flingInfo(1000f)
        
        val positionEarly = info.position(info.duration / 4)
        val positionMid = info.position(info.duration / 2)
        val positionLate = info.position(3 * info.duration / 4)
        
        assertThat(positionMid).isGreaterThan(positionEarly)
        assertThat(positionLate).isGreaterThan(positionMid)
    }

    @Test
    fun `flingInfo velocity decreases over time`() {
        val calculator = createCalculator()
        val info = calculator.flingInfo(1000f)
        
        val velocityEarly = info.velocity(info.duration / 4)
        val velocityMid = info.velocity(info.duration / 2)
        val velocityLate = info.velocity(3 * info.duration / 4)
        
        assertThat(kotlin.math.abs(velocityMid)).isLessThan(kotlin.math.abs(velocityEarly))
        assertThat(kotlin.math.abs(velocityLate)).isLessThan(kotlin.math.abs(velocityMid))
    }

    @Test
    fun `flingInfo velocity at end of duration is near zero`() {
        val calculator = createCalculator()
        val info = calculator.flingInfo(1000f)
        
        val velocityAtEnd = info.velocity(info.duration)
        
        // Velocity should be very small at the end
        assertThat(kotlin.math.abs(velocityAtEnd)).isLessThan(100f)
    }

    // ==================== Different Configurations ====================

    @Test
    fun `calculator with iOS-style config produces different results`() {
        val defaultCalculator = createCalculator()
        val iOSCalculator = createCalculator(
            FlingConfiguration.Builder()
                .scrollViewFriction(0.04f)
                .build()
        )
        
        val velocity = 1000f
        val defaultDistance = defaultCalculator.flingDistance(velocity)
        val iOSDistance = iOSCalculator.flingDistance(velocity)
        
        assertThat(defaultDistance).isNotEqualTo(iOSDistance)
    }

    @Test
    fun `calculator with quick-stop config has shorter distance`() {
        val defaultCalculator = createCalculator()
        val quickStopCalculator = createCalculator(
            FlingConfiguration.Builder()
                .decelerationFriction(0.5f)
                .build()
        )
        
        val velocity = 1000f
        val defaultDistance = defaultCalculator.flingDistance(velocity)
        val quickStopDistance = quickStopCalculator.flingDistance(velocity)
        
        assertThat(quickStopDistance).isLessThan(defaultDistance)
    }

    @Test
    fun `calculator with floaty config has longer distance`() {
        val defaultCalculator = createCalculator()
        val floatyCalculator = createCalculator(
            FlingConfiguration.Builder()
                .decelerationFriction(0.015f)
                .build()
        )
        
        val velocity = 1000f
        val defaultDistance = defaultCalculator.flingDistance(velocity)
        val floatyDistance = floatyCalculator.flingDistance(velocity)
        
        assertThat(floatyDistance).isGreaterThan(defaultDistance)
    }

    // ==================== Edge Cases ====================

    @Test
    fun `flingDistance handles very small velocity`() {
        val calculator = createCalculator()
        
        val distance = calculator.flingDistance(1f)
        
        assertThat(distance).isAtLeast(0f)
        assertThat(distance).isFinite()
    }

    @Test
    fun `flingDistance handles very large velocity`() {
        val calculator = createCalculator()
        
        val distance = calculator.flingDistance(10000f)
        
        assertThat(distance).isGreaterThan(0f)
        assertThat(distance).isFinite()
    }

    @Test
    fun `flingDuration handles very small velocity`() {
        val calculator = createCalculator()
        
        val duration = calculator.flingDuration(1f)
        
        assertThat(duration).isAtLeast(0)
    }

    @Test
    fun `flingDuration handles very large velocity`() {
        val calculator = createCalculator()
        
        val duration = calculator.flingDuration(10000f)
        
        assertThat(duration).isGreaterThan(0)
    }

    // ==================== Density Effects ====================

    @Test
    fun `calculator uses provided density`() {
        val density = FlingTestUtils.getDummyDensityComponent()
        val config = FlingConfiguration.Builder().build()
        val calculator = FlingCalculator(density, config)
        
        assertThat(calculator.density).isEqualTo(density)
    }

    @Test
    fun `calculator uses provided configuration`() {
        val density = FlingTestUtils.getDummyDensityComponent()
        val config = FlingConfiguration.Builder()
            .scrollViewFriction(0.05f)
            .build()
        val calculator = FlingCalculator(density, config)
        
        assertThat(calculator.flingConfiguration).isEqualTo(config)
    }

    // ==================== Helper Methods ====================

    private fun createCalculator(
        config: FlingConfiguration = FlingConfiguration.Builder().build()
    ): FlingCalculator {
        return FlingCalculator(
            density = FlingTestUtils.getDummyDensityComponent(),
            flingConfiguration = config
        )
    }
}
