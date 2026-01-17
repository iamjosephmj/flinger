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
import androidx.compose.animation.core.AnimationState
import androidx.compose.animation.core.DecayAnimationSpec
import androidx.compose.animation.core.animateDecay
import androidx.compose.animation.core.animateTo
import androidx.compose.animation.core.generateDecayAnimationSpec
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalDensity
import io.iamjosephmj.flinger.callbacks.FlingCallbacks
import io.iamjosephmj.flinger.configs.FlingConfiguration
import io.iamjosephmj.flinger.flings.SplineBasedFloatDecayAnimationSpec
import kotlin.math.abs

/**
 * Key class for remember to reduce equality checks.
 * Groups related configuration values.
 */
private data class SnapBehaviorKey(
    val snapPosition: SnapPosition,
    val velocityThreshold: Float,
    val smoothFusion: Boolean,
    val fusionVelocityRatio: Float
)

/**
 * Creates a snap-to-item fling behavior for LazyList composables.
 *
 * This behavior performs a normal fling animation and then snaps to the nearest
 * item boundary based on the specified [snapPosition]. It's perfect for carousels,
 * galleries, and any scrollable list where items should align to specific positions.
 *
 * ## Basic Usage
 * ```kotlin
 * val listState = rememberLazyListState()
 * 
 * LazyRow(
 *     state = listState,
 *     flingBehavior = snapFlingBehavior(listState)
 * ) {
 *     items(100) { CarouselCard(it) }
 * }
 * ```
 *
 * ## Center-Aligned Carousel with Smooth Animation
 * ```kotlin
 * val listState = rememberLazyListState()
 * 
 * LazyRow(
 *     state = listState,
 *     flingBehavior = snapFlingBehavior(
 *         lazyListState = listState,
 *         snapPosition = SnapPosition.Center,
 *         snapAnimation = SnapAnimationConfig.Smooth
 *     ),
 *     contentPadding = PaddingValues(horizontal = 48.dp)
 * ) {
 *     items(photos) { photo ->
 *         PhotoCard(photo, modifier = Modifier.fillParentMaxWidth(0.8f))
 *     }
 * }
 * ```
 *
 * ## Dynamic Smooth Fusion Mode
 * ```kotlin
 * // Enable smooth fusion - snap starts when velocity decays, not after fling stops
 * LazyRow(
 *     flingBehavior = snapFlingBehavior(
 *         lazyListState = listState,
 *         smoothFusion = true,
 *         fusionVelocityRatio = 0.15f  // Fusion at 15% of initial velocity
 *     )
 * ) { ... }
 * ```
 *
 * @param lazyListState The state of the LazyList to snap.
 * @param snapPosition Where items should snap to within the viewport.
 * @param flingConfig Optional fling configuration for the initial fling phase.
 * @param snapAnimation Configuration for the snap animation feel.
 * @param velocityThreshold Minimum velocity to snap to next/previous item.
 * @param smoothFusion When true, dynamically fuses fling into snap when velocity decays.
 * @param fusionVelocityRatio When smoothFusion is true, controls when fusion kicks in.
 *   Range 0.0-1.0. Lower = earlier fusion, higher = later fusion. Default: 0.15
 * @param callbacks Optional callbacks for fling lifecycle events.
 * @return A [FlingBehavior] that snaps to item boundaries.
 *
 * @author Joseph James
 */
