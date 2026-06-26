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

        // Sampled at 0.001ms (sub-millisecond precision), not truncated to 0ms.
        assertThat(spline.getValueFromNanos(1000, 1f, 20f))
            .isEqualTo(1.0000191f)

        assertThat(spline.getVelocityFromNanos(1000, 1f, 20f))
            .isEqualTo(16.320223f)
    }

    @Test
    fun `getValueFromNanos samples position with sub-millisecond precision`() {
        val spline = SplineBasedFloatDecayAnimationSpec(
            density = FlingTestUtils.getDummyDensityComponent(),
            scrollConfiguration = FlingTestUtils.getDefaultScrollConfiguration()
        )

        // 5.0ms and 5.9ms truncate to the same whole millisecond. With float
        // precision they must sample different points on the spline.
        val at5_0ms = spline.getValueFromNanos(5_000_000L, 0f, 20f)
        val at5_9ms = spline.getValueFromNanos(5_900_000L, 0f, 20f)

        assertThat(at5_9ms).isNotEqualTo(at5_0ms)
    }

    @Test
    fun `getVelocityFromNanos samples velocity with sub-millisecond precision`() {
        val spline = SplineBasedFloatDecayAnimationSpec(
            density = FlingTestUtils.getDummyDensityComponent(),
            scrollConfiguration = FlingTestUtils.getDefaultScrollConfiguration()
        )

        val at5_0ms = spline.getVelocityFromNanos(5_000_000L, 0f, 20f)
        val at5_9ms = spline.getVelocityFromNanos(5_900_000L, 0f, 20f)

        assertThat(at5_9ms).isNotEqualTo(at5_0ms)
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