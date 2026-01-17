/*
* MIT License
*
* Copyright (c) 2021 Joseph James
*/

package io.iamjosephmj.flinger.snap

import androidx.compose.animation.core.Spring
import com.google.common.truth.Truth.assertThat
import org.junit.Test

/**
 * Unit tests for SnapAnimationConfig presets and custom configurations.
 */
class SnapAnimationConfigTest {

    @Test
    fun `Smooth preset uses critical damping`() {
        val smooth = SnapAnimationConfig.Smooth
        assertThat(smooth).isInstanceOf(SnapAnimationConfig.SpringBased::class.java)
        
        val springBased = smooth as SnapAnimationConfig.SpringBased
        assertThat(springBased.dampingRatio).isEqualTo(1f) // Critical damping
        assertThat(springBased.stiffness).isEqualTo(200f)
    }

    @Test
    fun `Snappy preset has higher stiffness than Smooth`() {
        val smooth = SnapAnimationConfig.Smooth as SnapAnimationConfig.SpringBased
        val snappy = SnapAnimationConfig.Snappy as SnapAnimationConfig.SpringBased
        
        assertThat(snappy.stiffness).isGreaterThan(smooth.stiffness)
    }

    @Test
    fun `Bouncy preset is underdamped`() {
        val bouncy = SnapAnimationConfig.Bouncy
        assertThat(bouncy).isInstanceOf(SnapAnimationConfig.SpringBased::class.java)
        
        val springBased = bouncy as SnapAnimationConfig.SpringBased
        assertThat(springBased.dampingRatio).isLessThan(1f) // Underdamped = bounce
    }

    @Test
    fun `Gentle preset has lowest stiffness`() {
        val gentle = SnapAnimationConfig.Gentle as SnapAnimationConfig.SpringBased
        val smooth = SnapAnimationConfig.Smooth as SnapAnimationConfig.SpringBased
        val snappy = SnapAnimationConfig.Snappy as SnapAnimationConfig.SpringBased
        
        assertThat(gentle.stiffness).isLessThan(smooth.stiffness)
        assertThat(gentle.stiffness).isLessThan(snappy.stiffness)
    }

    @Test
    fun `Instant preset uses tween animation`() {
        val instant = SnapAnimationConfig.Instant
        assertThat(instant).isInstanceOf(SnapAnimationConfig.TweenBased::class.java)
        
        val tweenBased = instant as SnapAnimationConfig.TweenBased
        assertThat(tweenBased.durationMillis).isEqualTo(100)
    }

    @Test
    fun `iOS preset matches expected values`() {
        val ios = SnapAnimationConfig.IOS
        assertThat(ios).isInstanceOf(SnapAnimationConfig.SpringBased::class.java)
        
        val springBased = ios as SnapAnimationConfig.SpringBased
        assertThat(springBased.stiffness).isEqualTo(300f)
        assertThat(springBased.dampingRatio).isEqualTo(0.85f)
    }

    @Test
    fun `Material preset uses tween animation`() {
        val material = SnapAnimationConfig.Material
        assertThat(material).isInstanceOf(SnapAnimationConfig.TweenBased::class.java)
        
        val tweenBased = material as SnapAnimationConfig.TweenBased
        assertThat(tweenBased.durationMillis).isEqualTo(250)
    }

    @Test
    fun `custom spring creates SpringBased config`() {
        val custom = SnapAnimationConfig.custom(
            stiffness = 500f,
            dampingRatio = 0.75f
        )
        
        assertThat(custom).isInstanceOf(SnapAnimationConfig.SpringBased::class.java)
        val springBased = custom as SnapAnimationConfig.SpringBased
        assertThat(springBased.stiffness).isEqualTo(500f)
        assertThat(springBased.dampingRatio).isEqualTo(0.75f)
    }

    @Test
    fun `customTween creates TweenBased config`() {
        val custom = SnapAnimationConfig.customTween(
            durationMillis = 400
        )
        
        assertThat(custom).isInstanceOf(SnapAnimationConfig.TweenBased::class.java)
        val tweenBased = custom as SnapAnimationConfig.TweenBased
        assertThat(tweenBased.durationMillis).isEqualTo(400)
    }

    @Test
    fun `SpringBased default values match expected`() {
        val springBased = SnapAnimationConfig.SpringBased()
        
        assertThat(springBased.stiffness).isEqualTo(Spring.StiffnessLow)
        assertThat(springBased.dampingRatio).isEqualTo(Spring.DampingRatioNoBouncy)
        assertThat(springBased.visibilityThreshold).isEqualTo(0.1f)
    }

    @Test
    fun `TweenBased default values match expected`() {
        val tweenBased = SnapAnimationConfig.TweenBased()
        
        assertThat(tweenBased.durationMillis).isEqualTo(300)
        assertThat(tweenBased.delayMillis).isEqualTo(0)
    }

    @Test
    fun `all presets create valid animation specs`() {
        val presets = listOf(
            SnapAnimationConfig.Smooth,
            SnapAnimationConfig.Snappy,
            SnapAnimationConfig.Bouncy,
            SnapAnimationConfig.Gentle,
            SnapAnimationConfig.Instant,
            SnapAnimationConfig.IOS,
            SnapAnimationConfig.Material
        )
        
        presets.forEach { preset ->
            val spec = preset.createAnimationSpec<Float>()
            assertThat(spec).isNotNull()
        }
    }

    @Test
    fun `stiffness hierarchy is correct`() {
        // Snappy > Bouncy > iOS > Smooth > Gentle
        val snappy = (SnapAnimationConfig.Snappy as SnapAnimationConfig.SpringBased).stiffness
        val bouncy = (SnapAnimationConfig.Bouncy as SnapAnimationConfig.SpringBased).stiffness
        val ios = (SnapAnimationConfig.IOS as SnapAnimationConfig.SpringBased).stiffness
        val smooth = (SnapAnimationConfig.Smooth as SnapAnimationConfig.SpringBased).stiffness
        val gentle = (SnapAnimationConfig.Gentle as SnapAnimationConfig.SpringBased).stiffness
        
        assertThat(snappy).isGreaterThan(bouncy)
        assertThat(bouncy).isGreaterThan(ios)
        assertThat(ios).isGreaterThan(smooth)
        assertThat(smooth).isGreaterThan(gentle)
    }
}