@Composable
fun snapFlingBehavior(
    lazyListState: LazyListState,
    snapPosition: SnapPosition = SnapPosition.Start,
    flingConfig: FlingConfiguration = FlingConfiguration.Default,
    snapAnimation: SnapAnimationConfig = SnapAnimationConfig.Smooth,
    velocityThreshold: Float = 400f,
    smoothFusion: Boolean = false,
    fusionVelocityRatio: Float = 0.15f,
    callbacks: FlingCallbacks = FlingCallbacks.Empty
): FlingBehavior {
    val density = LocalDensity.current
    
    val flingSpec = remember(density.density, flingConfig) {
        SplineBasedFloatDecayAnimationSpec(density, flingConfig)
            .generateDecayAnimationSpec<Float>()
    }
    
    val snapAnimationSpec = remember(snapAnimation) {
        snapAnimation.createAnimationSpec<Float>()
    }
    
    // Group config into a key to reduce equality checks (4 keys instead of 8)
    val behaviorKey = remember(snapPosition, velocityThreshold, smoothFusion, fusionVelocityRatio) {
        SnapBehaviorKey(snapPosition, velocityThreshold, smoothFusion, fusionVelocityRatio.coerceIn(0.05f, 0.5f))
    }
    
    return remember(lazyListState, flingSpec, snapAnimationSpec, behaviorKey, callbacks) {
        SnapFlingBehaviorImpl(
            lazyListState = lazyListState,
            snapPosition = behaviorKey.snapPosition,
            flingDecay = flingSpec,
            snapAnimationSpec = snapAnimationSpec,
            velocityThreshold = behaviorKey.velocityThreshold,
            smoothFusion = behaviorKey.smoothFusion,
            fusionVelocityRatio = behaviorKey.fusionVelocityRatio,
            callbacks = callbacks
        )
    }
}

/**
 * Creates a snap-to-item fling behavior using a [SnapConfig].
 *
 * @param lazyListState The state of the LazyList to snap.
 * @param config Configuration for snap behavior including animation settings.
 * @param flingConfig Optional fling configuration for the initial fling phase.
 * @param callbacks Optional callbacks for fling lifecycle events.
 * @return A [FlingBehavior] that snaps to item boundaries.
 */
@Composable
fun snapFlingBehavior(
    lazyListState: LazyListState,
    config: SnapConfig = SnapConfig(),
    flingConfig: FlingConfiguration = FlingConfiguration.Default,
    callbacks: FlingCallbacks = FlingCallbacks.Empty
): FlingBehavior = snapFlingBehavior(
    lazyListState = lazyListState,
    snapPosition = config.snapPosition,
    flingConfig = flingConfig,
    snapAnimation = config.snapAnimation,
    velocityThreshold = config.velocityThreshold,
    smoothFusion = config.smoothFusion,
    fusionVelocityRatio = config.fusionVelocityRatio,
    callbacks = callbacks
)

/**
 * Internal implementation of snap fling behavior.
 * 
 * Performance optimized:
 * - Pre-computes absolute velocity values
 * - Removed try-catch from animation hot path
 * - Uses minByOrNull for cleaner snap offset calculation
 */
