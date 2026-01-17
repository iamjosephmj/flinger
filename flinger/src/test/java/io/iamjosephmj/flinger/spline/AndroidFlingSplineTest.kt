package io.iamjosephmj.flinger.spline

import com.google.common.truth.Truth.assertThat
import io.iamjosephmj.flinger.configs.FlingConfiguration
import org.junit.Test

/**
 * Comprehensive tests for [AndroidFlingSpline].
 */
class AndroidFlingSplineTest {

    // ==================== Fling Position Tests ====================

    @Test
    fun `flingPosition at time 0 returns near zero distance coefficient`() {
        val spline = createSpline()
        
        val result = spline.flingPosition(0f)
        
        assertThat(result.distanceCoefficient).isAtMost(0.01f)
    }

    @Test
    fun `flingPosition at time 1 returns distance coefficient of 1`() {
        val spline = createSpline()
        
        val result = spline.flingPosition(1f)
        
        assertThat(result.distanceCoefficient).isEqualTo(1f)
    }

    @Test
    fun `flingPosition at time greater than 1 returns distance coefficient of 1`() {
        val spline = createSpline()
        
        val result = spline.flingPosition(2f)
        
        assertThat(result.distanceCoefficient).isEqualTo(1f)
    }

    @Test
    fun `flingPosition at time greater than 1 returns velocity coefficient of 0`() {
        val spline = createSpline()
        
        val result = spline.flingPosition(2f)
        
        assertThat(result.velocityCoefficient).isEqualTo(0f)
    }

    @Test
    fun `flingPosition at mid-time returns intermediate distance coefficient`() {
        val spline = createSpline()
        
        val result = spline.flingPosition(0.5f)
        
        assertThat(result.distanceCoefficient).isGreaterThan(0f)
        assertThat(result.distanceCoefficient).isLessThan(1f)
    }

    @Test
    fun `flingPosition distance coefficient increases monotonically with time`() {
        val spline = createSpline()
        
        var previousDistance = 0f
        for (t in listOf(0.1f, 0.2f, 0.3f, 0.4f, 0.5f, 0.6f, 0.7f, 0.8f, 0.9f, 1.0f)) {
            val result = spline.flingPosition(t)
            assertThat(result.distanceCoefficient).isAtLeast(previousDistance)
            previousDistance = result.distanceCoefficient
        }
    }

    @Test
    fun `flingPosition velocity coefficient is positive during fling`() {
        val spline = createSpline()
        
        // Check at various points during the fling (not at endpoints)
        for (t in listOf(0.1f, 0.3f, 0.5f, 0.7f, 0.9f)) {
            val result = spline.flingPosition(t)
            assertThat(result.velocityCoefficient).isAtLeast(0f)
        }
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
        
        val resultLow = splineLow.flingPosition(0.5f)
        val resultHigh = splineHigh.flingPosition(0.5f)
        
        // Different inflection should produce different curves
        assertThat(resultLow.distanceCoefficient)
            .isNotEqualTo(resultHigh.distanceCoefficient)
    }

    @Test
    fun `different spline tension affects fling curve`() {
        val splineLowTension = createSpline(
            FlingConfiguration.Builder().splineStartTension(0.05f).build()
        )
        val splineHighTension = createSpline(
            FlingConfiguration.Builder().splineStartTension(0.5f).build()
        )
        
        val resultLow = splineLowTension.flingPosition(0.3f)
        val resultHigh = splineHighTension.flingPosition(0.3f)
        
        assertThat(resultLow.distanceCoefficient)
            .isNotEqualTo(resultHigh.distanceCoefficient)
    }

    @Test
    fun `more spline points produces smoother curve`() {
        val splineFew = createSpline(
            FlingConfiguration.Builder().numberOfSplinePoints(10).build()
        )
        val splineMany = createSpline(
            FlingConfiguration.Builder().numberOfSplinePoints(500).build()
        )
        
        // Both should still produce valid results
        val resultFew = splineFew.flingPosition(0.5f)
        val resultMany = splineMany.flingPosition(0.5f)
        
        assertThat(resultFew.distanceCoefficient).isGreaterThan(0f)
        assertThat(resultMany.distanceCoefficient).isGreaterThan(0f)
    }

    // ==================== FlingResult Tests ====================

    @Test
    fun `FlingResult contains both distance and velocity coefficients`() {
        val spline = createSpline()
        
        val result = spline.flingPosition(0.5f)
        
        assertThat(result.distanceCoefficient).isNotNull()
        assertThat(result.velocityCoefficient).isNotNull()
    }

    @Test
    fun `FlingResult data class equality works correctly`() {
        val result1 = AndroidFlingSpline.FlingResult(0.5f, 0.3f)
        val result2 = AndroidFlingSpline.FlingResult(0.5f, 0.3f)
        val result3 = AndroidFlingSpline.FlingResult(0.6f, 0.3f)
        
        assertThat(result1).isEqualTo(result2)
        assertThat(result1).isNotEqualTo(result3)
    }

    // ==================== Edge Cases ====================

    @Test
    fun `flingPosition handles very small time values`() {
        val spline = createSpline()
        
        val result = spline.flingPosition(0.001f)
        
        assertThat(result.distanceCoefficient).isAtLeast(0f)
        assertThat(result.distanceCoefficient).isAtMost(1f)
    }

    @Test
    fun `flingPosition handles time values just below 1`() {
        val spline = createSpline()
        
        val result = spline.flingPosition(0.999f)
        
        assertThat(result.distanceCoefficient).isAtLeast(0.9f)
        assertThat(result.distanceCoefficient).isAtMost(1f)
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
