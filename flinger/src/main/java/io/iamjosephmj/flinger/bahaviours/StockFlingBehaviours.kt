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
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.runtime.Composable
import io.iamjosephmj.flinger.configs.FlingConfiguration
import io.iamjosephmj.flinger.flings.flingBehavior

/**
 * This is a helper class for selecting the predefined set of scroll behaviours.
 *
 * @author Joseph James
 */
object StockFlingBehaviours {

    @Composable
    fun getAndroidNativeScroll(): FlingBehavior = ScrollableDefaults.flingBehavior()


    @Composable
    fun smoothScroll(): FlingBehavior = flingBehavior(
        scrollConfiguration = FlingConfiguration.Builder()
            .build()
    )

    @Composable
    fun presetOne(): FlingBehavior = flingBehavior(
        scrollConfiguration = FlingConfiguration.Builder()
            .scrollViewFriction(0.04f)
            .build()
    )

    @Composable
    fun presetTwo(): FlingBehavior = flingBehavior(
        scrollConfiguration = FlingConfiguration.Builder()
            .splineInflection(0.16f)
            .build()
    )

    @Composable
    fun presetThree(): FlingBehavior = flingBehavior(
        scrollConfiguration = FlingConfiguration.Builder()
            .decelerationFriction(0.5f)
            .build()
    )

    @Composable
    fun presetFour(): FlingBehavior = flingBehavior(
        scrollConfiguration = FlingConfiguration.Builder()
            .decelerationFriction(0.6f)
            .splineInflection(.4f)
            .build()
    )

    @Composable
    fun presetFive(): FlingBehavior = flingBehavior(
        scrollConfiguration = FlingConfiguration.Builder()
            .scrollViewFriction(.09f)
            .decelerationFriction(0.015f)
            .build()
    )

}