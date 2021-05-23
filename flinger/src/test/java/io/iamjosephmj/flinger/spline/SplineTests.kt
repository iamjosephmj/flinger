package io.iamjosephmj.flinger.spline

import com.google.common.truth.Truth
import io.iamjosephmj.flinger.fling.FlingTestUtils
import io.iamjosephmj.flinger.flings.SplineBasedFloatDecayAnimationSpec
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.runners.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
class SplineTests {

    @Test
    fun `test spline float decay animation spec success`() {
        val density = FlingTestUtils.getDummyDensityComponent()
        val scrollConfig = FlingTestUtils.getDefaultScrollConfiguration()
        val spline = SplineBasedFloatDecayAnimationSpec(
            density = density,
            scrollConfiguration = scrollConfig
        )

        Truth.assertThat(spline.getDurationNanos(1f, 20f))
            .isEqualTo(18000000)

        Truth.assertThat(spline.getTargetValue(1f, 20f))
            .isEqualTo(1.037405f)

        Truth.assertThat(spline.getValueFromNanos(1000, 1f, 20f))
            .isEqualTo(1.0000027f)

        Truth.assertThat(spline.getVelocityFromNanos(1000, 1f, 20f))
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

        Truth.assertThat(splinePositions).isEqualTo(SplineTestUtils.getExpectedPositionArray())

        Truth.assertThat(splineTimes).isEqualTo(SplineTestUtils.getExpectedTimeArray())
    }
}