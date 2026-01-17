package io.iamjosephmj.flinger.configs

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import kotlin.math.ln

/**
 * Comprehensive tests for [FlingConfiguration] and its Builder.
 */
class FlingConfigurationTest {

    // ==================== Builder Default Values ====================

    @Test
    fun `builder creates configuration with default scroll friction`() {
        val config = FlingConfiguration.Builder().build()
        
        assertThat(config.scrollFriction).isEqualTo(0.008f)
    }

    @Test
    fun `builder creates configuration with default velocity threshold`() {
        val config = FlingConfiguration.Builder().build()
        
        assertThat(config.absVelocityThreshold).isEqualTo(0f)
    }

    @Test
    fun `builder creates configuration with default gravitational force`() {
        val config = FlingConfiguration.Builder().build()
        
        assertThat(config.gravitationalForce).isEqualTo(9.80665f)
    }

    @Test
    fun `builder creates configuration with default inches per meter`() {
        val config = FlingConfiguration.Builder().build()
        
        assertThat(config.inchesPerMeter).isEqualTo(39.37f)
    }

    @Test
    fun `builder creates configuration with default deceleration rate`() {
        val config = FlingConfiguration.Builder().build()
        val expectedRate = (ln(0.78) / ln(0.9)).toFloat()
        
        assertThat(config.decelerationRate).isEqualTo(expectedRate)
    }

    @Test
    fun `builder creates configuration with default deceleration friction`() {
        val config = FlingConfiguration.Builder().build()
        
        assertThat(config.decelerationFriction).isEqualTo(0.09f)
    }

    @Test
    fun `builder creates configuration with default spline inflection`() {
        val config = FlingConfiguration.Builder().build()
        
        assertThat(config.splineInflection).isEqualTo(0.1f)
    }

    @Test
    fun `builder creates configuration with default spline start tension`() {
        val config = FlingConfiguration.Builder().build()
        
        assertThat(config.splineStartTension).isEqualTo(0.1f)
    }

    @Test
    fun `builder creates configuration with default number of spline points`() {
        val config = FlingConfiguration.Builder().build()
        
        assertThat(config.numberOfSplinePoints).isEqualTo(100)
    }

    // ==================== Builder Custom Values ====================

    @Test
    fun `builder applies custom scroll friction`() {
        val customFriction = 0.05f
        val config = FlingConfiguration.Builder()
            .scrollViewFriction(customFriction)
            .build()
        
        assertThat(config.scrollFriction).isEqualTo(customFriction)
    }

    @Test
    fun `builder applies custom velocity threshold`() {
        val customThreshold = 50f
        val config = FlingConfiguration.Builder()
            .absVelocityThreshold(customThreshold)
            .build()
        
        assertThat(config.absVelocityThreshold).isEqualTo(customThreshold)
    }

    @Test
    fun `builder applies custom gravitational force`() {
        val customGravity = 15f
        val config = FlingConfiguration.Builder()
            .gravitationalForce(customGravity)
            .build()
        
        assertThat(config.gravitationalForce).isEqualTo(customGravity)
    }

    @Test
    fun `builder applies custom inches per meter`() {
        val customInches = 50f
        val config = FlingConfiguration.Builder()
            .inchesPerMeter(customInches)
            .build()
        
        assertThat(config.inchesPerMeter).isEqualTo(customInches)
    }

    @Test
    fun `builder applies custom deceleration rate`() {
        val customRate = 3.0f
        val config = FlingConfiguration.Builder()
            .decelerationRate(customRate)
            .build()
        
        assertThat(config.decelerationRate).isEqualTo(customRate)
    }

    @Test
    fun `builder applies custom deceleration friction`() {
        val customFriction = 0.5f
        val config = FlingConfiguration.Builder()
            .decelerationFriction(customFriction)
            .build()
        
        assertThat(config.decelerationFriction).isEqualTo(customFriction)
    }

    @Test
    fun `builder applies custom spline inflection`() {
        val customInflection = 0.25f
        val config = FlingConfiguration.Builder()
            .splineInflection(customInflection)
            .build()
        
        assertThat(config.splineInflection).isEqualTo(customInflection)
    }

    @Test
    fun `builder applies custom spline start tension`() {
        val customTension = 0.3f
        val config = FlingConfiguration.Builder()
            .splineStartTension(customTension)
            .build()
        
        assertThat(config.splineStartTension).isEqualTo(customTension)
    }

    @Test
    fun `builder applies custom spline end tension`() {
        val customTension = 0.8f
        val config = FlingConfiguration.Builder()
            .splineEndTension(customTension)
            .build()
        
        // splineEndTension is private, verify through splineP2 calculation
        val expectedP2 = 1.0f - customTension * (1.0f - config.splineInflection)
        assertThat(config.splineP2).isEqualTo(expectedP2)
    }

    @Test
    fun `builder applies custom number of spline points`() {
        val customPoints = 200
        val config = FlingConfiguration.Builder()
            .numberOfSplinePoints(customPoints)
            .build()
        
        assertThat(config.numberOfSplinePoints).isEqualTo(customPoints)
    }

    // ==================== Builder Chaining ====================

