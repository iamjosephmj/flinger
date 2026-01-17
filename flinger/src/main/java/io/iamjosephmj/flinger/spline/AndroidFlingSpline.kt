/*
* MIT License
*
* Copyright (c) 2021 Joseph James
*
* Permission is hereby granted, free of charge, to any person obtaining a copy
* of this software and associated documentation files (the "Software"), to deal
* in the Software without restriction, including without limitation the rights
* to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
* copies of the Software, and to permit persons to whom the Software is
* furnished to do so, subject to the following conditions:
*
* The above copyright notice and this permission notice shall be included in all
* copies or substantial portions of the Software.
*
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
* IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
* AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
* OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
* SOFTWARE.
*
*/

package io.iamjosephmj.flinger.spline

import io.iamjosephmj.flinger.configs.FlingConfiguration
import kotlin.math.abs
import kotlin.math.ln


/**
 * The native Android fling scroll spline and the ability to sample it.
 *
 * Ported from `android.widget.Scroller`.
 * +
 * Addition of ScrollViewConfiguration.
 *
 * Performance optimized: Uses inline coefficient methods to avoid object allocation.
 *
 * @author Joseph James.
 */
class AndroidFlingSpline(private val flingConfiguration: FlingConfiguration) {
    // Eager initialization - these are always used
    private val samples = flingConfiguration.numberOfSplinePoints

    private val splinePositions = FloatArray(flingConfiguration.numberOfSplinePoints + 1)

    private val splineTimes = FloatArray(flingConfiguration.numberOfSplinePoints + 1)

    init {
        computeSplineInfo(splinePositions, splineTimes, samples, flingConfiguration)
    }

    /**
     * Compute the distance coefficient at the given time.
     *
     * @param time progress through the fling animation from 0-1
     * @return distance coefficient from 0-1
     */
    fun flingDistanceCoefficient(time: Float): Float {
        val index = (samples * time).toInt()
        if (index >= samples) return 1f
        
        val tInf = index.toFloat() / samples
        val dInf = splinePositions[index]
        val dSup = splinePositions[index + 1]
        val tSup = (index + 1).toFloat() / samples
        val velocityCoef = (dSup - dInf) / (tSup - tInf)
        return dInf + (time - tInf) * velocityCoef
    }

    /**
     * Compute the velocity coefficient at the given time.
     *
     * @param time progress through the fling animation from 0-1
     * @return velocity coefficient
     */
    fun flingVelocityCoefficient(time: Float): Float {
        val index = (samples * time).toInt()
        if (index >= samples) return 0f
        
        val tInf = index.toFloat() / samples
        val tSup = (index + 1).toFloat() / samples
        val dInf = splinePositions[index]
        val dSup = splinePositions[index + 1]
        return (dSup - dInf) / (tSup - tInf)
    }

    /**
     * The rate of deceleration along the spline motion given [velocity] and [friction].
     */
    fun deceleration(velocity: Float, friction: Float): Double =
        ln(flingConfiguration.splineInflection * abs(velocity) / friction.toDouble())
}
