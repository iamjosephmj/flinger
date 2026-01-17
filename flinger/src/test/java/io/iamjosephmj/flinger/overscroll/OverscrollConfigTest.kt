/*
* MIT License
*
* Copyright (c) 2021 Joseph James
*/

package io.iamjosephmj.flinger.overscroll

import androidx.compose.animation.core.Spring
import androidx.compose.ui.unit.dp
import com.google.common.truth.Truth.assertThat
import org.junit.Test

/**
 * Unit tests for [OverscrollConfig].
 */
class OverscrollConfigTest {

    @Test
    fun `Default config has expected values`() {
        val config = OverscrollConfig.Default
        
        assertThat(config.maxOverscrollDistance).isEqualTo(100.dp)
        assertThat(config.springStiffness).isEqualTo(Spring.StiffnessLow)
        assertThat(config.dampingRatio).isEqualTo(Spring.DampingRatioMediumBouncy)
        assertThat(config.resistanceFactor).isEqualTo(0.5f)
    }
    
    @Test
    fun `iOS config has higher stiffness`() {
        val config = OverscrollConfig.IOS
        
        assertThat(config.springStiffness).isGreaterThan(OverscrollConfig.Default.springStiffness)
        assertThat(config.dampingRatio).isEqualTo(Spring.DampingRatioNoBouncy)
    }
    
    @Test
    fun `Bouncy config has lower stiffness and bouncy damping`() {
        val config = OverscrollConfig.Bouncy
        
        assertThat(config.springStiffness).isEqualTo(Spring.StiffnessLow)
        assertThat(config.dampingRatio).isEqualTo(Spring.DampingRatioLowBouncy)
        assertThat(config.maxOverscrollDistance).isGreaterThan(OverscrollConfig.Default.maxOverscrollDistance)
    }
    
    @Test
    fun `Stiff config has high stiffness and short distance`() {
        val config = OverscrollConfig.Stiff
        
        assertThat(config.springStiffness).isEqualTo(Spring.StiffnessHigh)
        assertThat(config.maxOverscrollDistance).isLessThan(OverscrollConfig.Default.maxOverscrollDistance)
    }
    
    @Test
    fun `None config disables overscroll`() {
        val config = OverscrollConfig.None
        
        assertThat(config.maxOverscrollDistance).isEqualTo(0.dp)
        assertThat(config.resistanceFactor).isEqualTo(0f)
    }
    
    @Test
    fun `custom config can be created`() {
        val config = OverscrollConfig(
            maxOverscrollDistance = 200.dp,
            springStiffness = 500f,
            dampingRatio = 0.8f,
            resistanceFactor = 0.7f
        )
        
        assertThat(config.maxOverscrollDistance).isEqualTo(200.dp)
        assertThat(config.springStiffness).isEqualTo(500f)
        assertThat(config.dampingRatio).isEqualTo(0.8f)
        assertThat(config.resistanceFactor).isEqualTo(0.7f)
    }
    
    @Test
    fun `config copy works correctly`() {
        val original = OverscrollConfig.Default
        val modified = original.copy(maxOverscrollDistance = 150.dp)
        
        assertThat(modified.maxOverscrollDistance).isEqualTo(150.dp)
        assertThat(modified.springStiffness).isEqualTo(original.springStiffness)
        assertThat(modified.dampingRatio).isEqualTo(original.dampingRatio)
        assertThat(modified.resistanceFactor).isEqualTo(original.resistanceFactor)
    }
    
    @Test
    fun `all presets have valid resistance factors`() {
        val presets = listOf(
            OverscrollConfig.Default,
            OverscrollConfig.IOS,
            OverscrollConfig.Bouncy,
            OverscrollConfig.Stiff
        )
        
        presets.forEach { config ->
            assertThat(config.resistanceFactor).isAtLeast(0f)
            assertThat(config.resistanceFactor).isAtMost(1f)
        }
    }
}
