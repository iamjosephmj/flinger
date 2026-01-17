package io.iamjosephmj.flinger.fling

import com.google.common.truth.Truth.assertThat
import org.junit.Test


class FlingCalculatorTest {

    @Test
    fun `test fling distance success`() {
        val config = FlingTestUtils.getDefaultScrollConfiguration()
        val density = FlingTestUtils.getDummyDensityComponent()
        val flingCalculator = FlingTestUtils.getFlingCalculatorObject(
            density = density,
            flingConfiguration = config
        )

        assertThat(flingCalculator.flingDistance(5f)).isEqualTo(0.0033697102f)
    }

    @Test
    fun `test fling distance failed`() {
        val config = FlingTestUtils.getDefaultScrollConfiguration()
        val density = FlingTestUtils.getDummyDensityComponent()
        val flingCalculator = FlingTestUtils.getFlingCalculatorObject(
            density = density,
            flingConfiguration = config
        )

        assertThat(flingCalculator.flingDistance(21f)).isNotEqualTo(5f)
    }

    @Test
    fun `test fling duration pass`() {
        val config = FlingTestUtils.getDefaultScrollConfiguration()
        val density = FlingTestUtils.getDummyDensityComponent()
        val flingCalculator = FlingTestUtils.getFlingCalculatorObject(
            density = density,
            flingConfiguration = config
        )

        assertThat(flingCalculator.flingDuration(5f)).isEqualTo(6)
    }


    @Test
    fun `test fling duration fail`() {
        val config = FlingTestUtils.getDefaultScrollConfiguration()
        val density = FlingTestUtils.getDummyDensityComponent()
        val flingCalculator = FlingTestUtils.getFlingCalculatorObject(
            density = density,
            flingConfiguration = config
        )

        assertThat(flingCalculator.flingDuration(18f)).isNotEqualTo(20)
    }

    @Test
    fun `test fling info success`() {
        val config = FlingTestUtils.getDefaultScrollConfiguration()
        val density = FlingTestUtils.getDummyDensityComponent()
        val flingCalculator = FlingTestUtils.getFlingCalculatorObject(
            density = density,
            flingConfiguration = config
        )

        val info = flingCalculator.flingInfo(5f)

        assertThat(info.distance).isEqualTo(0.0033697102f)
        assertThat(info.duration).isEqualTo(6)
        assertThat(info.androidFlingSpline.deceleration(5f, config.decelerationFriction))
            .isEqualTo(1.7147983883554976)

        assertThat(info.androidFlingSpline.flingDistanceCoefficient(5f))
            .isEqualTo(1f)

        assertThat(info.androidFlingSpline.flingDistanceCoefficient(10f))
            .isEqualTo(1f)

        assertThat(info.androidFlingSpline.flingVelocityCoefficient(5f))
            .isEqualTo(0f)

        assertThat(info.androidFlingSpline.flingVelocityCoefficient(5f))
            .isEqualTo(0f)
    }
}