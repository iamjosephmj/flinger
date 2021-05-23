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

package io.iamjosephmj.flinger.ui.state

import io.iamjosephmj.flinger.configs.FlingConfiguration

/**
 * This is how we make a fling behaviour with all values from the settings page.
 *
 * @author Joseph James.
 */
object ScrollState {
    fun buildScrollBehaviour() = FlingConfiguration.Builder()
        .scrollViewFriction(scrollFriction)
        .absVelocityThreshold(absVelocityThreshold)
        .gravitationalForce(gravitationalForce)
        .inchesPerMeter(inchesPerMeter)
        .decelerationFriction(decelerationFriction)
        .decelerationRate(decelerationRate)
        .splineInflection(splineInflection)
        .splineStartTension(splineStartTension)
        .splineEndTension(splineEndTension)
        .numberOfSplinePoints(numberOfSplinePoints)
        .build()

    var type = 1
    var scrollFriction: Float = 0.008f
    var absVelocityThreshold: Float = 0f
    var gravitationalForce: Float = 9.80665f
    var inchesPerMeter: Float = 39.37f
    var decelerationFriction: Float = .09f
    var decelerationRate: Float = 2.358201f
    var splineInflection: Float = 0.1f
    var splineStartTension: Float = 0.1f
    var splineEndTension: Float = 1.0f
    var numberOfSplinePoints: Int = 100
}