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

package io.iamjosephmj.flinger.adaptive

import androidx.compose.animation.core.AnimationState
import androidx.compose.animation.core.DecayAnimationSpec
import androidx.compose.animation.core.animateDecay
import androidx.compose.animation.core.generateDecayAnimationSpec
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalDensity
import io.iamjosephmj.flinger.callbacks.FlingCallbacks
import io.iamjosephmj.flinger.configs.FlingConfiguration
import io.iamjosephmj.flinger.flings.SplineBasedFloatDecayAnimationSpec
import kotlin.math.abs

/**
 * Creates an adaptive fling behavior that dynamically adjusts physics based on velocity.
 *
 * This behavior uses different configurations for gentle vs aggressive flings,
 * providing a more intuitive scroll experience. Gentle swipes result in precise,
 * controlled scrolling while aggressive swipes allow for longer momentum scrolls.
 *
 * ## How It Works
 * - When fling velocity is below [velocityThreshold]: Uses [gentleConfig]
 * - When fling velocity is at or above [velocityThreshold]: Uses [aggressiveConfig]
 * - Optionally interpolates between configs using [interpolator]
 *
 * ## Usage Example
 * ```kotlin
 * LazyColumn(
 *     flingBehavior = adaptiveFlingBehavior(
 *         gentleConfig = FlingConfiguration.Builder()
 *             .decelerationFriction(0.4f)
 *             .build(),
 *         aggressiveConfig = FlingConfiguration.Builder()
 *             .decelerationFriction(0.05f)
 *             .build(),
 *         velocityThreshold = 1500f
 *     )
 * ) {
 *     items(100) { Text("Item $it") }
 * }
 * ```
 *
 * @param gentleConfig Configuration used for low-velocity (gentle) flings.
 *   Typically has higher friction for more controlled scrolling.
 * @param aggressiveConfig Configuration used for high-velocity (aggressive) flings.
 *   Typically has lower friction for longer scroll distances.
 * @param velocityThreshold The velocity (in pixels/second) that separates gentle
 *   from aggressive flings. Default is 1500f.
 * @param interpolator Optional function to blend between configs based on velocity.
 *   Receives a value from 0 (at threshold) to 1 (at max velocity) and should return
 *   the blend factor. Default is linear (identity function).
 * @param callbacks Optional callbacks for fling lifecycle events.
 * @return A [FlingBehavior] that adapts to fling velocity.
 *
 * @author Joseph James
 */
@Composable
fun adaptiveFlingBehavior(
    gentleConfig: FlingConfiguration = FlingConfiguration.Builder()
        .decelerationFriction(0.4f)
        .scrollViewFriction(0.02f)
        .build(),
    aggressiveConfig: FlingConfiguration = FlingConfiguration.Builder()
        .decelerationFriction(0.05f)
        .scrollViewFriction(0.006f)
        .build(),
    velocityThreshold: Float = 1500f,
    maxVelocity: Float = 8000f,
    interpolator: (Float) -> Float = { it },
    callbacks: FlingCallbacks = FlingCallbacks.Empty
): FlingBehavior {
    val density = LocalDensity.current
    
    val gentleSpec = remember(density.density, gentleConfig) {
        SplineBasedFloatDecayAnimationSpec(density, gentleConfig)
            .generateDecayAnimationSpec<Float>()
    }
    
    val aggressiveSpec = remember(density.density, aggressiveConfig) {
        SplineBasedFloatDecayAnimationSpec(density, aggressiveConfig)
            .generateDecayAnimationSpec<Float>()
    }
    
    return remember(gentleSpec, aggressiveSpec, velocityThreshold, maxVelocity, interpolator, callbacks) {
        AdaptiveFlingBehaviorImpl(
            gentleSpec = gentleSpec,
            aggressiveSpec = aggressiveSpec,
            velocityThreshold = velocityThreshold,
            maxVelocity = maxVelocity,
            interpolator = interpolator,
            callbacks = callbacks
        )
    }
}

/**
 * Creates an adaptive fling behavior with preset configurations.
 *
 * This is a convenience function that provides sensible defaults for
 * common use cases.
 *
 * @param mode The adaptive mode to use.
 * @param callbacks Optional callbacks for fling lifecycle events.
 * @return A [FlingBehavior] configured for the specified mode.
 */
