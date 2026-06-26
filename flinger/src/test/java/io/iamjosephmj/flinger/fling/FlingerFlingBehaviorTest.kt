package io.iamjosephmj.flinger.fling

import androidx.compose.animation.core.generateDecayAnimationSpec
import androidx.compose.foundation.gestures.ScrollScope
import androidx.compose.runtime.BroadcastFrameClock
import io.iamjosephmj.flinger.flings.FlingerFlingBehavior
import io.iamjosephmj.flinger.flings.SplineBasedFloatDecayAnimationSpec
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.yield
import org.junit.Assert.assertThrows
import org.junit.Test

class FlingerFlingBehaviorTest {

    private fun createBehavior(): FlingerFlingBehavior {
        val spec = SplineBasedFloatDecayAnimationSpec(
            density = FlingTestUtils.getDummyDensityComponent(),
            scrollConfiguration = FlingTestUtils.getDefaultScrollConfiguration()
        ).generateDecayAnimationSpec<Float>()
        return FlingerFlingBehavior(spec)
    }

    @Test
    fun `performFling propagates CancellationException from scrollBy`() {
        val behavior = createBehavior()
        // Simulates the scroll being grabbed mid-fling: scrollBy is cancelled.
        val scope = object : ScrollScope {
            override fun scrollBy(pixels: Float): Float =
                throw CancellationException("grabbed mid-fling")
        }

        val clock = BroadcastFrameClock()
        assertThrows(CancellationException::class.java) {
            runBlocking(clock) {
                withTimeout(5_000) {
                    val fling = async { with(behavior) { scope.performFling(8000f) } }
                    // Pump frames until the fling coroutine reaches scrollBy and finishes.
                    while (fling.isActive) {
                        clock.sendFrame(0L)
                        yield()
                    }
                    fling.await()
                }
            }
        }
    }
}
