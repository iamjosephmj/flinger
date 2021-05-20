package io.iamjosephmj.flinger.spline

import io.iamjosephmj.flinger.ScrollViewConfiguration
import kotlin.math.abs
import kotlin.math.ln


/**
 * The native Android fling scroll spline and the ability to sample it.
 *
 * Ported from `android.widget.Scroller`.
 */
class AndroidFlingSpline(private val scrollViewConfiguration: ScrollViewConfiguration) {
    private val samples by lazy { scrollViewConfiguration.numberOfSplinePoints }

    private val splinePositions by lazy {
        FloatArray(scrollViewConfiguration.numberOfSplinePoints + 1)
    }

    private val splineTimes by lazy {
        FloatArray(scrollViewConfiguration.numberOfSplinePoints + 1)
    }

    init {
        computeSplineInfo(splinePositions, splineTimes, samples, scrollViewConfiguration)
    }

    /**
     * Compute an instantaneous fling position along the scroller spline.
     *
     * @param time progress through the fling animation from 0-1
     */
    fun flingPosition(time: Float): FlingResult {
        val index = (samples * time).toInt()
        var distanceCoef = 1f
        var velocityCoef = 0f
        if (index < samples) {
            val tInf = index.toFloat() / samples
            val tSup = (index + 1).toFloat() / samples
            val dInf = splinePositions[index]
            val dSup = splinePositions[index + 1]
            velocityCoef = (dSup - dInf) / (tSup - tInf)
            distanceCoef = dInf + (time - tInf) * velocityCoef
        }
        return FlingResult(
            distanceCoefficient = distanceCoef,
            velocityCoefficient = velocityCoef
        )
    }

    /**
     * The rate of deceleration along the spline motion given [velocity] and [friction].
     */
    fun deceleration(velocity: Float, friction: Float): Double =
        ln(scrollViewConfiguration.splineInflection * abs(velocity) / friction.toDouble())

    /**
     * Result coefficients of a scroll computation
     */
    data class FlingResult(
        /**
         * Linear distance traveled from 0-1, from source (0) to destination (1)
         */
        val distanceCoefficient: Float,
        /**
         * Instantaneous velocity coefficient at this point in the fling expressed in
         * total distance per unit time
         */
        val velocityCoefficient: Float
    )
}
