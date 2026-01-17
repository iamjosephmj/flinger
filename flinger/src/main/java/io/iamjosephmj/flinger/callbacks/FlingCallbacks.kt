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

package io.iamjosephmj.flinger.callbacks

/**
 * Callbacks for fling animation lifecycle events.
 *
 * These callbacks allow you to react to various stages of a fling animation,
 * enabling use cases like:
 * - Triggering haptic feedback when a fling starts
 * - Logging scroll analytics
 * - Synchronizing animations with fling progress
 * - Updating UI state when fling ends
 *
 * ## Usage Example
 * ```kotlin
 * flingBehavior(
 *     callbacks = FlingCallbacks(
 *         onFlingStart = { velocity ->
 *             hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
 *         },
 *         onFlingProgress = { progress, velocity ->
 *             parallaxOffset = progress * maxParallax
 *         },
 *         onFlingEnd = { finalOffset, cancelled ->
 *             analytics.logScrollComplete(finalOffset)
 *         }
 *     )
 * )
 * ```
 *
 * @property onFlingStart Called when a fling animation begins.
 *   Receives the initial velocity of the fling in pixels per second.
 * @property onFlingProgress Called periodically during the fling animation.
 *   Receives the current progress (0.0 to 1.0) and current velocity.
 * @property onFlingEnd Called when the fling animation completes.
 *   Receives the total distance scrolled and whether the fling was cancelled.
 *
 * @author Joseph James
 */
data class FlingCallbacks(
    /**
     * Called when a fling animation starts.
     *
     * @param initialVelocity The starting velocity of the fling in pixels per second.
     *   Positive values indicate scrolling down/right, negative values indicate up/left.
     */
    val onFlingStart: ((initialVelocity: Float) -> Unit)? = null,
    
    /**
     * Called during fling animation with progress updates.
     *
     * Note: This callback is invoked on every animation frame, so keep the
     * implementation lightweight to avoid performance issues.
     *
     * @param progress The current progress of the fling, from 0.0 (start) to 1.0 (end).
     * @param currentVelocity The current velocity at this point in the animation.
     */
    val onFlingProgress: ((progress: Float, currentVelocity: Float) -> Unit)? = null,
    
    /**
     * Called when the fling animation ends.
     *
     * @param totalScrolled The total distance scrolled during the fling in pixels.
     * @param cancelled Whether the fling was cancelled (e.g., user touched screen)
     *   or completed naturally.
     */
    val onFlingEnd: ((totalScrolled: Float, cancelled: Boolean) -> Unit)? = null
) {
    companion object {
        /**
         * An empty callbacks instance with no handlers.
         * Use this when you don't need any callbacks.
         */
        val Empty = FlingCallbacks()
    }
}
