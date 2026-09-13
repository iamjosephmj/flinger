/*
* MIT License
*
* Copyright (c) 2021 Joseph James
*/

package io.iamjosephmj.flingersample.ui.components

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.iamjosephmj.squishy.physics.OverScrollConfig
import io.iamjosephmj.squishy.physics.OverscrollCurve
import io.iamjosephmj.squishy.scroll.OverScrollArea
import io.iamjosephmj.squishy.state.OverScrollState
import io.iamjosephmj.squishy.state.rememberOverScrollState

/**
 * Remembers a rubber-band overscroll state for one scroll axis.
 * A state is vertical or horizontal — not both — so use one per scrollable.
 */
@Composable
fun rememberSquishyOverscrollState(
    orientation: Orientation = Orientation.Vertical,
    maxOverscroll: Float = 500f,
): OverScrollState = rememberOverScrollState(
    config = OverScrollConfig(
        orientation = orientation,
        maxOverscroll = maxOverscroll,
        curve = OverscrollCurve.RubberBand()
    )
)

/**
 * Wraps a scrollable in the classic container overscroll: the whole content
 * shifts with the edge pull (push-down) and springs back on release. The
 * state's fling absorption drives the bounce for both drags and flings.
 */
@Composable
fun SquishyOverscrollArea(
    state: OverScrollState,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    OverScrollArea(state = state, modifier = modifier, content = content)
}
