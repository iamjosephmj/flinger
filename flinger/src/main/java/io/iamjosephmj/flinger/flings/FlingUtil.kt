package io.iamjosephmj.flinger.flings

import androidx.compose.animation.core.DecayAnimationSpec
import androidx.compose.animation.core.generateDecayAnimationSpec
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalDensity
import io.iamjosephmj.flinger.ScrollViewConfiguration


@Composable
fun flingBehavior(
    scrollConfiguration: ScrollViewConfiguration =
        ScrollViewConfiguration.Builder()
            .build()
): FlingBehavior {
    val flingSpec = rememberSplineBasedDecay<Float>(scrollConfiguration)
    return remember(flingSpec) {
        DefaultFlingBehavior(flingSpec)
    }
}

@Composable
fun <T> rememberSplineBasedDecay(scrollConfiguration: ScrollViewConfiguration): DecayAnimationSpec<T> {
    // This function will internally update the calculation of fling decay when the density changes,
    // but the reference to the returned spec will not change across calls.
    val density = LocalDensity.current
    return remember(density.density) {
        SplineBasedFloatDecayAnimationSpec(
            density,
            scrollConfiguration
        ).generateDecayAnimationSpec()
    }
}