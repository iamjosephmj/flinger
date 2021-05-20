package io.iamjosephmj.flinger.bahaviours

import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.runtime.Composable
import io.iamjosephmj.flinger.ScrollViewConfiguration
import io.iamjosephmj.flinger.flings.flingBehavior


object ScrollBehaviourBank {

    @Composable
    fun getAndroidNativeScroll(): FlingBehavior = ScrollableDefaults.flingBehavior()

    @Composable
    fun lowInflectionLowFrictionLowDecelerationScroll(): FlingBehavior = flingBehavior(
        scrollConfiguration = ScrollViewConfiguration.Builder()
            .build()
    )

    @Composable
    fun lowInflectionLowFrictionLowDecelerationHighSampleScroll(): FlingBehavior = flingBehavior(
        scrollConfiguration = ScrollViewConfiguration.Builder()
            .numberOfSplinePoints(1000)
            .build()
    )

}