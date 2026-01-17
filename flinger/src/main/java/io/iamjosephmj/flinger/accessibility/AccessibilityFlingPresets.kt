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

package io.iamjosephmj.flinger.accessibility

import android.os.Build
import android.provider.Settings
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import io.iamjosephmj.flinger.configs.FlingConfiguration
import io.iamjosephmj.flinger.flings.flingBehavior

/**
 * Accessibility-focused fling behavior presets.
 *
 * These presets are designed to provide better scroll experiences for users
 * with different accessibility needs. They automatically adapt to system
 * accessibility settings where possible.
 *
 * ## Key Features
 * - Automatic adaptation to system "Reduce Motion" settings
 * - Presets optimized for users who need more control
 * - Configurable animation scaling
 *
 * ## Usage Example
 * ```kotlin
 * // Automatically adapts to system accessibility settings
 * LazyColumn(
 *     flingBehavior = AccessibilityFlingPresets.systemAware()
 * ) {
 *     items(100) { Text("Item $it") }
 * }
 * ```
 *
 * @author Joseph James
 */
object AccessibilityFlingPresets {

    /**
     * A fling behavior that automatically adapts to system accessibility settings.
     *
     * This preset checks the system's animation scale and "Reduce Motion" settings
     * and adjusts the fling behavior accordingly:
     * - If animations are disabled or reduced motion is enabled: Uses [reducedMotion]
     * - Otherwise: Uses the default smooth scroll
     *
     * **Best for:**
     * - Apps that want to be accessibility-friendly by default
     * - Complying with platform accessibility guidelines
     * - Users who have enabled reduced motion settings
     *
     * @return A [FlingBehavior] that adapts to system accessibility settings.
     */
    @Composable
    fun systemAware(): FlingBehavior {
        val context = LocalContext.current
        
        val shouldReduceMotion = remember {
            isReducedMotionEnabled(context)
        }
        
        return if (shouldReduceMotion) {
            reducedMotion()
        } else {
            flingBehavior()
        }
    }

    /**
     * A minimal motion fling behavior for users who prefer reduced animations.
     *
     * This preset uses high friction and quick deceleration to minimize
     * the perceived motion while still allowing functional scrolling.
     *
     * **Best for:**
     * - Users sensitive to motion
     * - Users with vestibular disorders
     * - Apps targeting users who enable "Reduce Motion" settings
     *
     * **Configuration:**
     * - Very high deceleration friction (0.8)
     * - Higher scroll friction (0.05)
     * - Quick, controlled stops
     *
     * @return A [FlingBehavior] with minimal motion.
     */
    @Composable
    fun reducedMotion(): FlingBehavior = flingBehavior(
        scrollConfiguration = FlingConfiguration.Builder()
            .scrollViewFriction(0.05f)
            .decelerationFriction(0.8f)
            .build()
    )

    /**
     * A fling behavior optimized for users who need more precise control.
     *
     * This preset creates a slower, more controllable scroll that's easier
     * to stop at exact positions. Ideal for users with motor impairments
     * or those who prefer deliberate, precise interactions.
     *
     * **Best for:**
     * - Users with motor control difficulties
     * - Precise item selection scenarios
     * - Users who find fast scrolling difficult to control
     *
     * **Configuration:**
     * - Moderate friction (0.04)
     * - High deceleration (0.6)
     * - Shorter scroll distances
     *
     * @return A [FlingBehavior] with enhanced control.
     */
    @Composable
    fun largeTarget(): FlingBehavior = flingBehavior(
        scrollConfiguration = FlingConfiguration.Builder()
            .scrollViewFriction(0.04f)
            .decelerationFriction(0.6f)
            .absVelocityThreshold(100f) // Require more deliberate gestures
            .build()
    )

    /**
     * A fling behavior with slower animation for easier visual tracking.
     *
     * This preset maintains smooth animations but at a slower pace,
     * making it easier for users to track content as it scrolls.
     *
     * **Best for:**
     * - Users with visual tracking difficulties
     * - Users who prefer to follow scrolling content visually
     * - Situations where content comprehension during scroll is important
     *
     * **Configuration:**
     * - Lower deceleration rate for extended animation
     * - Standard friction values
     * - Smooth, predictable motion
     *
     * @return A [FlingBehavior] with slower, trackable animations.
     */
    @Composable
    fun slowMotion(): FlingBehavior = flingBehavior(
        scrollConfiguration = FlingConfiguration.Builder()
            .scrollViewFriction(0.012f)
            .decelerationFriction(0.15f)
            .decelerationRate(1.8f) // Slower deceleration
            .build()
    )

    /**
     * Returns the native Android fling behavior.
     *
     * Use this when you want to fall back to the platform default,
     * which already has some accessibility considerations built in.
     *
     * @return The default Android [FlingBehavior].
     */
    @Composable
    fun platformDefault(): FlingBehavior = ScrollableDefaults.flingBehavior()

    /**
     * Checks if the system has reduced motion or disabled animations.
     */
    private fun isReducedMotionEnabled(context: android.content.Context): Boolean {
        return try {
            // Check animator duration scale (0 = animations disabled)
            val animatorScale = Settings.Global.getFloat(
                context.contentResolver,
                Settings.Global.ANIMATOR_DURATION_SCALE,
                1.0f
            )
            
            // Check transition animation scale
            val transitionScale = Settings.Global.getFloat(
                context.contentResolver,
                Settings.Global.TRANSITION_ANIMATION_SCALE,
                1.0f
            )
            
            // Consider motion reduced if either scale is 0 or very low
            animatorScale < 0.1f || transitionScale < 0.1f
        } catch (e: Exception) {
            false
        }
    }
}
