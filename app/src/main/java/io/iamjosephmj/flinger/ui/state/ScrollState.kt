package io.iamjosephmj.flinger.ui.state

import io.iamjosephmj.flinger.configs.ScrollViewConfiguration

object ScrollState {
    fun buildScrollBehaviour() = ScrollViewConfiguration.Builder()
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