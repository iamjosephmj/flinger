package io.iamjosephmj.flinger.flings

import androidx.compose.ui.unit.Density
import io.iamjosephmj.flinger.ScrollViewConfiguration
import io.iamjosephmj.flinger.spline.AndroidFlingSpline
import kotlin.math.exp
import kotlin.math.sign


/**
 * Configuration for Android-feel flinging motion at the given density.
 *
 * @param friction scroll friction.
 * @param density density of the screen. Use LocalDensity to get current density in composition.
 */
class FlingCalculator(
    val density: Density,
    val scrollConfiguration: ScrollViewConfiguration
) {

    private val androidFlingSpline: AndroidFlingSpline by lazy {
        AndroidFlingSpline(scrollViewConfiguration = scrollConfiguration)
    }

    /**
     * Compute the rate of deceleration based on pixel density, physical gravity
     * and a [coefficient of friction][friction].
     */
    private fun computeDeceleration(friction: Float, density: Float): Float =
        scrollConfiguration.gravitationalForce *
                scrollConfiguration.inchesPerMeter *
                density *
                160f *
                friction


    /**
     * A density-specific coefficient adjusted to physical values.
     */
    private val magicPhysicalCoefficient: Float by lazy { computeDeceleration(density) }

    /**
     * Computes the rate of deceleration in pixels based on
     * the given [density].
     */
    private fun computeDeceleration(density: Density) =
        computeDeceleration(scrollConfiguration.decelerationFriction, density.density)

    private fun getSplineDeceleration(velocity: Float): Double =
        androidFlingSpline.deceleration(
            velocity,
            scrollConfiguration.scrollFriction * magicPhysicalCoefficient
        )

    /**
     * Compute the duration in milliseconds of a fling with an initial velocity of [velocity]
     */
    fun flingDuration(velocity: Float): Long {
        val l = getSplineDeceleration(velocity)
        val decelMinusOne = scrollConfiguration.decelerationRate - 1.0
        return (1000.0 * exp(l / decelMinusOne)).toLong()
    }

    /**
     * Compute the distance of a fling in units given an initial [velocity] of units/second
     */
    fun flingDistance(velocity: Float): Float {
        val l = getSplineDeceleration(velocity)
        val decelMinusOne = scrollConfiguration.decelerationRate - 1.0
        return (
                scrollConfiguration.scrollFriction * magicPhysicalCoefficient
                        * exp(scrollConfiguration.decelerationRate / decelMinusOne * l)
                ).toFloat()
    }

    /**
     * Compute all interesting information about a fling of initial velocity [velocity].
     */
    fun flingInfo(velocity: Float): FlingInfo {
        val l = getSplineDeceleration(velocity)
        val decelMinusOne = scrollConfiguration.decelerationRate - 1.0
        return FlingInfo(
            initialVelocity = velocity,
            distance = (
                    scrollConfiguration.scrollFriction * magicPhysicalCoefficient
                            * exp(scrollConfiguration.decelerationRate / decelMinusOne * l)
                    ).toFloat(),
            duration = (1000.0 * exp(l / decelMinusOne)).toLong(),
            androidFlingSpline
        )
    }

    /**
     * Info about a fling started with [initialVelocity]. The units of [initialVelocity]
     * determine the distance units of [distance] and the time units of [duration].
     */
    data class FlingInfo(
        val initialVelocity: Float,
        val distance: Float,
        val duration: Long,
        val androidFlingSpline: AndroidFlingSpline
    ) {
        fun position(time: Long): Float {
            val splinePos = if (duration > 0) time / duration.toFloat() else 1f
            return distance * sign(initialVelocity) *
                    androidFlingSpline.flingPosition(splinePos).distanceCoefficient
        }

        fun velocity(time: Long): Float {
            val splinePos = if (duration > 0) time / duration.toFloat() else 1f
            return androidFlingSpline.flingPosition(splinePos).velocityCoefficient *
                    sign(initialVelocity) * distance / duration * 1000.0f
        }
    }
}