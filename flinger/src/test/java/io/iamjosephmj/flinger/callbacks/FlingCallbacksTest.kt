/*
* MIT License
*
* Copyright (c) 2021 Joseph James
*/

package io.iamjosephmj.flinger.callbacks

import com.google.common.truth.Truth.assertThat
import org.junit.Test

/**
 * Unit tests for [FlingCallbacks].
 */
class FlingCallbacksTest {

    @Test
    fun `Empty callback has all null handlers`() {
        val callbacks = FlingCallbacks.Empty
        
        assertThat(callbacks.onFlingStart).isNull()
        assertThat(callbacks.onFlingProgress).isNull()
        assertThat(callbacks.onFlingEnd).isNull()
    }
    
    @Test
    fun `callback with onFlingStart only`() {
        var startCalled = false
        var receivedVelocity = 0f
        
        val callbacks = FlingCallbacks(
            onFlingStart = { velocity ->
                startCalled = true
                receivedVelocity = velocity
            }
        )
        
        // Simulate calling the callback
        callbacks.onFlingStart?.invoke(1500f)
        
        assertThat(startCalled).isTrue()
        assertThat(receivedVelocity).isEqualTo(1500f)
        assertThat(callbacks.onFlingProgress).isNull()
        assertThat(callbacks.onFlingEnd).isNull()
    }
    
    @Test
    fun `callback with onFlingProgress only`() {
        var progressCalled = false
        var receivedProgress = 0f
        var receivedVelocity = 0f
        
        val callbacks = FlingCallbacks(
            onFlingProgress = { progress, velocity ->
                progressCalled = true
                receivedProgress = progress
                receivedVelocity = velocity
            }
        )
        
        // Simulate calling the callback
        callbacks.onFlingProgress?.invoke(0.5f, 750f)
        
        assertThat(progressCalled).isTrue()
        assertThat(receivedProgress).isEqualTo(0.5f)
        assertThat(receivedVelocity).isEqualTo(750f)
    }
    
    @Test
    fun `callback with onFlingEnd only`() {
        var endCalled = false
        var receivedScrolled = 0f
        var receivedCancelled = false
        
        val callbacks = FlingCallbacks(
            onFlingEnd = { totalScrolled, cancelled ->
                endCalled = true
                receivedScrolled = totalScrolled
                receivedCancelled = cancelled
            }
        )
        
        // Simulate calling the callback
        callbacks.onFlingEnd?.invoke(2500f, false)
        
        assertThat(endCalled).isTrue()
        assertThat(receivedScrolled).isEqualTo(2500f)
        assertThat(receivedCancelled).isFalse()
    }
    
    @Test
    fun `callback with all handlers`() {
        var startVelocity = 0f
        var progressValue = 0f
        var endScrolled = 0f
        
        val callbacks = FlingCallbacks(
            onFlingStart = { startVelocity = it },
            onFlingProgress = { progress, _ -> progressValue = progress },
            onFlingEnd = { scrolled, _ -> endScrolled = scrolled }
        )
        
        callbacks.onFlingStart?.invoke(2000f)
        callbacks.onFlingProgress?.invoke(0.75f, 500f)
        callbacks.onFlingEnd?.invoke(3000f, true)
        
        assertThat(startVelocity).isEqualTo(2000f)
        assertThat(progressValue).isEqualTo(0.75f)
        assertThat(endScrolled).isEqualTo(3000f)
    }
    
    @Test
    fun `data class equality works correctly`() {
        val callback1 = FlingCallbacks()
        val callback2 = FlingCallbacks()
        val callback3 = FlingCallbacks(onFlingStart = {})
        
        assertThat(callback1).isEqualTo(callback2)
        assertThat(callback1).isNotEqualTo(callback3)
    }
}
