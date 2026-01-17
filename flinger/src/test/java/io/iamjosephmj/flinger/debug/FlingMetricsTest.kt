/*
* MIT License
*
* Copyright (c) 2021 Joseph James
*/

package io.iamjosephmj.flinger.debug

import com.google.common.truth.Truth.assertThat
import io.iamjosephmj.flinger.configs.FlingConfiguration
import org.junit.Test

/**
 * Unit tests for [FlingMetrics].
 */
class FlingMetricsTest {

    @Test
    fun `Empty metrics has default values`() {
        val metrics = FlingMetrics.Empty
        
        assertThat(metrics.isFlinging).isFalse()
        assertThat(metrics.initialVelocity).isEqualTo(0f)
        assertThat(metrics.currentVelocity).isEqualTo(0f)
        assertThat(metrics.progress).isEqualTo(0f)
        assertThat(metrics.totalScrolled).isEqualTo(0f)
        assertThat(metrics.flingCount).isEqualTo(0)
        assertThat(metrics.lastFlingDuration).isEqualTo(0L)
        assertThat(metrics.configuration).isNull()
    }
    
    @Test
    fun `velocityText formats correctly`() {
        val metrics = FlingMetrics(currentVelocity = 1234.56f)
        
        assertThat(metrics.velocityText).isEqualTo("1235 px/s")
    }
    
    @Test
    fun `scrolledText formats correctly`() {
        val metrics = FlingMetrics(totalScrolled = 5678.9f)
        
        assertThat(metrics.scrolledText).isEqualTo("5679 px")
    }
    
    @Test
    fun `progressText formats correctly`() {
        val metrics50 = FlingMetrics(progress = 0.5f)
        val metrics100 = FlingMetrics(progress = 1.0f)
        val metrics0 = FlingMetrics(progress = 0f)
        
        assertThat(metrics50.progressText).isEqualTo("50%")
        assertThat(metrics100.progressText).isEqualTo("100%")
        assertThat(metrics0.progressText).isEqualTo("0%")
    }
    
    @Test
    fun `generateConfigCode returns placeholder when no config`() {
        val metrics = FlingMetrics(configuration = null)
        
        assertThat(metrics.generateConfigCode()).contains("No configuration available")
    }
    
    @Test
    fun `generateConfigCode generates valid Kotlin code`() {
        val config = FlingConfiguration.Builder()
            .scrollViewFriction(0.02f)
            .decelerationFriction(0.1f)
            .build()
        
        val metrics = FlingMetrics(configuration = config)
        val code = metrics.generateConfigCode()
        
        assertThat(code).contains("FlingConfiguration.Builder()")
        assertThat(code).contains(".scrollViewFriction(")
        assertThat(code).contains(".decelerationFriction(")
        assertThat(code).contains(".build()")
    }
    
    @Test
    fun `metrics can be constructed with all values`() {
        val config = FlingConfiguration.Builder().build()
        
        val metrics = FlingMetrics(
            isFlinging = true,
            initialVelocity = 2000f,
            currentVelocity = 1500f,
            progress = 0.25f,
            totalScrolled = 500f,
            flingCount = 5,
            lastFlingDuration = 350L,
            averageVelocity = 1800f,
            configuration = config
        )
        
        assertThat(metrics.isFlinging).isTrue()
        assertThat(metrics.initialVelocity).isEqualTo(2000f)
        assertThat(metrics.currentVelocity).isEqualTo(1500f)
        assertThat(metrics.progress).isEqualTo(0.25f)
        assertThat(metrics.totalScrolled).isEqualTo(500f)
        assertThat(metrics.flingCount).isEqualTo(5)
        assertThat(metrics.lastFlingDuration).isEqualTo(350L)
        assertThat(metrics.averageVelocity).isEqualTo(1800f)
        assertThat(metrics.configuration).isEqualTo(config)
    }
    
    @Test
    fun `FlingStatistics has correct default values`() {
        val stats = FlingStatistics()
        
        assertThat(stats.totalFlings).isEqualTo(0)
        assertThat(stats.totalDistance).isEqualTo(0f)
        assertThat(stats.averageVelocity).isEqualTo(0f)
        assertThat(stats.maxVelocity).isEqualTo(0f)
        assertThat(stats.averageDistance).isEqualTo(0f)
        assertThat(stats.averageDuration).isEqualTo(0L)
    }
}
