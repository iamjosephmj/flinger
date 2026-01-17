/*
* MIT License
*
* Copyright (c) 2021 Joseph James
*/

package io.iamjosephmj.flinger.snap

import com.google.common.truth.Truth.assertThat
import org.junit.Test

/**
 * Unit tests for SnapConfig and SnapPosition.
 */
class SnapConfigTest {

    @Test
    fun `default SnapConfig uses Start position`() {
        val config = SnapConfig()
        assertThat(config.snapPosition).isEqualTo(SnapPosition.Start)
    }

    @Test
    fun `default SnapConfig uses Smooth animation`() {
        val config = SnapConfig()
        assertThat(config.snapAnimation).isEqualTo(SnapAnimationConfig.Smooth)
    }

    @Test
    fun `default smoothFusion is false`() {
        val config = SnapConfig()
        assertThat(config.smoothFusion).isFalse()
    }

    @Test
    fun `smoothFusion can be enabled`() {
        val config = SnapConfig(smoothFusion = true)
        assertThat(config.smoothFusion).isTrue()
    }

    @Test
    fun `default fusionVelocityRatio is 0_15`() {
        val config = SnapConfig()
        assertThat(config.fusionVelocityRatio).isEqualTo(0.15f)
    }

    @Test
    fun `fusionVelocityRatio can be customized`() {
        val config = SnapConfig(fusionVelocityRatio = 0.25f)
        assertThat(config.fusionVelocityRatio).isEqualTo(0.25f)
    }

    @Test
    fun `default velocity threshold is 400f`() {
        val config = SnapConfig()
        assertThat(config.velocityThreshold).isEqualTo(400f)
    }

    @Test
    fun `default maxFlingItems is Int MAX_VALUE`() {
        val config = SnapConfig()
        assertThat(config.maxFlingItems).isEqualTo(Int.MAX_VALUE)
    }

    @Test
    fun `custom SnapConfig preserves values`() {
        val config = SnapConfig(
            snapPosition = SnapPosition.Center,
            velocityThreshold = 600f,
            maxFlingItems = 5,
            snapAnimation = SnapAnimationConfig.Bouncy
        )
        
        assertThat(config.snapPosition).isEqualTo(SnapPosition.Center)
        assertThat(config.velocityThreshold).isEqualTo(600f)
        assertThat(config.maxFlingItems).isEqualTo(5)
        assertThat(config.snapAnimation).isEqualTo(SnapAnimationConfig.Bouncy)
    }

    @Test
    fun `SnapPosition has three values`() {
        val positions = SnapPosition.entries
        assertThat(positions).hasSize(3)
        assertThat(positions).containsExactly(
            SnapPosition.Start,
            SnapPosition.Center,
            SnapPosition.End
        )
    }

    @Test
    fun `SnapConfig copy works correctly`() {
        val original = SnapConfig()
        val modified = original.copy(
            snapPosition = SnapPosition.End,
            snapAnimation = SnapAnimationConfig.Snappy
        )
        
        assertThat(modified.snapPosition).isEqualTo(SnapPosition.End)
        assertThat(modified.snapAnimation).isEqualTo(SnapAnimationConfig.Snappy)
        // Unchanged values should be preserved
        assertThat(modified.velocityThreshold).isEqualTo(original.velocityThreshold)
        assertThat(modified.maxFlingItems).isEqualTo(original.maxFlingItems)
    }

    @Test
    fun `SnapConfig equals works correctly`() {
        val config1 = SnapConfig(
            snapPosition = SnapPosition.Center,
            velocityThreshold = 500f
        )
        val config2 = SnapConfig(
            snapPosition = SnapPosition.Center,
            velocityThreshold = 500f
        )
        val config3 = SnapConfig(
            snapPosition = SnapPosition.Start,
            velocityThreshold = 500f
        )
        
        assertThat(config1).isEqualTo(config2)
        assertThat(config1).isNotEqualTo(config3)
    }
}
