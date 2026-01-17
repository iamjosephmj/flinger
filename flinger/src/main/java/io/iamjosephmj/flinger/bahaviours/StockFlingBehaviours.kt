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

package io.iamjosephmj.flinger.bahaviours

import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.runtime.Composable
import io.iamjosephmj.flinger.behaviours.FlingPresets

/**
 * Legacy helper class for selecting predefined scroll behaviours.
 *
 * **Deprecated:** This class is deprecated due to the typo in the package name.
 * Please migrate to [io.iamjosephmj.flinger.behaviours.FlingPresets] instead.
 *
 * ## Migration Guide
 * ```kotlin
 * // Old (deprecated)
 * import io.iamjosephmj.flinger.bahaviours.StockFlingBehaviours
 * StockFlingBehaviours.smoothScroll()
 * StockFlingBehaviours.presetOne()
 *
 * // New (recommended)
 * import io.iamjosephmj.flinger.behaviours.FlingPresets
 * FlingPresets.smooth()
 * FlingPresets.iOSStyle()
 * ```
 *
 * @author Joseph James
 * @see FlingPresets
 */
@Deprecated(
    message = "Package name has a typo. Use io.iamjosephmj.flinger.behaviours.FlingPresets instead.",
    replaceWith = ReplaceWith(
        "FlingPresets",
        "io.iamjosephmj.flinger.behaviours.FlingPresets"
    )
)
object StockFlingBehaviours {

    /**
     * Returns the native Android fling behavior.
     *
     * @deprecated Use [FlingPresets.androidNative] instead.
     */
    @Deprecated(
        message = "Use FlingPresets.androidNative() instead",
        replaceWith = ReplaceWith(
            "FlingPresets.androidNative()",
            "io.iamjosephmj.flinger.behaviours.FlingPresets"
        )
    )
    @Composable
    fun getAndroidNativeScroll(): FlingBehavior = FlingPresets.androidNative()

    /**
     * A smooth, balanced fling behavior.
     *
     * @deprecated Use [FlingPresets.smooth] instead.
     */
    @Deprecated(
        message = "Use FlingPresets.smooth() instead",
        replaceWith = ReplaceWith(
            "FlingPresets.smooth()",
            "io.iamjosephmj.flinger.behaviours.FlingPresets"
        )
    )
    @Composable
    fun smoothScroll(): FlingBehavior = FlingPresets.smooth()

    /**
     * iOS-style fling behavior with higher friction.
     *
     * @deprecated Use [FlingPresets.iOSStyle] instead.
     */
    @Deprecated(
        message = "Use FlingPresets.iOSStyle() instead",
        replaceWith = ReplaceWith(
            "FlingPresets.iOSStyle()",
            "io.iamjosephmj.flinger.behaviours.FlingPresets"
        )
    )
    @Composable
    fun presetOne(): FlingBehavior = FlingPresets.iOSStyle()

    /**
     * Modified spline behavior for unique scroll feel.
     *
     * @deprecated Use [FlingPresets.smoothCurve] instead.
     */
    @Deprecated(
        message = "Use FlingPresets.smoothCurve() instead",
        replaceWith = ReplaceWith(
            "FlingPresets.smoothCurve()",
            "io.iamjosephmj.flinger.behaviours.FlingPresets"
        )
    )
    @Composable
    fun presetTwo(): FlingBehavior = FlingPresets.smoothCurve()

    /**
     * Quick-stop fling behavior for precision scrolling.
     *
     * @deprecated Use [FlingPresets.quickStop] instead.
     */
    @Deprecated(
        message = "Use FlingPresets.quickStop() instead",
        replaceWith = ReplaceWith(
            "FlingPresets.quickStop()",
            "io.iamjosephmj.flinger.behaviours.FlingPresets"
        )
    )
    @Composable
    fun presetThree(): FlingBehavior = FlingPresets.quickStop()

    /**
     * Bouncy, playful fling behavior.
     *
     * @deprecated Use [FlingPresets.bouncy] instead.
     */
    @Deprecated(
        message = "Use FlingPresets.bouncy() instead",
        replaceWith = ReplaceWith(
            "FlingPresets.bouncy()",
            "io.iamjosephmj.flinger.behaviours.FlingPresets"
        )
    )
    @Composable
    fun presetFour(): FlingBehavior = FlingPresets.bouncy()

    /**
     * Floaty, long-distance fling behavior.
     *
     * @deprecated Use [FlingPresets.floaty] instead.
     */
    @Deprecated(
        message = "Use FlingPresets.floaty() instead",
        replaceWith = ReplaceWith(
            "FlingPresets.floaty()",
            "io.iamjosephmj.flinger.behaviours.FlingPresets"
        )
    )
    @Composable
    fun presetFive(): FlingBehavior = FlingPresets.floaty()
}
