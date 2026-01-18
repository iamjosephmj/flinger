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

import androidx.compose.animation.core.DecayAnimationSpec
import androidx.compose.animation.core.generateDecayAnimationSpec
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalDensity
import io.iamjosephmj.flinger.callbacks.FlingCallbacks
import io.iamjosephmj.flinger.configs.FlingConfiguration

/**
 * Creates a customizable fling behavior for scrollable composables.
 *
 * This is the primary entry point for using Flinger's custom fling physics.
 * It creates a [FlingBehavior] that can be passed to any scrollable composable
 * like [LazyColumn], [LazyRow], or scrollable modifiers.
 *
 * ## Basic Usage
 * ```kotlin
 * LazyColumn(
 *     flingBehavior = flingBehavior()
 * ) {
 *     items(100) { Text("Item $it") }
 * }
 * ```
 *
 * ## Custom Configuration
 * ```kotlin
 * LazyColumn(
 *     flingBehavior = flingBehavior(
 *         scrollConfiguration = FlingConfiguration.Builder()
 *             .scrollViewFriction(0.04f)
 *             .decelerationFriction(0.1f)
 *             .build()
 *     )
 * ) {
 *     items(100) { Text("Item $it") }
 * }
 * ```
 *
 * ## With Callbacks
 * ```kotlin
 * LazyColumn(
 *     flingBehavior = flingBehavior(
 *         callbacks = FlingCallbacks(
 *             onFlingStart = { velocity -> Log.d("Fling", "Started: $velocity") },
 *             onFlingEnd = { distance, cancelled -> Log.d("Fling", "Ended: $distance") }
 *         )
 *     )
 * ) {
 *     items(100) { Text("Item $it") }
 * }
 * ```
 *
 * @param scrollConfiguration The configuration for fling physics. Defaults to
 *   a smooth, balanced configuration.
 * @param callbacks Optional callbacks for fling lifecycle events.
 * @return A [FlingBehavior] with the specified configuration.
 *
 * @author Joseph James
 */
@Composable
fun flingBehavior(
    scrollConfiguration: FlingConfiguration = FlingConfiguration.Default,
    callbacks: FlingCallbacks = FlingCallbacks.Empty
): FlingBehavior {
    val flingSpec = rememberSplineBasedDecay<Float>(scrollConfiguration)
    return remember(flingSpec, callbacks) {
        FlingerFlingBehavior(flingSpec, callbacks)
    }
}

/**
 * Creates and remembers a spline-based decay animation spec.
 *
 * This function internally updates the calculation when density changes,
 * but the reference to the returned spec remains stable across calls.
 *
 * @param scrollConfiguration The fling configuration to use.
 * @return A [DecayAnimationSpec] configured with the specified settings.
 */
@Composable
fun <T> rememberSplineBasedDecay(scrollConfiguration: FlingConfiguration): DecayAnimationSpec<T> {
    val density = LocalDensity.current
    return remember(density.density, scrollConfiguration) {
        SplineBasedFloatDecayAnimationSpec(
            density,
            scrollConfiguration
        ).generateDecayAnimationSpec()
    }
}
