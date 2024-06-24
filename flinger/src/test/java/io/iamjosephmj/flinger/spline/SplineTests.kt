package io.iamjosephmj.flinger.spline

import com.google.common.truth.Truth.assertThat
import io.iamjosephmj.flinger.fling.FlingTestUtils
import io.iamjosephmj.flinger.flings.SplineBasedFloatDecayAnimationSpec
import org.junit.Test

class SplineTests {

    @Test
    fun `test spline float decay animation spec success`() {
        val density = FlingTestUtils.getDummyDensityComponent()
        val scrollConfig = FlingTestUtils.getDefaultScrollConfiguration()
        val spline = SplineBasedFloatDecayAnimationSpec(
            density = density,
            scrollConfiguration = scrollConfig
        )

        assertThat(spline.getDurationNanos(1f, 20f))
            .isEqualTo(18000000)

        assertThat(spline.getTargetValue(1f, 20f))
            .isEqualTo(1.037405f)

        assertThat(spline.getValueFromNanos(1000, 1f, 20f))
            .isEqualTo(1.0000027f)

        assertThat(spline.getVelocityFromNanos(1000, 1f, 20f))
            .isEqualTo(16.320223f)
    }

    @Test
    fun `test spline util`() {
        val splinePositions = SplineTestUtils.getSplinePositionsArray()
        val splineTimes = SplineTestUtils.getSplineTimeArray()
        computeSplineInfo(
            splinePositions = splinePositions,
            splineTimes = splineTimes,
            nbSamples = SplineTestUtils.getNumberOfSamples(),
            flingConfiguration = FlingTestUtils.getDefaultScrollConfiguration()
        )

        assertThat(splinePositions).isEqualTo(SplineTestUtils.getExpectedPositionArray())

        assertThat(splineTimes).isEqualTo(SplineTestUtils.getExpectedTimeArray())
    }
}