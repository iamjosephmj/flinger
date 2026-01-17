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

package io.iamjosephmj.flinger.pager

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.AnimationState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDecay
import androidx.compose.animation.core.animateTo
import androidx.compose.animation.core.generateDecayAnimationSpec
import androidx.compose.animation.core.spring
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollScope
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalDensity
import io.iamjosephmj.flinger.callbacks.FlingCallbacks
import io.iamjosephmj.flinger.configs.FlingConfiguration
import io.iamjosephmj.flinger.flings.SplineBasedFloatDecayAnimationSpec
import kotlin.math.abs
import kotlin.math.roundToInt
import kotlin.math.sign

/**
 * Creates a custom fling behavior for Pager composables.
 *
 * This behavior allows you to customize the page-turn physics of
 * [HorizontalPager] and [VerticalPager] while maintaining proper
 * page snapping behavior.
 *
 * ## How It Works
 * 1. When the user flings, it calculates the target page based on velocity
 * 2. Applies Flinger's custom fling physics during the transition
 * 3. Snaps to the target page with a customizable animation
 *
 * ## Usage Example
 * ```kotlin
 * val pagerState = rememberPagerState { 10 }
 *
 * HorizontalPager(
 *     state = pagerState,
 *     flingBehavior = pagerFlingBehavior(pagerState)
 * ) { page ->
 *     PageContent(page)
 * }
 * ```
 *
 * ## Custom Page-Turn Feel
 * ```kotlin
 * HorizontalPager(
 *     state = pagerState,
 *     flingBehavior = pagerFlingBehavior(
 *         pagerState = pagerState,
 *         flingConfig = FlingConfiguration.Builder()
 *             .scrollViewFriction(0.02f)
 *             .decelerationFriction(0.15f)
 *             .build(),
 *         snapVelocityThreshold = 300f
 *     )
 * ) { page ->
 *     PageContent(page)
 * }
 * ```
 *
 * @param pagerState The state of the pager.
 * @param flingConfig Configuration for the fling physics during page transitions.
 * @param snapVelocityThreshold Minimum velocity required to move to next/previous page.
 *   Below this threshold, the pager snaps back to the current page.
 * @param lowVelocityAnimationSpec Animation spec used when velocity is below threshold.
 * @param highVelocityAnimationSpec Animation spec used when velocity is above threshold.
 * @param callbacks Optional callbacks for fling lifecycle events.
 * @return A [FlingBehavior] configured for pager use.
 *
 * @author Joseph James
 */
@Composable
fun pagerFlingBehavior(
    pagerState: PagerState,
    flingConfig: FlingConfiguration = FlingConfiguration.Builder()
        .scrollViewFriction(0.015f)
        .decelerationFriction(0.12f)
        .build(),
    snapVelocityThreshold: Float = 400f,
    lowVelocityAnimationSpec: AnimationSpec<Float> = spring(
        dampingRatio = Spring.DampingRatioNoBouncy,
        stiffness = Spring.StiffnessMedium
    ),
    highVelocityAnimationSpec: AnimationSpec<Float> = spring(
        dampingRatio = Spring.DampingRatioLowBouncy,
        stiffness = Spring.StiffnessMediumLow
    ),
    callbacks: FlingCallbacks = FlingCallbacks.Empty
): FlingBehavior {
    val density = LocalDensity.current
    
    val flingSpec = remember(density.density, flingConfig) {
        SplineBasedFloatDecayAnimationSpec(density, flingConfig)
            .generateDecayAnimationSpec<Float>()
    }
    
    return remember(
        pagerState,
        flingSpec,
        snapVelocityThreshold,
        lowVelocityAnimationSpec,
        highVelocityAnimationSpec,
        callbacks
    ) {
        PagerFlingBehaviorImpl(
            pagerState = pagerState,
            flingDecay = flingSpec,
            snapVelocityThreshold = snapVelocityThreshold,
            lowVelocityAnimationSpec = lowVelocityAnimationSpec,
            highVelocityAnimationSpec = highVelocityAnimationSpec,
            callbacks = callbacks
        )
    }
}

/**
 * Configuration presets for common pager behaviors.
 */
object PagerFlingPresets {
    
    /**
     * Standard pager feel - balanced page transitions.
     */
    val Standard = PagerFlingConfig(
        flingConfig = FlingConfiguration.Builder()
            .scrollViewFriction(0.015f)
            .decelerationFriction(0.12f)
            .build(),
        snapVelocityThreshold = 400f
    )
    
    /**
     * iOS-style pager with higher resistance.
     */
    val IOS = PagerFlingConfig(
        flingConfig = FlingConfiguration.Builder()
            .scrollViewFriction(0.025f)
            .decelerationFriction(0.2f)
            .build(),
        snapVelocityThreshold = 500f
    )
    
