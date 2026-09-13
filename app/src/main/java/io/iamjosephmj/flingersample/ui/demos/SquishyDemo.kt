/*
* MIT License
*
* Copyright (c) 2021 Joseph James
*/

package io.iamjosephmj.flingersample.ui.demos

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import io.iamjosephmj.flinger.behaviours.FlingPresets
import io.iamjosephmj.flingersample.R
import io.iamjosephmj.flingersample.ui.components.GradientPresets
import io.iamjosephmj.flingersample.ui.theme.AuroraCyan
import io.iamjosephmj.flingersample.ui.theme.AuroraViolet
import io.iamjosephmj.squishy.physics.OverScrollConfig
import io.iamjosephmj.squishy.physics.OverscrollCurve
import io.iamjosephmj.squishy.scroll.OverScrollArea
import io.iamjosephmj.squishy.state.rememberOverScrollState
import io.iamjosephmj.squishy.visual.OverscrollVisual
import io.iamjosephmj.squishy.visual.OverscrollVisuals

/**
 * The overscroll effects this demo cycles through. All of them come from
 * Squishy's [OverscrollVisuals] factory.
 */
enum class SquishyVisualKind {
    PushDown, Zoom, Tilt, Fade, Rotate
}

/**
 * Demo screen showcasing Squishy's custom overscroll effects combined with
 * Flinger's fling physics: Flinger drives the scroll, and the velocity left
 * over at either edge becomes a rubber-band bounce rendered by Squishy.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SquishyDemo(navController: NavController) {
    var selectedVisual by remember { mutableStateOf(SquishyVisualKind.PushDown) }

    val overscrollVisual: OverscrollVisual = when (selectedVisual) {
        SquishyVisualKind.PushDown -> OverscrollVisuals.pushDown()
        SquishyVisualKind.Zoom -> OverscrollVisuals.zoom()
        SquishyVisualKind.Tilt -> OverscrollVisuals.tilt()
        SquishyVisualKind.Fade -> OverscrollVisuals.fade()
        SquishyVisualKind.Rotate -> OverscrollVisuals.rotate()
    }

    val overscrollState = rememberOverScrollState(
        visual = overscrollVisual,
        config = OverScrollConfig(
            maxOverscroll = 500f,
            curve = OverscrollCurve.RubberBand()
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        stringResource(R.string.squishy_demo_title),
                        color = AuroraCyan
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.action_back)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Effect selector
            Text(
                text = stringResource(R.string.squishy_visual),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(16.dp),
                color = MaterialTheme.colorScheme.onSurface
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SquishyVisualKind.entries.forEach { kind ->
                    FilterChip(
                        selected = selectedVisual == kind,
                        onClick = { selectedVisual = kind },
                        label = { Text(kind.name) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = AuroraCyan,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.squishy_swipe_prompt),
                style = MaterialTheme.typography.bodyMedium,
                color = AuroraCyan,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Scrollable list: Flinger handles the fling, Squishy renders
            // the bounce when the edges are overshot.
            OverScrollArea(
                state = overscrollState,
                modifier = Modifier.weight(1f)
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    flingBehavior = FlingPresets.smooth()
                ) {
                    items(100) { index ->
                        SquishyListItem(index)
                    }

                    // Info card at the bottom of the scrollable list
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            shape = RoundedCornerShape(20.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        Brush.linearGradient(
                                            listOf(AuroraCyan, AuroraViolet)
                                        )
                                    )
                                    .padding(20.dp)
                            ) {
                                Column {
                                    Text(
                                        text = stringResource(R.string.squishy_how_it_works),
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = stringResource(R.string.squishy_how_it_works_desc),
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = Color.White.copy(alpha = 0.9f)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SquishyListItem(index: Int) {
    val gradient = GradientPresets.forIndex(index)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.8f)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = stringResource(R.string.squishy_item, index + 1),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = gradient.first()
            )
            Text(
                text = stringResource(R.string.squishy_item_desc),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
