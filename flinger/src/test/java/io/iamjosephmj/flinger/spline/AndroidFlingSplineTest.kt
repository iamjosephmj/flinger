package io.iamjosephmj.flinger.spline

import com.google.common.truth.Truth.assertThat
import io.iamjosephmj.flinger.configs.FlingConfiguration
import org.junit.Test

/**
 * Comprehensive tests for [AndroidFlingSpline].
 */
class AndroidFlingSplineTest {

    // ==================== Distance Coefficient Tests ====================

    @Test
    fun `flingDistanceCoefficient at time 0 returns near zero`() {
        val spline = createSpline()
        
        val distance = spline.flingDistanceCoefficient(0f)
        
        assertThat(distance).isAtMost(0.01f)
    }

    @Test
    fun `flingDistanceCoefficient at time 1 returns 1`() {
        val spline = createSpline()
        
        val distance = spline.flingDistanceCoefficient(1f)
        
        assertThat(distance).isEqualTo(1f)
    }

    @Test
    fun `flingDistanceCoefficient at time greater than 1 returns 1`() {
        val spline = createSpline()
        
        val distance = spline.flingDistanceCoefficient(2f)
        
        assertThat(distance).isEqualTo(1f)
    }

    @Test
    fun `flingDistanceCoefficient at mid-time returns intermediate value`() {
        val spline = createSpline()
        
        val distance = spline.flingDistanceCoefficient(0.5f)
        
        assertThat(distance).isGreaterThan(0f)
        assertThat(distance).isLessThan(1f)
    }

    @Test
    fun `flingDistanceCoefficient increases monotonically with time`() {
        val spline = createSpline()
        
        var previousDistance = 0f
        for (t in listOf(0.1f, 0.2f, 0.3f, 0.4f, 0.5f, 0.6f, 0.7f, 0.8f, 0.9f, 1.0f)) {
            val distance = spline.flingDistanceCoefficient(t)
            assertThat(distance).isAtLeast(previousDistance)
            previousDistance = distance
        }
    }

    // ==================== Velocity Coefficient Tests ====================

    @Test
    fun `flingVelocityCoefficient at time greater than 1 returns 0`() {
        val spline = createSpline()
        
        val velocity = spline.flingVelocityCoefficient(2f)
        
        assertThat(velocity).isEqualTo(0f)
    }

    @Test
    fun `flingVelocityCoefficient is positive during fling`() {
        val spline = createSpline()
        
        // Check at various points during the fling (not at endpoints)
        for (t in listOf(0.1f, 0.3f, 0.5f, 0.7f, 0.9f)) {
            val velocity = spline.flingVelocityCoefficient(t)
            assertThat(velocity).isAtLeast(0f)
        }
    }

    @Test
    fun `flingVelocityCoefficient decreases as time approaches 1`() {
        val spline = createSpline()
        
        val velocityEarly = spline.flingVelocityCoefficient(0.2f)
        val velocityLate = spline.flingVelocityCoefficient(0.9f)
        
        // Velocity should generally decrease as the fling decelerates
        assertThat(velocityEarly).isGreaterThan(velocityLate)
    }

    // ==================== Deceleration Tests ====================

    @Test
    fun `deceleration returns finite value for positive velocity`() {
        val spline = createSpline()
        
        val decel = spline.deceleration(1000f, 0.09f)
        
        assertThat(decel).isFinite()
    }

    @Test
    fun `deceleration returns finite value for negative velocity`() {
        val spline = createSpline()
        
        val decel = spline.deceleration(-1000f, 0.09f)
        
        assertThat(decel).isFinite()
    }

    @Test
    fun `deceleration increases with higher velocity`() {
        val spline = createSpline()
        
        val decelLow = spline.deceleration(100f, 0.09f)
        val decelHigh = spline.deceleration(1000f, 0.09f)
        
        assertThat(decelHigh).isGreaterThan(decelLow)
    }

    @Test
    fun `deceleration is same for positive and negative velocity of same magnitude`() {
        val spline = createSpline()
        
        val decelPositive = spline.deceleration(500f, 0.09f)
        val decelNegative = spline.deceleration(-500f, 0.09f)
        
        assertThat(decelPositive).isEqualTo(decelNegative)
    }

    @Test
    fun `deceleration increases with lower friction`() {
        val spline = createSpline()
        
        val decelHighFriction = spline.deceleration(1000f, 0.5f)
        val decelLowFriction = spline.deceleration(1000f, 0.01f)
        
        assertThat(decelLowFriction).isGreaterThan(decelHighFriction)
    }

