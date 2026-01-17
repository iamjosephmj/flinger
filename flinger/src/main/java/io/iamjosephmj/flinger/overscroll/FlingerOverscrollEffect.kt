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

package io.iamjosephmj.flinger.overscroll

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.Velocity
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.roundToInt
import kotlin.math.sign

/**
 * Applies a custom overscroll effect to scrollable content.
 *
 * This modifier adds a spring-back effect when scrolling past the content
 * boundaries, providing visual feedback that the user has reached the edge.
 *
 * ## How It Works
 * When the user scrolls past the content boundary:
 * 1. The content translates in the scroll direction with resistance
 * 2. The resistance increases as the user pulls further
 * 3. When released, the content springs back to its original position
 *
 * ## Usage Example
 * ```kotlin
 * LazyColumn(
 *     modifier = Modifier.flingerOverscroll()
 * ) {
 *     items(100) { Text("Item $it") }
 * }
 * ```
 *
 * ## Custom Configuration
 * ```kotlin
 * LazyColumn(
 *     modifier = Modifier.flingerOverscroll(
 *         config = OverscrollConfig.Bouncy,
 *         orientation = OverscrollOrientation.Vertical
 *     )
 * ) {
 *     items(100) { Text("Item $it") }
 * }
 * ```
 *
 * @param config Configuration for the overscroll effect.
 * @param orientation The scroll orientation (vertical or horizontal).
 * @param enabled Whether the overscroll effect is enabled.
 * @return A [Modifier] with the overscroll effect applied.
 *
 * @author Joseph James
 */
fun Modifier.flingerOverscroll(
    config: OverscrollConfig = OverscrollConfig.Default,
    orientation: OverscrollOrientation = OverscrollOrientation.Vertical,
    enabled: Boolean = true
): Modifier = composed {
    if (!enabled || config.maxOverscrollDistance.value <= 0f) {
        return@composed this
    }
    
    val density = LocalDensity.current
    val maxOverscrollPx = with(density) { config.maxOverscrollDistance.toPx() }
    val scope = rememberCoroutineScope()
    
    val overscrollOffset = remember { Animatable(0f) }
    
    val nestedScrollConnection = remember(config, orientation, maxOverscrollPx) {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                // If we're overscrolled and scrolling back toward center, consume some scroll
                val delta = if (orientation == OverscrollOrientation.Vertical) {
                    available.y
                } else {
                    available.x
                }
                
                val currentOffset = overscrollOffset.value
                
                // If overscrolled and scrolling back
                if (currentOffset != 0f && sign(delta) != sign(currentOffset)) {
                    val newOffset = currentOffset + delta
                    
                    // If this would cross zero, only consume what's needed
                    if (sign(newOffset) != sign(currentOffset)) {
                        scope.launch {
                            overscrollOffset.snapTo(0f)
                        }
                        val consumed = -currentOffset
                        return if (orientation == OverscrollOrientation.Vertical) {
                            Offset(0f, consumed)
                        } else {
                            Offset(consumed, 0f)
                        }
                    } else {
                        scope.launch {
                            overscrollOffset.snapTo(newOffset)
                        }
                        return available
                    }
                }
                
                return Offset.Zero
            }
            
            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                val delta = if (orientation == OverscrollOrientation.Vertical) {
                    available.y
                } else {
                    available.x
                }
                
                // If there's unconsumed scroll, we're at a boundary
                if (delta != 0f) {
                    val currentOffset = overscrollOffset.value
                    
                    // Apply resistance based on current offset
                    val resistanceFraction = 1f - (abs(currentOffset) / maxOverscrollPx)
                        .coerceIn(0f, 1f)
                    val resistedDelta = delta * config.resistanceFactor * resistanceFraction
                    
                    val newOffset = (currentOffset + resistedDelta)
                        .coerceIn(-maxOverscrollPx, maxOverscrollPx)
                    
                    scope.launch {
                        overscrollOffset.snapTo(newOffset)
                    }
                    
                    return available // Consume all available
                }
                
                return Offset.Zero
            }
            
            override suspend fun onPreFling(available: Velocity): Velocity {
                // If we're overscrolled, don't let content fling - animate back instead
                if (overscrollOffset.value != 0f) {
                    overscrollOffset.animateTo(
                        targetValue = 0f,
                        animationSpec = spring(
                            dampingRatio = config.dampingRatio,
                            stiffness = config.springStiffness
                        )
                    )
                    return available // Consume velocity
                }
                return Velocity.Zero
            }
            
            override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
                // Spring back if we're overscrolled after fling
                if (overscrollOffset.value != 0f) {
                    overscrollOffset.animateTo(
                        targetValue = 0f,
                        animationSpec = spring(
                            dampingRatio = config.dampingRatio,
                            stiffness = config.springStiffness
                        )
                    )
                }
                return Velocity.Zero
            }
        }
    }
    
    this
        .nestedScroll(nestedScrollConnection)
        .offset {
            if (orientation == OverscrollOrientation.Vertical) {
                IntOffset(0, overscrollOffset.value.roundToInt())
            } else {
                IntOffset(overscrollOffset.value.roundToInt(), 0)
            }
        }
}

/**
 * Orientation for the overscroll effect.
 */
enum class OverscrollOrientation {
    /**
     * Vertical overscroll (for vertical scrolling content).
     */
    Vertical,
    
    /**
     * Horizontal overscroll (for horizontal scrolling content).
     */
    Horizontal
}

/**
 * A container composable that applies overscroll effect to its content.
 *
 * This is an alternative to the modifier-based API for cases where you
 * prefer a composable wrapper.
 *
 * ## Usage Example
 * ```kotlin
 * FlingerOverscrollContainer(
 *     config = OverscrollConfig.Bouncy
 * ) {
 *     LazyColumn {
 *         items(100) { Text("Item $it") }
 *     }
 * }
 * ```
 *
 * @param config Configuration for the overscroll effect.
 * @param orientation The scroll orientation.
 * @param enabled Whether the overscroll effect is enabled.
 * @param content The scrollable content.
 */
@Composable
fun FlingerOverscrollContainer(
    config: OverscrollConfig = OverscrollConfig.Default,
    orientation: OverscrollOrientation = OverscrollOrientation.Vertical,
    enabled: Boolean = true,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier.flingerOverscroll(
            config = config,
            orientation = orientation,
            enabled = enabled
        )
    ) {
        content()
    }
}
