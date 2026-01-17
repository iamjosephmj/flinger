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

package io.iamjosephmj.flinger.snap

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing

/**
 * Defines where items should snap to within the viewport.
 *
 * ## Usage Example
 * ```kotlin
 * // Snap items to the center of the viewport
 * LazyRow(
 *     flingBehavior = snapFlingBehavior(
 *         lazyListState = listState,
 *         snapPosition = SnapPosition.Center
 *     )
 * ) {
 *     items(100) { CarouselCard(it) }
 * }
 * ```
 *
 * @author Joseph James
 */
enum class SnapPosition {
    /**
     * Snap items to the start of the viewport.
     * - For vertical lists: top of the viewport
     * - For horizontal lists: left (or right in RTL) of the viewport
     */
    Start,
    
    /**
     * Snap items to the center of the viewport.
     * The center of the snapped item aligns with the center of the viewport.
     * This is the most common choice for carousels and galleries.
     */
    Center,
    
    /**
     * Snap items to the end of the viewport.
     * - For vertical lists: bottom of the viewport
     * - For horizontal lists: right (or left in RTL) of the viewport
     */
    End
}

/**
 * Configuration for snap behavior.
 *
 * @property snapPosition Where items should snap to within the viewport.
 * @property velocityThreshold Minimum velocity required to snap to the next item.
 *   Below this threshold, the list snaps back to the current item.
 * @property maxFlingItems Maximum number of items to fling past in a single gesture.
 *   Limits how far a very fast fling can travel. Set to [Int.MAX_VALUE] for unlimited.
 * @property snapAnimation The animation configuration for the snap effect.
 * @property smoothFusion When true, the snap dynamically fuses with the fling
 *   when velocity decays below a threshold, creating a seamless transition.
 * @property fusionVelocityRatio Controls when fusion kicks in (0.0 to 1.0).
 *   Lower values = earlier fusion (smoother but may feel "assisted").
 *   Higher values = later fusion (more natural momentum, snappier end).
 *   Default is 0.15 (fusion starts when velocity drops to 15% of initial).
 */
data class SnapConfig(
    val snapPosition: SnapPosition = SnapPosition.Start,
    val velocityThreshold: Float = 400f,
    val maxFlingItems: Int = Int.MAX_VALUE,
    val snapAnimation: SnapAnimationConfig = SnapAnimationConfig.Smooth,
    val smoothFusion: Boolean = false,
    val fusionVelocityRatio: Float = 0.15f
)

/**
 * Configuration for the snap animation that occurs after scrolling stops.
 *
 * This class provides presets for common animation feels, as well as the ability
 * to create custom configurations. The animation controls how the list settles
 * into its final snapped position.
 *
 * ## Presets
 * - [Smooth] - Gentle, fluid snap with no bounce (recommended for most cases)
 * - [Snappy] - Quick, responsive snap for fast interactions
 * - [Bouncy] - Playful snap with a slight overshoot
 * - [Gentle] - Very slow, relaxed snap for a premium feel
 * - [Instant] - Near-instant snap with minimal animation
 *
 * ## Custom Configuration
 * ```kotlin
 * val customSnap = SnapAnimationConfig.custom(
 *     stiffness = 300f,
 *     dampingRatio = 0.8f
 * )
 * ```
 *
 * @author Joseph James
 */
sealed class SnapAnimationConfig {
    
    /**
     * Creates the AnimationSpec for this configuration.
     */
    abstract fun <T> createAnimationSpec(): AnimationSpec<T>
    