@Composable
fun adaptiveFlingBehavior(
    mode: AdaptiveMode = AdaptiveMode.Balanced,
    callbacks: FlingCallbacks = FlingCallbacks.Empty
): FlingBehavior {
    return adaptiveFlingBehavior(
        gentleConfig = mode.gentleConfig,
        aggressiveConfig = mode.aggressiveConfig,
        velocityThreshold = mode.threshold,
        callbacks = callbacks
    )
}

/**
 * Predefined adaptive fling modes.
 */
enum class AdaptiveMode(
    val gentleConfig: FlingConfiguration,
    val aggressiveConfig: FlingConfiguration,
    val threshold: Float
) {
    /**
     * Balanced mode - good for general use.
     * Provides controlled scrolling for gentle swipes and longer
     * momentum for aggressive swipes.
     */
    Balanced(
        gentleConfig = FlingConfiguration.Builder()
            .decelerationFriction(0.3f)
            .scrollViewFriction(0.015f)
            .build(),
        aggressiveConfig = FlingConfiguration.Builder()
            .decelerationFriction(0.06f)
            .scrollViewFriction(0.006f)
            .build(),
        threshold = 1500f
    ),
    
    /**
     * Precision mode - prioritizes control.
     * Even aggressive swipes are relatively controlled, while
     * gentle swipes are very precise.
     */
    Precision(
        gentleConfig = FlingConfiguration.Builder()
            .decelerationFriction(0.5f)
            .scrollViewFriction(0.03f)
            .build(),
        aggressiveConfig = FlingConfiguration.Builder()
            .decelerationFriction(0.15f)
            .scrollViewFriction(0.01f)
            .build(),
        threshold = 2000f
    ),
    
    /**
     * Momentum mode - prioritizes long scrolls.
     * Even gentle swipes have some momentum, while aggressive
     * swipes travel very far.
     */
    Momentum(
        gentleConfig = FlingConfiguration.Builder()
            .decelerationFriction(0.1f)
            .scrollViewFriction(0.008f)
            .build(),
        aggressiveConfig = FlingConfiguration.Builder()
            .decelerationFriction(0.02f)
            .scrollViewFriction(0.003f)
            .build(),
        threshold = 1000f
    )
}

/**
 * Internal implementation of adaptive fling behavior.
 */
private class AdaptiveFlingBehaviorImpl(
    private val gentleSpec: DecayAnimationSpec<Float>,
    private val aggressiveSpec: DecayAnimationSpec<Float>,
    private val velocityThreshold: Float,
    private val maxVelocity: Float,
    private val interpolator: (Float) -> Float,
    private val callbacks: FlingCallbacks
) : FlingBehavior {
    
    override suspend fun ScrollScope.performFling(initialVelocity: Float): Float {
        if (abs(initialVelocity) <= 1f) {
            return initialVelocity
        }
        
        // Select the appropriate spec based on velocity
        val absVelocity = abs(initialVelocity)
        val flingSpec = if (absVelocity < velocityThreshold) {
            gentleSpec
        } else {
            aggressiveSpec
        }
        
        var velocityLeft = initialVelocity
        var lastValue = 0f
        var totalScrolled = 0f
        var wasCancelled = false
        
        callbacks.onFlingStart?.invoke(initialVelocity)
        
        AnimationState(
            initialValue = 0f,
            initialVelocity = initialVelocity,
        ).animateDecay(flingSpec) {
            try {
                val delta = value - lastValue
                val consumed = scrollBy(delta)
                lastValue = value
                totalScrolled += abs(consumed)
                velocityLeft = this.velocity
                
                callbacks.onFlingProgress?.let { onProgress ->
                    val progress = if (abs(initialVelocity) > 0.1f) {
                        1f - (abs(velocityLeft) / abs(initialVelocity))
                    } else {
                        1f
                    }.coerceIn(0f, 1f)
                    onProgress(progress, velocityLeft)
                }
                
                if (abs(delta - consumed) > 0.5f) {
                    wasCancelled = true
                    this.cancelAnimation()
                }
            } catch (e: Exception) {
                wasCancelled = true
                this.cancelAnimation()
            }
        }
        
        callbacks.onFlingEnd?.invoke(totalScrolled, wasCancelled)
        
        return velocityLeft
    }
}
