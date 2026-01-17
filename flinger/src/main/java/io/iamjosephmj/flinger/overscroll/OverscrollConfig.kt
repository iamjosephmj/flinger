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

import androidx.compose.animation.core.Spring
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Configuration for overscroll/spring-back effects.
 *
 * This class defines the physics and visual properties of the overscroll
 * effect that occurs when scrolling past the content boundaries.
 *
 * ## Usage Example
 * ```kotlin
 * val config = OverscrollConfig(
 *     maxOverscrollDistance = 120.dp,
 *     springStiffness = Spring.StiffnessLow,
 *     dampingRatio = Spring.DampingRatioMediumBouncy
 * )
 *
 * LazyColumn(
 *     modifier = Modifier.flingerOverscroll(config)
 * ) {
 *     items(100) { Text("Item $it") }
 * }
 * ```
 *
 * @property maxOverscrollDistance Maximum distance the content can be pulled past its bounds.
 * @property springStiffness Controls how quickly the spring returns to rest.
 *   Higher values = faster return. Use [Spring] constants for common values.
 * @property dampingRatio Controls the bounciness of the spring.
 *   - 0.0 = no damping (oscillates forever)
 *   - 1.0 = critically damped (returns smoothly without bounce)
 *   - Values between 0-1 = underdamped (bounces)
 *   Use [Spring] constants for common values.
 * @property resistanceFactor Controls how much resistance is applied when overscrolling.
 *   Higher values = more resistance (harder to pull past bounds).
 *
 * @author Joseph James
 */
data class OverscrollConfig(
    val maxOverscrollDistance: Dp = 100.dp,
    val springStiffness: Float = Spring.StiffnessLow,
    val dampingRatio: Float = Spring.DampingRatioMediumBouncy,
    val resistanceFactor: Float = 0.5f
) {
    companion object {
        /**
         * Default overscroll configuration with subtle bounce.
         */
        val Default = OverscrollConfig()
        
        /**
         * iOS-style overscroll with rubber-band effect.
         * More resistance, less bounce.
         */
        val IOS = OverscrollConfig(
            maxOverscrollDistance = 80.dp,
            springStiffness = Spring.StiffnessMedium,
            dampingRatio = Spring.DampingRatioNoBouncy,
            resistanceFactor = 0.4f
        )
        
        /**
         * Bouncy overscroll effect for playful UIs.
         */
        val Bouncy = OverscrollConfig(
            maxOverscrollDistance = 150.dp,
            springStiffness = Spring.StiffnessLow,
            dampingRatio = Spring.DampingRatioLowBouncy,
            resistanceFactor = 0.6f
        )
        
        /**
         * Stiff overscroll with minimal movement.
         * Good for precision UIs.
         */
        val Stiff = OverscrollConfig(
            maxOverscrollDistance = 40.dp,
            springStiffness = Spring.StiffnessHigh,
            dampingRatio = Spring.DampingRatioNoBouncy,
            resistanceFactor = 0.3f
        )
        
        /**
         * No overscroll effect - content stops at boundaries.
         */
        val None = OverscrollConfig(
            maxOverscrollDistance = 0.dp,
            resistanceFactor = 0f
        )
    }
}
