package io.iamjosephmj.flinger.fling

import com.google.common.truth.Truth
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.runners.MockitoJUnitRunner


class FlingCalculatorTest {

    @Test
    fun `test fling distance success`() {
        val config = FlingTestUtils.getDefaultScrollConfiguration()
        val density = FlingTestUtils.getDummyDensityComponent()
        val flingCalculator = FlingTestUtils.getFlingCalculatorObject(
            density = density,
            flingConfiguration = config
        )

        Truth.assertThat(flingCalculator.flingDistance(5f)).isEqualTo(0.0033697102f)
    }

    @Test
    fun `test fling distance failed`() {
        val config = FlingTestUtils.getDefaultScrollConfiguration()
        val density = FlingTestUtils.getDummyDensityComponent()
        val flingCalculator = FlingTestUtils.getFlingCalculatorObject(
            density = density,
            flingConfiguration = config
        )

        Truth.assertThat(flingCalculator.flingDistance(21f)).isNotEqualTo(5f)
    }

    @Test
    fun `test fling duration pass`() {
        val config = FlingTestUtils.getDefaultScrollConfiguration()
        val density = FlingTestUtils.getDummyDensityComponent()
        val flingCalculator = FlingTestUtils.getFlingCalculatorObject(
            density = density,
            flingConfiguration = config
        )

        Truth.assertThat(flingCalculator.flingDuration(5f)).isEqualTo(6)
    }


    @Test
    fun `test fling duration fail`() {
        val config = FlingTestUtils.getDefaultScrollConfiguration()
        val density = FlingTestUtils.getDummyDensityComponent()
        val flingCalculator = FlingTestUtils.getFlingCalculatorObject(
            density = density,
            flingConfiguration = config
        )

        Truth.assertThat(flingCalculator.flingDuration(18f)).isNotEqualTo(20)
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

        Truth.assertThat(info.distance).isEqualTo(0.0033697102f)
        Truth.assertThat(info.duration).isEqualTo(6)
        Truth.assertThat(info.androidFlingSpline.deceleration(5f, config.decelerationFriction))
            .isEqualTo(1.7147983883554976)

        Truth.assertThat(info.androidFlingSpline.flingPosition(5f).distanceCoefficient)
            .isEqualTo(1f)

        Truth.assertThat(info.androidFlingSpline.flingPosition(10f).distanceCoefficient)
            .isEqualTo(1f)

        Truth.assertThat(info.androidFlingSpline.flingPosition(5f).velocityCoefficient)
            .isEqualTo(0f)

        Truth.assertThat(info.androidFlingSpline.flingPosition(5f).velocityCoefficient)
            .isEqualTo(0f)
    }
}