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

package io.iamjosephmj.flinger.debug

import androidx.compose.runtime.Stable
import io.iamjosephmj.flinger.configs.FlingConfiguration

/**
 * Real-time metrics captured during fling animations.
 *
 * This class holds all the data needed to visualize and debug fling behavior.
 *
 * @property isFlinging Whether a fling is currently in progress.
 * @property initialVelocity The velocity at which the current fling started.
 * @property currentVelocity The current velocity of the fling.
 * @property progress The progress of the fling from 0.0 to 1.0.
 * @property totalScrolled The total distance scrolled during the current fling.
 * @property flingCount The total number of flings since metrics were reset.
 * @property lastFlingDuration Duration of the last completed fling in milliseconds.
 * @property averageVelocity Average initial velocity across all flings.
 * @property configuration The current fling configuration being used.
 *
 * @author Joseph James
 */
@Stable
data class FlingMetrics(
    val isFlinging: Boolean = false,
    val initialVelocity: Float = 0f,
    val currentVelocity: Float = 0f,
    val progress: Float = 0f,
    val totalScrolled: Float = 0f,
    val flingCount: Int = 0,
    val lastFlingDuration: Long = 0L,
    val averageVelocity: Float = 0f,
    val configuration: FlingConfiguration? = null
) {
    /**
     * Formatted string representation of current velocity.
     */
    val velocityText: String
        get() = "%.0f px/s".format(currentVelocity)
    
    /**
     * Formatted string representation of total scrolled distance.
     */
    val scrolledText: String
        get() = "%.0f px".format(totalScrolled)
    
    /**
     * Formatted string representation of progress percentage.
     */
    val progressText: String
        get() = "%.0f%%".format(progress * 100)
    
    /**
     * Generates Kotlin code for the current configuration.
     * Useful for developers to copy settings they like.
     */
    fun generateConfigCode(): String {
        val config = configuration ?: return "// No configuration available"
        
        return buildString {
            appendLine("FlingConfiguration.Builder()")
            appendLine("    .scrollViewFriction(${config.scrollFriction}f)")
            appendLine("    .decelerationFriction(${config.decelerationFriction}f)")
            appendLine("    .decelerationRate(${config.decelerationRate}f)")
            appendLine("    .gravitationalForce(${config.gravitationalForce}f)")
            appendLine("    .splineInflection(${config.splineInflection}f)")
            appendLine("    .splineStartTension(${config.splineStartTension}f)")
            appendLine("    .numberOfSplinePoints(${config.numberOfSplinePoints})")
            appendLine("    .build()")
        }
    }
    
    companion object {
        val Empty = FlingMetrics()
    }
}

/**
 * Aggregated statistics for fling analytics.
 */
data class FlingStatistics(
    val totalFlings: Int = 0,
    val totalDistance: Float = 0f,
    val averageVelocity: Float = 0f,
    val maxVelocity: Float = 0f,
    val averageDistance: Float = 0f,
    val averageDuration: Long = 0L
)