private class SnapFlingBehaviorImpl(
    private val lazyListState: LazyListState,
    private val snapPosition: SnapPosition,
    private val flingDecay: DecayAnimationSpec<Float>,
    private val snapAnimationSpec: AnimationSpec<Float>,
    private val velocityThreshold: Float,
    private val smoothFusion: Boolean,
    private val fusionVelocityRatio: Float,
    private val callbacks: FlingCallbacks
) : FlingBehavior {
    
    override suspend fun ScrollScope.performFling(initialVelocity: Float): Float {
        // Notify fling start
        callbacks.onFlingStart?.invoke(initialVelocity)
        
        var totalScrolled = 0f
        var velocityLeft = initialVelocity
        var wasCancelled = false
        
        if (smoothFusion) {
            // Smooth Fusion Mode: Let fling complete naturally, then seamlessly blend into snap
            performSmoothFusionFling(
                initialVelocity = initialVelocity,
                onProgress = { scrolled, velocity, cancelled ->
                    totalScrolled = scrolled
                    velocityLeft = velocity
                    wasCancelled = cancelled
                }
            )
        } else {
            // Standard Mode: Two distinct phases (fling then snap)
            performStandardFling(
                initialVelocity = initialVelocity,
                onProgress = { scrolled, velocity, cancelled ->
                    totalScrolled = scrolled
                    velocityLeft = velocity
                    wasCancelled = cancelled
                }
            )
        }
        
        // Notify fling end
        callbacks.onFlingEnd?.invoke(totalScrolled, wasCancelled)
        
        return 0f // After snap, velocity should be 0
    }
    
    /**
     * Standard fling mode with two distinct phases.
     * Performance optimized: pre-computes abs values, no try-catch in hot path.
     */
    private suspend fun ScrollScope.performStandardFling(
        initialVelocity: Float,
        onProgress: (Float, Float, Boolean) -> Unit
    ) {
        // Pre-compute absolute value once
        val absInitialVelocity = abs(initialVelocity)
        
        var velocityLeft = initialVelocity
        var lastValue = 0f
        var totalScrolled = 0f
        var wasCancelled = false
        
        // Phase 1: Perform initial fling if velocity is significant
        if (absInitialVelocity > 1f) {
            AnimationState(
                initialValue = 0f,
                initialVelocity = initialVelocity,
            ).animateDecay(flingDecay) {
                val delta = value - lastValue
                val consumed = scrollBy(delta)
                lastValue = value
                totalScrolled += abs(consumed)
                velocityLeft = this.velocity
                
                // Pre-compute abs for velocity comparison
                val absVelocityLeft = abs(velocityLeft)
                
                // Report progress (only if callback exists)
                callbacks.onFlingProgress?.let { onProgressCallback ->
                    val progress = if (absInitialVelocity > 0.1f) {
                        1f - (absVelocityLeft / absInitialVelocity)
                    } else {
                        1f
                    }.coerceIn(0f, 1f)
                    onProgressCallback(progress * 0.7f, velocityLeft)
                }
                
                // Stop fling if scroll is consumed (hit boundary)
                val unconsumed = abs(delta - consumed)
                if (unconsumed > 0.5f) {
                    cancelAnimation()
                }
            }
        }
        
        // Phase 2: Snap to nearest item with smooth animation
        val snapOffset = calculateSnapOffset()
        val absSnapOffset = abs(snapOffset)
        
        if (absSnapOffset > 0.5f) {
            var snapScrolled = 0f
            val absVelocityLeft = abs(velocityLeft)
            
            // Use the remaining velocity to make snap feel more natural
            val snapInitialVelocity = if (absVelocityLeft > 100f) {
                velocityLeft * 0.3f
            } else {
                0f
            }
            
            AnimationState(
                initialValue = 0f,
                initialVelocity = snapInitialVelocity
            ).animateTo(
                targetValue = snapOffset,
                animationSpec = snapAnimationSpec
            ) {
                val delta = value - snapScrolled
                val consumed = scrollBy(delta)
                snapScrolled = value
                totalScrolled += abs(consumed)
                
                // Report progress for snap phase
                callbacks.onFlingProgress?.let { onProgressCallback ->
                    val snapProgress = if (absSnapOffset > 0.1f) {
                        abs(snapScrolled) / absSnapOffset
                    } else {
                        1f
                    }.coerceIn(0f, 1f)
                    onProgressCallback(0.7f + snapProgress * 0.3f, velocity)
                }
                
                val unconsumed = abs(delta - consumed)
                if (unconsumed > 0.5f) {
                    wasCancelled = true
                    cancelAnimation()
                }
            }
        }
        
        onProgress(totalScrolled, velocityLeft, wasCancelled)
    }
    
    /**
     * Smooth fusion mode: Dynamically blends fling into snap when velocity decays.
     * Performance optimized: pre-computes abs values, no try-catch in hot path.
     */
    private suspend fun ScrollScope.performSmoothFusionFling(
        initialVelocity: Float,
        onProgress: (Float, Float, Boolean) -> Unit
    ) {
        // Pre-compute absolute value once
        val absInitialVelocity = abs(initialVelocity)
        
        var velocityLeft = initialVelocity
        var lastValue = 0f
        var totalScrolled = 0f
        var wasCancelled = false
        var transitionedToSnap = false
        
        // Fusion threshold: when velocity drops below this, start transitioning to snap
        val fusionThreshold = (absInitialVelocity * fusionVelocityRatio).coerceIn(100f, 800f)
        
        // Phase 1: Fling until velocity decays enough to start fusion
        if (absInitialVelocity > 1f) {
            AnimationState(
                initialValue = 0f,
                initialVelocity = initialVelocity,
            ).animateDecay(flingDecay) {
                val delta = value - lastValue
                val consumed = scrollBy(delta)
                lastValue = value
                totalScrolled += abs(consumed)
                velocityLeft = this.velocity
                
                // Pre-compute abs for velocity comparison
                val absVelocityLeft = abs(velocityLeft)
                
                // Report progress
                callbacks.onFlingProgress?.let { onProgressCallback ->
                    val progress = if (absInitialVelocity > 0.1f) {
                        1f - (absVelocityLeft / absInitialVelocity)
                    } else {
                        1f
                    }.coerceIn(0f, 1f)
                    onProgressCallback(progress * 0.75f, velocityLeft)
                }
                
                // DYNAMIC FUSION: When velocity decays below threshold, transition to snap
                if (absVelocityLeft < fusionThreshold && !transitionedToSnap) {
                    transitionedToSnap = true
                    cancelAnimation()
                }
                
                // Stop fling if scroll is consumed (hit boundary)
                val unconsumed = abs(delta - consumed)
                if (unconsumed > 0.5f) {
                    cancelAnimation()
                }
            }
        }
        
        // Phase 2: Dynamic fusion snap
        val snapOffset = calculateSnapOffset()
        val absSnapOffset = abs(snapOffset)
        
        if (absSnapOffset > 0.5f) {
            var snapScrolled = 0f
            val absVelocityLeft = abs(velocityLeft)
            
            // In dynamic fusion, we use the ACTUAL remaining velocity
            val fusionVelocity = if (transitionedToSnap && absVelocityLeft > 50f) {
                velocityLeft * 0.8f
            } else if (absVelocityLeft > 30f) {
                velocityLeft * 0.5f
            } else {
                // Near-zero velocity - use proportional approach velocity
                val direction = if (snapOffset > 0) 1f else -1f
                direction * absSnapOffset * 2f
            }
            
            AnimationState(
                initialValue = 0f,
                initialVelocity = fusionVelocity
            ).animateTo(
                targetValue = snapOffset,
                animationSpec = snapAnimationSpec
            ) {
                val delta = value - snapScrolled
                val consumed = scrollBy(delta)
                snapScrolled = value
                totalScrolled += abs(consumed)
                
                // Report progress for fusion snap phase
                callbacks.onFlingProgress?.let { onProgressCallback ->
                    val snapProgress = if (absSnapOffset > 0.1f) {
                        abs(snapScrolled) / absSnapOffset
                    } else {
                        1f
                    }.coerceIn(0f, 1f)
                    onProgressCallback(0.75f + snapProgress * 0.25f, velocity)
                }
                
                val unconsumed = abs(delta - consumed)
                if (unconsumed > 0.5f) {
                    wasCancelled = true
                    cancelAnimation()
                }
            }
        }
        
        onProgress(totalScrolled, velocityLeft, wasCancelled)
    }
    
    /**
     * Calculates the offset needed to snap to the nearest item.
     * Performance optimized: uses minByOrNull for cleaner, efficient iteration.
     */
    private fun calculateSnapOffset(): Float {
        val layoutInfo = lazyListState.layoutInfo
        val visibleItems = layoutInfo.visibleItemsInfo
        
        if (visibleItems.isEmpty()) return 0f
        
        val viewportStart = layoutInfo.viewportStartOffset
        val viewportEnd = layoutInfo.viewportEndOffset
        val viewportSize = viewportEnd - viewportStart
        
        // Pre-compute target offset once
        val targetOffset = when (snapPosition) {
            SnapPosition.Start -> viewportStart
            SnapPosition.Center -> viewportStart + viewportSize / 2
            SnapPosition.End -> viewportEnd
        }
        
        // Find closest item using minByOrNull - cleaner and avoids manual tracking
        val closestItem = visibleItems.minByOrNull { item ->
            val itemSnapPoint = when (snapPosition) {
                SnapPosition.Start -> item.offset
                SnapPosition.Center -> item.offset + item.size / 2
                SnapPosition.End -> item.offset + item.size
            }
            abs(itemSnapPoint - targetOffset)
        } ?: return 0f
        
        // Calculate offset to snap this item to the target position
        return when (snapPosition) {
            SnapPosition.Start -> {
                (closestItem.offset - viewportStart).toFloat()
            }
            SnapPosition.Center -> {
                val itemCenter = closestItem.offset + closestItem.size / 2
                val viewportCenter = viewportStart + viewportSize / 2
                (itemCenter - viewportCenter).toFloat()
            }
            SnapPosition.End -> {
                val itemEnd = closestItem.offset + closestItem.size
                (itemEnd - viewportEnd).toFloat()
            }
        }
    }
}