    /**
     * Spring-based snap animation with configurable physics.
     *
     * @property stiffness Controls how fast the spring reaches equilibrium.
     *   Higher values = faster snap. Range: 1 - 10000.
     * @property dampingRatio Controls bounce/overshoot.
     *   - 0.0 = no damping (oscillates forever)
     *   - 1.0 = critically damped (no overshoot, smooth stop)
     *   - > 1.0 = overdamped (slower, no overshoot)
     *   - < 1.0 = underdamped (bouncy)
     * @property visibilityThreshold Minimum visible change before animation stops.
     */
    data class SpringBased(
        val stiffness: Float = Spring.StiffnessLow,
        val dampingRatio: Float = Spring.DampingRatioNoBouncy,
        val visibilityThreshold: Float = 0.1f
    ) : SnapAnimationConfig() {
        @Suppress("UNCHECKED_CAST")
        override fun <T> createAnimationSpec(): AnimationSpec<T> = spring(
            dampingRatio = dampingRatio,
            stiffness = stiffness,
            visibilityThreshold = visibilityThreshold as? T
        )
    }
    
    /**
     * Duration-based snap animation with configurable timing.
     *
     * @property durationMillis How long the snap animation takes.
     * @property delayMillis Delay before animation starts.
     * @property easing The easing curve to use.
     */
    data class TweenBased(
        val durationMillis: Int = 300,
        val delayMillis: Int = 0,
        val easing: androidx.compose.animation.core.Easing = FastOutSlowInEasing
    ) : SnapAnimationConfig() {
        override fun <T> createAnimationSpec(): AnimationSpec<T> = tween(
            durationMillis = durationMillis,
            delayMillis = delayMillis,
            easing = easing
        )
    }
    
    companion object {
        /**
         * Smooth, fluid snap with no bounce.
         * 
         * Best for: Most carousels and galleries.
         * Feel: Professional, polished, non-distracting.
         */
        val Smooth = SpringBased(
            stiffness = 200f,
            dampingRatio = 1f  // Critically damped - no bounce
        )
        
        /**
         * Quick, responsive snap.
         * 
         * Best for: Fast-paced UIs, interactive lists.
         * Feel: Snappy, immediate, responsive.
         */
        val Snappy = SpringBased(
            stiffness = 800f,
            dampingRatio = 0.9f
        )
        
        /**
         * Playful snap with slight overshoot.
         * 
         * Best for: Games, playful UIs, children's apps.
         * Feel: Fun, bouncy, energetic.
         */
        val Bouncy = SpringBased(
            stiffness = 400f,
            dampingRatio = 0.6f  // Underdamped - some bounce
        )
        
        /**
         * Very slow, relaxed snap.
         * 
         * Best for: Luxury/premium apps, reading apps.
         * Feel: Elegant, unhurried, premium.
         */
        val Gentle = SpringBased(
            stiffness = 100f,
            dampingRatio = 1f
        )
        
        /**
         * Near-instant snap with minimal animation.
         * 
         * Best for: Utility apps, accessibility-focused UIs.
         * Feel: Direct, no-nonsense.
         */
        val Instant = TweenBased(
            durationMillis = 100,
            easing = LinearOutSlowInEasing
        )
        
        /**
         * iOS-style snap animation.
         * 
         * Best for: Cross-platform apps wanting iOS consistency.
         * Feel: Familiar to iOS users.
         */
        val IOS = SpringBased(
            stiffness = 300f,
            dampingRatio = 0.85f
        )
        
        /**
         * Material Design inspired snap.
         * 
         * Best for: Material Design apps.
         * Feel: Modern, Material-like.
         */
        val Material = TweenBased(
            durationMillis = 250,
            easing = FastOutSlowInEasing
        )
        
        /**
         * Creates a custom spring-based snap animation.
         *
         * @param stiffness Higher = faster snap (default: 200f)
         * @param dampingRatio 1.0 = no bounce, <1.0 = bouncy, >1.0 = slow (default: 1.0f)
         */
        fun custom(
            stiffness: Float = 200f,
            dampingRatio: Float = 1f
        ): SnapAnimationConfig = SpringBased(stiffness, dampingRatio)
        
        /**
         * Creates a custom tween-based snap animation.
         *
         * @param durationMillis Animation duration in milliseconds.
         * @param easing The easing function to use.
         */
        fun customTween(
            durationMillis: Int = 300,
            easing: androidx.compose.animation.core.Easing = FastOutSlowInEasing
        ): SnapAnimationConfig = TweenBased(durationMillis, 0, easing)
    }
}
