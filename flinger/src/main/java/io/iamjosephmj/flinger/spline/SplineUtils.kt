package io.iamjosephmj.flinger.spline

import io.iamjosephmj.flinger.ScrollViewConfiguration
import kotlin.math.abs

internal fun computeSplineInfo(
    splinePositions: FloatArray,
    splineTimes: FloatArray,
    nbSamples: Int,
    scrollViewConfiguration: ScrollViewConfiguration
) {
    var xMin = 0.0f
    var yMin = 0.0f
    for (i in 0 until nbSamples) {
        val alpha = i.toFloat() / nbSamples
        var xMax = 1.0f
        var x: Float
        var tx: Float
        var coef: Float
        while (true) {
            x = xMin + (xMax - xMin) / 2.0f
            coef = 3.0f * x * (1.0f - x)
            tx = coef * ((1.0f - x)
                    * scrollViewConfiguration.splineP1 + x
                    * scrollViewConfiguration.splineP2) + x * x * x
            if (abs(tx - alpha) < 1E-5) break
            if (tx > alpha) xMax = x else xMin = x
        }
        splinePositions[i] =
            coef * ((1.0f - x) * scrollViewConfiguration.splineStartTension + x) + x * x * x
        var yMax = 1.0f
        var y: Float
        var dy: Float
        while (true) {
            y = yMin + (yMax - yMin) / 2.0f
            coef = 3.0f * y * (1.0f - y)
            dy = coef * ((1.0f - y) * scrollViewConfiguration.splineStartTension + y) + y * y * y
            if (abs(dy - alpha) < 1E-5) break
            if (dy > alpha) yMax = y else yMin = y
        }
        splineTimes[i] = coef * ((1.0f - y) * scrollViewConfiguration.splineP1 + y *
                scrollViewConfiguration.splineP2
                ) + y * y * y
    }
    splineTimes[nbSamples] = 1.0f
    splinePositions[nbSamples] = splineTimes[nbSamples]
}