    // ==================== Configuration Effects ====================

    @Test
    fun `different spline inflection affects fling curve`() {
        val splineLow = createSpline(
            FlingConfiguration.Builder().splineInflection(0.05f).build()
        )
        val splineHigh = createSpline(
            FlingConfiguration.Builder().splineInflection(0.3f).build()
        )
        
        val distanceLow = splineLow.flingDistanceCoefficient(0.5f)
        val distanceHigh = splineHigh.flingDistanceCoefficient(0.5f)
        
        // Different inflection should produce different curves
        assertThat(distanceLow).isNotEqualTo(distanceHigh)
    }

    @Test
    fun `different spline tension affects fling curve`() {
        val splineLowTension = createSpline(
            FlingConfiguration.Builder().splineStartTension(0.05f).build()
        )
        val splineHighTension = createSpline(
            FlingConfiguration.Builder().splineStartTension(0.5f).build()
        )
        
        val distanceLow = splineLowTension.flingDistanceCoefficient(0.3f)
        val distanceHigh = splineHighTension.flingDistanceCoefficient(0.3f)
        
        assertThat(distanceLow).isNotEqualTo(distanceHigh)
    }

    @Test
    fun `more spline points produces valid results`() {
        val splineFew = createSpline(
            FlingConfiguration.Builder().numberOfSplinePoints(10).build()
        )
        val splineMany = createSpline(
            FlingConfiguration.Builder().numberOfSplinePoints(500).build()
        )
        
        // Both should still produce valid results
        val distanceFew = splineFew.flingDistanceCoefficient(0.5f)
        val distanceMany = splineMany.flingDistanceCoefficient(0.5f)
        
        assertThat(distanceFew).isGreaterThan(0f)
        assertThat(distanceMany).isGreaterThan(0f)
    }

    // ==================== Consistency Tests ====================

    @Test
    fun `distance and velocity coefficients are consistent`() {
        val spline = createSpline()
        
        // At time 0.5, both should be valid
        val distance = spline.flingDistanceCoefficient(0.5f)
        val velocity = spline.flingVelocityCoefficient(0.5f)
        
        assertThat(distance).isGreaterThan(0f)
        assertThat(distance).isLessThan(1f)
        assertThat(velocity).isGreaterThan(0f)
    }

    @Test
    fun `coefficients are reproducible with same input`() {
        val spline = createSpline()
        
        val distance1 = spline.flingDistanceCoefficient(0.5f)
        val distance2 = spline.flingDistanceCoefficient(0.5f)
        val velocity1 = spline.flingVelocityCoefficient(0.5f)
        val velocity2 = spline.flingVelocityCoefficient(0.5f)
        
        assertThat(distance1).isEqualTo(distance2)
        assertThat(velocity1).isEqualTo(velocity2)
    }

    // ==================== Edge Cases ====================

    @Test
    fun `flingDistanceCoefficient handles very small time values`() {
        val spline = createSpline()
        
        val distance = spline.flingDistanceCoefficient(0.001f)
        
        assertThat(distance).isAtLeast(0f)
        assertThat(distance).isAtMost(1f)
    }

    @Test
    fun `flingDistanceCoefficient handles time values just below 1`() {
        val spline = createSpline()
        
        val distance = spline.flingDistanceCoefficient(0.999f)
        
        assertThat(distance).isAtLeast(0.9f)
        assertThat(distance).isAtMost(1f)
    }

    @Test
    fun `flingVelocityCoefficient handles very small time values`() {
        val spline = createSpline()
        
        val velocity = spline.flingVelocityCoefficient(0.001f)
        
        assertThat(velocity).isAtLeast(0f)
    }

    @Test
    fun `deceleration handles very small velocity`() {
        val spline = createSpline()
        
        val decel = spline.deceleration(0.001f, 0.09f)
        
        assertThat(decel).isFinite()
    }

    @Test
    fun `deceleration handles very large velocity`() {
        val spline = createSpline()
        
        val decel = spline.deceleration(100000f, 0.09f)
        
        assertThat(decel).isFinite()
    }

    // ==================== Helper Methods ====================

    private fun createSpline(
        config: FlingConfiguration = FlingConfiguration.Builder().build()
    ): AndroidFlingSpline {
        return AndroidFlingSpline(config)
    }
}
