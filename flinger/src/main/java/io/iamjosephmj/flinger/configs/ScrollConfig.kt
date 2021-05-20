package io.iamjosephmj.flinger

import kotlin.math.ln

class ScrollViewConfiguration private constructor(
    val scrollFriction: Float,
    val absVelocityThreshold: Float,
    val gravitationalForce: Float,
    val inchesPerMeter: Float,
    val decelerationFriction: Float,
    val decelerationRate: Float,
    val splineInflection: Float,
    val splineStartTension: Float,
    private val splineEndTension: Float,
    val numberOfSplinePoints: Int
) {

    val splineP1: Float by lazy { splineStartTension * splineInflection }

    val splineP2: Float by lazy { 1.0f - splineEndTension * (1.0f - splineInflection) }

    data class Builder(
        /*
         * This variable manages the friction to the scrolls in the LazyColumn
         */
        var scrollViewFriction: Float = 0.008f,

        /*
         * This is the absolute value of a velocity threshold, below which the
         * animation is considered finished.
         */
        var absVelocityThreshold: Float = 0f,

        /*
         * Gravitational obstruction to the scroll.
         */
        var gravitationalForce: Float = 9.80665f,

        /*
         * Scroll Inches per meter
         */
        var inchesPerMeter: Float = 39.37f,

        /*
         * Rate of deceleration of the scrollView.
         */
        var decelerationRate: Float = (ln(0.78) / ln(0.9)).toFloat(),

        /*
         * Friction at the time of deceleration.
         */
        var decelerationFriction: Float = 0.09f,

        /*
         * Inflection is the place where the start and end tension lines cross each other.
         */
        var splineInflection: Float = 0.1f,

        /*
         * Spline's start tension.
         */
        var splineStartTension: Float = 0.1f,

        /*
         * Spline's end tension.
         */
        var splineEndTension: Float = 1.0f,

        /*
         * number of sampling points in the spline
         */
        var numberOfSplinePoints: Int = 100
    ) {

        fun scrollViewFriction(scrollViewFriction: Float) =
            apply { this.scrollViewFriction = scrollViewFriction }

        fun absVelocityThreshold(absVelocityThreshold: Float) =
            apply { this.absVelocityThreshold = absVelocityThreshold }

        fun gravitationalForce(gravitationalForce: Float) =
            apply { this.gravitationalForce = gravitationalForce }

        fun inchesPerMeter(inchesPerMeter: Float) =
            apply { this.inchesPerMeter = inchesPerMeter }

        fun decelerationFriction(decelerationFriction: Float) =
            apply { this.decelerationFriction = decelerationFriction }

        fun decelerationRate(decelerationRate: Float) =
            apply { this.decelerationRate = decelerationRate }

        fun splineInflection(splineInflection: Float) =
            apply { this.splineInflection = splineInflection }

        fun splineStartTension(splineStartTension: Float) =
            apply { this.splineStartTension = splineStartTension }

        fun splineEndTension(splineEndTension: Float) =
            apply { this.splineEndTension = splineEndTension }

        fun numberOfSplinePoints(numberOfSplinePoints: Int) =
            apply { this.numberOfSplinePoints = numberOfSplinePoints }

        fun build() = ScrollViewConfiguration(
            scrollViewFriction,
            absVelocityThreshold,
            gravitationalForce,
            inchesPerMeter,
            decelerationFriction,
            decelerationRate,
            splineInflection,
            splineStartTension,
            splineEndTension,
            numberOfSplinePoints
        )
    }
}