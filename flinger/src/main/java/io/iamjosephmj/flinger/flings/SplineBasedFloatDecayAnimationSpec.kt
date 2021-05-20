package io.iamjosephmj.flinger.flings

import androidx.compose.animation.core.FloatDecayAnimationSpec
import androidx.compose.ui.unit.Density
import io.iamjosephmj.flinger.ScrollViewConfiguration
import kotlin.math.sign


class SplineBasedFloatDecayAnimationSpec(
    density: Density,
    scrollConfiguration: ScrollViewConfiguration
) :
    FloatDecayAnimationSpec {

    private val flingCalculator = FlingCalculator(
        density = density,
        scrollConfiguration = scrollConfiguration
    )

    override val absVelocityThreshold: Float get() = 0f

    private fun flingDistance(startVelocity: Float): Float =
        flingCalculator.flingDistance(startVelocity) * sign(startVelocity)

    override fun getTargetValue(initialValue: Float, initialVelocity: Float): Float =
        initialValue + flingDistance(initialVelocity)

    @Suppress("MethodNameUnits")
    override fun getValueFromNanos(
        playTimeNanos: Long,
        initialValue: Float,
        initialVelocity: Float
    ): Float {
        val playTimeMillis = playTimeNanos / 1_000_000L
        return initialValue + flingCalculator.flingInfo(initialVelocity).position(playTimeMillis)
    }

    @Suppress("MethodNameUnits")
    override fun getDurationNanos(initialValue: Float, initialVelocity: Float): Long =
        flingCalculator.flingDuration(initialVelocity) * 1_000_000L

    @Suppress("MethodNameUnits")
    override fun getVelocityFromNanos(
        playTimeNanos: Long,
        initialValue: Float,
        initialVelocity: Float
    ): Float {
        val playTimeMillis = playTimeNanos / 1_000_000L
        return flingCalculator.flingInfo(initialVelocity).velocity(playTimeMillis)
    }
}
