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

package io.iamjosephmj.flinger.flings

import androidx.compose.animation.core.AnimationState
import androidx.compose.animation.core.DecayAnimationSpec
import androidx.compose.animation.core.animateDecay
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollScope
import io.iamjosephmj.flinger.callbacks.FlingCallbacks
import kotlin.math.abs

/**
 * The default fling behavior implementation for Flinger.
 *
 * This class performs decay-based fling animations with customizable physics
 * and optional lifecycle callbacks for monitoring fling progress.
 *
 * @param flingDecay The decay animation specification that controls fling physics.
 * @param callbacks Optional callbacks for fling lifecycle events (start, progress, end).
 *
 * @author Joseph James
 */
class FlingerFlingBehavior(
    private val flingDecay: DecayAnimationSpec<Float>,
    private val callbacks: FlingCallbacks = FlingCallbacks.Empty
) : FlingBehavior {
    
    override suspend fun ScrollScope.performFling(initialVelocity: Float): Float {
        // Threshold to avoid spline curve NaN issues
        return if (abs(initialVelocity) > 1f) {
            var velocityLeft = initialVelocity
            var lastValue = 0f
            var totalScrolled = 0f
            var wasCancelled = false
            var initialValue = 0f
            var targetValue: Float? = null
            
            // Notify fling start
            callbacks.onFlingStart?.invoke(initialVelocity)
            
            AnimationState(
                initialValue = 0f,
                initialVelocity = initialVelocity,
            ).animateDecay(flingDecay) {
                try {
                    // Capture target value on first frame for progress calculation
                    if (targetValue == null) {
                        initialValue = value
                        // Estimate target based on decay spec behavior
                        targetValue = value + (velocity * 0.5f) // Approximation
                    }
                    
                    val delta = value - lastValue
                    val consumed = scrollBy(delta)
                    lastValue = value
                    totalScrolled += abs(consumed)
                    velocityLeft = this.velocity
                    
                    // Calculate and report progress
                    callbacks.onFlingProgress?.let { onProgress ->
                        val progress = if (abs(initialVelocity) > 0.1f) {
                            1f - (abs(velocityLeft) / abs(initialVelocity))
                        } else {
                            1f
                        }.coerceIn(0f, 1f)
                        onProgress(progress, velocityLeft)
                    }
                    
                    // Avoid rounding errors and stop if anything is unconsumed
                    if (abs(delta - consumed) > 0.5f) {
                        wasCancelled = true
                        this.cancelAnimation()
                    }
                } catch (e: Exception) {
                    wasCancelled = true
                    this.cancelAnimation()
                }
            }
            
            // Notify fling end
            callbacks.onFlingEnd?.invoke(totalScrolled, wasCancelled)
            
            velocityLeft
        } else {
            initialVelocity
        }
    }
}