    @Test
    fun `builder supports method chaining`() {
        val config = FlingConfiguration.Builder()
            .scrollViewFriction(0.01f)
            .absVelocityThreshold(10f)
            .gravitationalForce(10f)
            .inchesPerMeter(40f)
            .decelerationRate(2.5f)
            .decelerationFriction(0.1f)
            .splineInflection(0.15f)
            .splineStartTension(0.2f)
            .splineEndTension(0.9f)
            .numberOfSplinePoints(150)
            .build()
        
        assertThat(config.scrollFriction).isEqualTo(0.01f)
        assertThat(config.absVelocityThreshold).isEqualTo(10f)
        assertThat(config.gravitationalForce).isEqualTo(10f)
        assertThat(config.inchesPerMeter).isEqualTo(40f)
        assertThat(config.decelerationRate).isEqualTo(2.5f)
        assertThat(config.decelerationFriction).isEqualTo(0.1f)
        assertThat(config.splineInflection).isEqualTo(0.15f)
        assertThat(config.splineStartTension).isEqualTo(0.2f)
        assertThat(config.numberOfSplinePoints).isEqualTo(150)
    }

    @Test
    fun `builder allows overwriting previously set values`() {
        val config = FlingConfiguration.Builder()
            .scrollViewFriction(0.01f)
            .scrollViewFriction(0.02f)
            .scrollViewFriction(0.03f)
            .build()
        
        assertThat(config.scrollFriction).isEqualTo(0.03f)
    }

    // ==================== Spline P1 and P2 Calculations ====================

    @Test
    fun `splineP1 is calculated correctly from start tension and inflection`() {
        val config = FlingConfiguration.Builder()
            .splineStartTension(0.2f)
            .splineInflection(0.3f)
            .build()
        
        val expectedP1 = 0.2f * 0.3f // startTension * inflection
        assertThat(config.splineP1).isEqualTo(expectedP1)
    }

    @Test
    fun `splineP2 is calculated correctly from end tension and inflection`() {
        val config = FlingConfiguration.Builder()
            .splineEndTension(0.8f)
            .splineInflection(0.2f)
            .build()
        
        // P2 = 1.0 - endTension * (1.0 - inflection)
        val expectedP2 = 1.0f - 0.8f * (1.0f - 0.2f)
        assertThat(config.splineP2).isEqualTo(expectedP2)
    }

    @Test
    fun `splineP1 with default values`() {
        val config = FlingConfiguration.Builder().build()
        
        // Default: startTension = 0.1, inflection = 0.1
        val expectedP1 = 0.1f * 0.1f
        assertThat(config.splineP1).isEqualTo(expectedP1)
    }

    @Test
    fun `splineP2 with default values`() {
        val config = FlingConfiguration.Builder().build()
        
        // Default: endTension = 1.0, inflection = 0.1
        // P2 = 1.0 - 1.0 * (1.0 - 0.1) = 1.0 - 0.9 = 0.1
        val expectedP2 = 1.0f - 1.0f * (1.0f - 0.1f)
        assertThat(config.splineP2).isEqualTo(expectedP2)
    }

    // ==================== Edge Cases ====================

    @Test
    fun `builder handles zero scroll friction`() {
        val config = FlingConfiguration.Builder()
            .scrollViewFriction(0f)
            .build()
        
        assertThat(config.scrollFriction).isEqualTo(0f)
    }

    @Test
    fun `builder handles very small friction values`() {
        val config = FlingConfiguration.Builder()
            .scrollViewFriction(0.0001f)
            .build()
        
        assertThat(config.scrollFriction).isEqualTo(0.0001f)
    }

    @Test
    fun `builder handles large friction values`() {
        val config = FlingConfiguration.Builder()
            .scrollViewFriction(1.0f)
            .build()
        
        assertThat(config.scrollFriction).isEqualTo(1.0f)
    }

    @Test
    fun `builder handles minimum spline points`() {
        val config = FlingConfiguration.Builder()
            .numberOfSplinePoints(1)
            .build()
        
        assertThat(config.numberOfSplinePoints).isEqualTo(1)
    }

    @Test
    fun `builder handles large number of spline points`() {
        val config = FlingConfiguration.Builder()
            .numberOfSplinePoints(1000)
            .build()
        
        assertThat(config.numberOfSplinePoints).isEqualTo(1000)
    }

    // ==================== Multiple Configurations ====================

    @Test
    fun `multiple configurations are independent`() {
        val config1 = FlingConfiguration.Builder()
            .scrollViewFriction(0.01f)
            .build()
        
        val config2 = FlingConfiguration.Builder()
            .scrollViewFriction(0.05f)
            .build()
        
        assertThat(config1.scrollFriction).isEqualTo(0.01f)
        assertThat(config2.scrollFriction).isEqualTo(0.05f)
        assertThat(config1.scrollFriction).isNotEqualTo(config2.scrollFriction)
    }

    @Test
    fun `builder can be reused for multiple configurations`() {
        val builder = FlingConfiguration.Builder()
            .scrollViewFriction(0.01f)
        
        val config1 = builder.build()
        
        builder.scrollViewFriction(0.02f)
        val config2 = builder.build()
        
        // Note: This tests that builder is mutable
        assertThat(config1.scrollFriction).isEqualTo(0.01f)
        assertThat(config2.scrollFriction).isEqualTo(0.02f)
    }
}
