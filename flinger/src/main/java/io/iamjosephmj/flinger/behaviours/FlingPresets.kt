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

package io.iamjosephmj.flinger.behaviours

import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.runtime.Composable
import io.iamjosephmj.flinger.configs.FlingConfiguration
import io.iamjosephmj.flinger.flings.flingBehavior

/**
 * A collection of pre-configured fling behaviors for common use cases.
 *
 * Each preset is designed for specific scenarios and provides a distinct
 * scroll feel. Use these presets as starting points or directly in your
 * scrollable composables.
 *
 * ## Usage Example
 * ```kotlin
 * LazyColumn(
 *     flingBehavior = FlingPresets.iOSStyle()
 * ) {
 *     items(100) { Text("Item $it") }
 * }
 * ```
 *
 * @author Joseph James
 */
object FlingPresets {

    /**
     * Returns the native Android fling behavior.
     *
     * Use this for comparison or when you want standard Android scroll physics.
     *
     * **Best for:**
     * - A/B testing against custom behaviors
     * - Matching native Android feel
     * - Baseline comparisons
     *
     * @return The default Android [FlingBehavior]
     */
    @Composable
    fun androidNative(): FlingBehavior = ScrollableDefaults.flingBehavior()

    /**
     * A smooth, balanced fling behavior.
     *
     * This is the default Flinger behavior with carefully tuned parameters
     * for a smooth, natural-feeling scroll experience.
     *
     * **Best for:**
     * - General purpose scrolling
     * - Most list-based UIs
     * - When unsure which preset to use
     *
     * **Configuration:**
     * - Default friction and deceleration values
     * - Balanced momentum and stopping distance
     *
     * @return A [FlingBehavior] with smooth, balanced physics
     */
    @Composable
    fun smooth(): FlingBehavior = flingBehavior(
        scrollConfiguration = FlingConfiguration.Builder().build()
    )

    /**
     * iOS-style fling behavior with higher friction.
     *
     * Mimics the scroll physics found in iOS, providing a more
     * controlled, higher-friction scroll experience.
     *
     * **Best for:**
     * - Cross-platform apps wanting iOS consistency
     * - Users familiar with iOS scroll behavior
     * - Photo browsers and media apps
     *
     * **Configuration:**
     * - Higher scroll friction (0.04)
     * - Shorter scroll distance
     * - More controlled momentum
     *
     * @return A [FlingBehavior] with iOS-like physics
     */
    @Composable
    fun iOSStyle(): FlingBehavior = flingBehavior(
        scrollConfiguration = FlingConfiguration.Builder()
            .scrollViewFriction(0.04f)
            .build()
    )

    /**
     * Modified spline behavior for unique scroll feel.
     *
     * Uses a different spline inflection point to create a distinct
     * scroll curve that feels different from standard Android or iOS.
     *
     * **Best for:**
     * - Apps wanting a unique scroll signature
     * - Creative/artistic applications
     * - Experimentation with scroll physics
     *
     * **Configuration:**
     * - Modified spline inflection (0.16)
     * - Different acceleration/deceleration curve
     *
     * @return A [FlingBehavior] with modified spline physics
     */
    @Composable
    fun smoothCurve(): FlingBehavior = flingBehavior(
        scrollConfiguration = FlingConfiguration.Builder()
            .splineInflection(0.16f)
            .build()
    )

    /**
     * Quick-stop fling behavior for precision scrolling.
     *
     * High deceleration friction causes the scroll to stop quickly,
     * giving users precise control over scroll position.
     *
     * **Best for:**
     * - Text-heavy content (articles, documents)
     * - Lists requiring precise item selection
     * - Accessibility-focused UIs
     * - Settings screens
     *
     * **Configuration:**
     * - High deceleration friction (0.5)
     * - Short scroll distance
     * - Quick stop after gesture ends
     *
     * @return A [FlingBehavior] with quick-stop physics
     */
    @Composable
    fun quickStop(): FlingBehavior = flingBehavior(
        scrollConfiguration = FlingConfiguration.Builder()
            .decelerationFriction(0.5f)
            .build()
    )

    /**
     * Bouncy, playful fling behavior.
     *
     * Combines high deceleration friction with modified spline
     * for a bouncy, responsive feel.
     *
     * **Best for:**
     * - Games and playful UIs
     * - Children's apps
     * - Interactive experiences
     * - Apps with playful branding
     *
     * **Configuration:**
     * - High deceleration friction (0.6)
     * - Modified spline inflection (0.4)
     * - Responsive, bouncy feel
     *
     * @return A [FlingBehavior] with bouncy physics
     */
    @Composable
    fun bouncy(): FlingBehavior = flingBehavior(
        scrollConfiguration = FlingConfiguration.Builder()
            .decelerationFriction(0.6f)
            .splineInflection(.4f)
            .build()
    )

    /**
     * Floaty, long-distance fling behavior.
     *
     * Low deceleration friction allows for long, gliding scrolls
     * with minimal effort. Great for browsing large amounts of content.
     *
     * **Best for:**
     * - Photo galleries
     * - Social media feeds
     * - Content discovery UIs
     * - Long lists with visual content
     *
     * **Configuration:**
     * - Higher scroll friction (0.09)
     * - Very low deceleration friction (0.015)
     * - Long scroll distance with smooth deceleration
     *
     * @return A [FlingBehavior] with floaty, long-glide physics
     */
    @Composable
    fun floaty(): FlingBehavior = flingBehavior(
        scrollConfiguration = FlingConfiguration.Builder()
            .scrollViewFriction(.09f)
            .decelerationFriction(0.015f)
            .build()
    )

    /**
     * Snappy, responsive fling behavior.
     *
     * Balanced settings that provide quick response to user input
     * while maintaining smooth scrolling.
     *
     * **Best for:**
     * - Interactive UIs with frequent scrolling
     * - E-commerce product lists
     * - Chat applications
     * - Any UI requiring quick response
     *
     * **Configuration:**
     * - Moderate scroll friction (0.03)
     * - Moderate deceleration friction (0.2)
     * - Responsive feel with controlled momentum
     *
     * @return A [FlingBehavior] with snappy physics
     */
    @Composable
    fun snappy(): FlingBehavior = flingBehavior(
        scrollConfiguration = FlingConfiguration.Builder()
            .scrollViewFriction(0.03f)
            .decelerationFriction(0.2f)
            .build()
    )

    /**
     * Ultra-smooth fling behavior for premium feel.
     *
     * Carefully tuned parameters for the smoothest possible
     * scroll experience with extended momentum.
     *
     * **Best for:**
     * - Premium/luxury apps
     * - Reading applications
     * - Showcase/portfolio apps
     * - When smoothness is the top priority
     *
     * **Configuration:**
     * - Low scroll friction (0.006)
     * - Low deceleration friction (0.05)
     * - High spline resolution (150 points)
     *
     * @return A [FlingBehavior] with ultra-smooth physics
     */
    @Composable
    fun ultraSmooth(): FlingBehavior = flingBehavior(
        scrollConfiguration = FlingConfiguration.Builder()
            .scrollViewFriction(0.006f)
            .decelerationFriction(0.05f)
            .numberOfSplinePoints(150)
            .build()
    )
}
