/*
* MIT License
*
* Copyright (c) 2021 Joseph James
*/

package io.iamjosephmj.flinger.adaptive

import com.google.common.truth.Truth.assertThat
import org.junit.Test

/**
 * Unit tests for [AdaptiveMode].
 */
class AdaptiveModeTest {

    @Test
    fun `AdaptiveMode has three modes`() {
        val modes = AdaptiveMode.entries
        
        assertThat(modes).hasSize(3)
        assertThat(modes).containsExactly(
            AdaptiveMode.Balanced,
            AdaptiveMode.Precision,
            AdaptiveMode.Momentum
        )
    }
    
    @Test
    fun `Balanced mode has expected configuration`() {
        val mode = AdaptiveMode.Balanced
        
        // Gentle config should have higher friction
        assertThat(mode.gentleConfig.decelerationFriction).isGreaterThan(0.1f)
        
        // Aggressive config should have lower friction
        assertThat(mode.aggressiveConfig.decelerationFriction).isLessThan(0.1f)
        
        // Threshold should be reasonable
        assertThat(mode.threshold).isEqualTo(1500f)
    }
    
    @Test
    fun `Precision mode prioritizes control`() {
        val mode = AdaptiveMode.Precision
        
        // Both configs should have relatively high friction
        assertThat(mode.gentleConfig.decelerationFriction).isGreaterThan(0.3f)
        assertThat(mode.aggressiveConfig.decelerationFriction).isGreaterThan(0.1f)
        
        // Higher threshold means more velocity needed for aggressive
        assertThat(mode.threshold).isEqualTo(2000f)
    }
    
    @Test
    fun `Momentum mode prioritizes long scrolls`() {
        val mode = AdaptiveMode.Momentum
        
        // Both configs should have relatively low friction
        assertThat(mode.gentleConfig.decelerationFriction).isLessThan(0.2f)
        assertThat(mode.aggressiveConfig.decelerationFriction).isLessThan(0.05f)
        
        // Lower threshold means easier to get momentum
        assertThat(mode.threshold).isEqualTo(1000f)
    }
    
    @Test
    fun `gentle config has higher friction than aggressive for all modes`() {
        AdaptiveMode.entries.forEach { mode ->
            assertThat(mode.gentleConfig.decelerationFriction)
                .isGreaterThan(mode.aggressiveConfig.decelerationFriction)
        }
    }
    
    @Test
    fun `all modes have valid threshold values`() {
        AdaptiveMode.entries.forEach { mode ->
            assertThat(mode.threshold).isGreaterThan(0f)
            assertThat(mode.threshold).isLessThan(10000f)
        }
    }
}
