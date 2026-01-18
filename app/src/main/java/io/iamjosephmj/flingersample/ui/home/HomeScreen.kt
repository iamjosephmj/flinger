/*
* MIT License
*
* Copyright (c) 2021 Joseph James
*/

package io.iamjosephmj.flingersample.ui.home

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.Compare
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.SwipeRight
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.ViewCarousel
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import io.iamjosephmj.flinger.behaviours.FlingPresets
import io.iamjosephmj.flingersample.ui.components.TranslucentBackground
import io.iamjosephmj.flingersample.ui.components.GlowingIcon
import io.iamjosephmj.flingersample.ui.components.GradientPresets
import io.iamjosephmj.flingersample.ui.theme.FlingerTheme
import kotlinx.coroutines.delay

/**
 * Home screen - the main dashboard of the Flinger sample app.
 * Features an Aurora animated background and staggered card animations.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    
    // Use theme colors for accents
    val primaryColor = MaterialTheme.colorScheme.primary
    val secondaryColor = MaterialTheme.colorScheme.secondary
    val tertiaryColor = MaterialTheme.colorScheme.tertiary
    
    // Navigation items
    val coreFeatures = listOf(
        NavItem(
            title = "Fling Playground",
            description = "Interactively adjust all fling parameters and see the results in real-time",
            icon = Icons.Default.Tune,
            route = "playground",
            accentColor = primaryColor
        ),
        NavItem(
            title = "Preset Gallery",
            description = "Browse and try all built-in presets with live previews",
            icon = Icons.Default.GridView,
            route = "presets",
            accentColor = tertiaryColor
        ),
        NavItem(
            title = "Side-by-Side Comparison",
            description = "Compare native Android scroll with Flinger behaviors",
            icon = Icons.Default.Compare,
            route = "comparison",
            accentColor = secondaryColor
        )
    )
    
    val demoFeatures = listOf(
        NavItem(
            title = "Snap Gallery",
            description = "Snap-to-item behavior for carousels and galleries",
            icon = Icons.Default.ViewCarousel,
            route = "snapDemo",
            accentColor = primaryColor
        ),
        NavItem(
            title = "Adaptive Fling",
            description = "Velocity-aware physics that adapts to your swipes",
            icon = Icons.Default.Speed,
            route = "adaptiveDemo",
            accentColor = tertiaryColor
        ),
        NavItem(
            title = "Pager Physics",
            description = "Custom page-turn physics for HorizontalPager",
            icon = Icons.Default.SwipeRight,
            route = "pagerDemo",
            accentColor = secondaryColor
        ),
        NavItem(
            title = "Debug Overlay",
            description = "Real-time fling visualization for developers",
            icon = Icons.Default.BugReport,
            route = "debugDemo",
            accentColor = primaryColor
        )
    )
    
    TranslucentBackground {
        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            containerColor = Color.Transparent,
            topBar = {
                LargeTopAppBar(
                    title = {
                        Column {
                            Text(
                                text = "✦ FLINGER",
                                style = MaterialTheme.typography.headlineLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = "Customizable Fling Physics for Compose",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    },
                    scrollBehavior = scrollBehavior,
                    colors = TopAppBarDefaults.largeTopAppBarColors(
                        containerColor = Color.Transparent,
                        scrolledContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.92f)
                    )
                )
            }
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                flingBehavior = FlingPresets.smooth()
            ) {
                item {
                    SectionHeader(
                        title = "Core Features",
                        index = 0
                    )
                }
                
                itemsIndexed(coreFeatures) { index, item ->
                    AnimatedNavigationCard(
                        item = item,
                        index = index + 1,
                        onClick = { navController.navigate(item.route) }
                    )
                }
                
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    SectionHeader(
                        title = "Feature Demos",
                        index = coreFeatures.size + 1
                    )
                }
                
                itemsIndexed(demoFeatures) { index, item ->
                    AnimatedNavigationCard(
                        item = item,
                        index = index + coreFeatures.size + 2,
                        onClick = { navController.navigate(item.route) }
                    )
                }
                
                item {
                    Spacer(modifier = Modifier.height(24.dp))
                    VersionInfo(index = coreFeatures.size + demoFeatures.size + 3)
                }
            }
        }
    }
}

private data class NavItem(
    val title: String,
    val description: String,
    val icon: ImageVector,
    val route: String,
    val accentColor: androidx.compose.ui.graphics.Color
)

@Composable
private fun SectionHeader(
    title: String,
    index: Int
) {
    var visible by remember { mutableStateOf(false) }
    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(400),
        label = "section_alpha"
    )
    val offsetY by animateFloatAsState(
        targetValue = if (visible) 0f else 20f,
        animationSpec = tween(400),
        label = "section_offset"
    )
    
    LaunchedEffect(Unit) {
        delay(index * 50L)
        visible = true
    }
    
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier
            .padding(vertical = 8.dp)
            .alpha(alpha)
            .graphicsLayer { translationY = offsetY }
    )
}

@Composable
private fun AnimatedNavigationCard(
    item: NavItem,
    index: Int,
    onClick: () -> Unit
) {
    var visible by remember { mutableStateOf(false) }
    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(400),
        label = "card_alpha"
    )
    val offsetY by animateFloatAsState(
        targetValue = if (visible) 0f else 30f,
        animationSpec = tween(400),
        label = "card_offset"
    )
    val scale by animateFloatAsState(
        targetValue = if (visible) 1f else 0.95f,
        animationSpec = tween(400),
        label = "card_scale"
    )
    
    LaunchedEffect(Unit) {
        delay(index * 50L)
        visible = true
    }
    
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .alpha(alpha)
            .graphicsLayer {
                translationY = offsetY
                scaleX = scale
                scaleY = scale
            },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            GlowingIcon(
                imageVector = item.icon,
                contentDescription = null,
                tint = item.accentColor,
                glowColor = item.accentColor.copy(alpha = 0.5f),
                iconSize = 40.dp,
                glowRadius = 8.dp
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = item.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun VersionInfo(index: Int) {
    var visible by remember { mutableStateOf(false) }
    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(400),
        label = "version_alpha"
    )
    
    LaunchedEffect(Unit) {
        delay(index * 50L)
        visible = true
    }
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(alpha),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Flinger v2.0.0",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
        )
        Text(
            text = "Made with ❤️ by Joseph James",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
            textAlign = TextAlign.Center
        )
    }
}