    /**
     * Snappy pager with quick page turns.
     */
    val Snappy = PagerFlingConfig(
        flingConfig = FlingConfiguration.Builder()
            .scrollViewFriction(0.02f)
            .decelerationFriction(0.25f)
            .build(),
        snapVelocityThreshold = 300f
    )
    
    /**
     * Smooth pager with longer page transitions.
     */
    val Smooth = PagerFlingConfig(
        flingConfig = FlingConfiguration.Builder()
            .scrollViewFriction(0.008f)
            .decelerationFriction(0.08f)
            .build(),
        snapVelocityThreshold = 350f
    )
}

/**
 * Configuration for pager fling behavior.
 */
data class PagerFlingConfig(
    val flingConfig: FlingConfiguration,
    val snapVelocityThreshold: Float
)

/**
 * Creates a pager fling behavior from a preset configuration.
 */
@Composable
fun pagerFlingBehavior(
    pagerState: PagerState,
    preset: PagerFlingConfig = PagerFlingPresets.Standard,
    callbacks: FlingCallbacks = FlingCallbacks.Empty
): FlingBehavior = pagerFlingBehavior(
    pagerState = pagerState,
    flingConfig = preset.flingConfig,
    snapVelocityThreshold = preset.snapVelocityThreshold,
    callbacks = callbacks
)

/**
 * Internal implementation of pager fling behavior.
 */
private class PagerFlingBehaviorImpl(
    private val pagerState: PagerState,
    private val flingDecay: androidx.compose.animation.core.DecayAnimationSpec<Float>,
    private val snapVelocityThreshold: Float,
    private val lowVelocityAnimationSpec: AnimationSpec<Float>,
    private val highVelocityAnimationSpec: AnimationSpec<Float>,
    private val callbacks: FlingCallbacks
) : FlingBehavior {
    
    override suspend fun ScrollScope.performFling(initialVelocity: Float): Float {
        callbacks.onFlingStart?.invoke(initialVelocity)
        
        val absVelocity = abs(initialVelocity)
        var totalScrolled = 0f
        var wasCancelled = false
        
        // Determine target page based on velocity and current offset
        val currentPage = pagerState.currentPage
        val currentPageOffset = pagerState.currentPageOffsetFraction
        val pageSize = pagerState.layoutInfo.pageSize.takeIf { it > 0 } ?: return initialVelocity
        
        val targetPage = when {
            absVelocity >= snapVelocityThreshold -> {
                // High velocity - move to next/previous page
                if (initialVelocity > 0) {
                    (currentPage - 1).coerceAtLeast(0)
                } else {
                    (currentPage + 1).coerceAtMost(pagerState.pageCount - 1)
                }
            }
            abs(currentPageOffset) > 0.5f -> {
                // Low velocity but past midpoint - move to adjacent page
                if (currentPageOffset > 0) {
                    (currentPage + 1).coerceAtMost(pagerState.pageCount - 1)
                } else {
                    (currentPage - 1).coerceAtLeast(0)
                }
            }
            else -> {
                // Low velocity and before midpoint - snap back to current
                currentPage
            }
        }
        
        // Calculate distance to target page
        val targetOffset = when {
            targetPage > currentPage -> {
                // Moving forward
                ((1f - currentPageOffset) * pageSize)
            }
            targetPage < currentPage -> {
                // Moving backward
                ((-1f - currentPageOffset) * pageSize)
            }
            else -> {
                // Snapping back to current page
                (-currentPageOffset * pageSize)
            }
        }
        
        // Choose animation spec based on velocity
        val animationSpec = if (absVelocity >= snapVelocityThreshold) {
            highVelocityAnimationSpec
        } else {
            lowVelocityAnimationSpec
        }
        
        // Animate to target
        var lastValue = 0f
        
        try {
            AnimationState(
                initialValue = 0f,
                initialVelocity = initialVelocity
            ).animateTo(
                targetValue = targetOffset,
                animationSpec = animationSpec
            ) {
                val delta = value - lastValue
                val consumed = scrollBy(delta)
                lastValue = value
                totalScrolled += abs(consumed)
                
                // Report progress
                callbacks.onFlingProgress?.let { onProgress ->
                    val progress = if (abs(targetOffset) > 0.1f) {
                        abs(lastValue) / abs(targetOffset)
                    } else {
                        1f
                    }.coerceIn(0f, 1f)
                    onProgress(progress, velocity)
                }
                
                // Stop if scroll is blocked
                if (abs(delta) > 0.1f && abs(delta - consumed) > 0.5f) {
                    wasCancelled = true
                    cancelAnimation()
                }
            }
        } catch (e: Exception) {
            wasCancelled = true
        }
        
        callbacks.onFlingEnd?.invoke(totalScrolled, wasCancelled)
        
        return 0f // Pager should always settle at a page boundary
    }
}
