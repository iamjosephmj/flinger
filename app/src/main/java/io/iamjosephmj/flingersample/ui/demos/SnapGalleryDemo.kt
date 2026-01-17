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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import io.iamjosephmj.flinger.snap.SnapAnimationConfig
import io.iamjosephmj.flinger.snap.SnapPosition
import io.iamjosephmj.flinger.snap.snapFlingBehavior

/**
 * Demo screen showcasing the snap-to-item fling behavior with configurable animations.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SnapGalleryDemo(navController: NavController) {
    var selectedSnapPosition by remember { mutableStateOf(SnapPosition.Center) }
    var selectedAnimation by remember { mutableStateOf("Smooth") }
    var smoothFusionEnabled by remember { mutableStateOf(false) }
    var fusionRatio by remember { mutableStateOf(0.15f) }
    val listState = rememberLazyListState()
    
    // Get the selected animation config
    val snapAnimation = when (selectedAnimation) {
        "Smooth" -> SnapAnimationConfig.Smooth
        "Snappy" -> SnapAnimationConfig.Snappy
        "Bouncy" -> SnapAnimationConfig.Bouncy
        "Gentle" -> SnapAnimationConfig.Gentle
        "iOS" -> SnapAnimationConfig.IOS
        "Material" -> SnapAnimationConfig.Material
        "Instant" -> SnapAnimationConfig.Instant
        else -> SnapAnimationConfig.Smooth
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Snap Gallery Demo") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Snap position selector
            Text(
                text = "Snap Position",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
            
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SnapPosition.entries.forEach { position ->
                    FilterChip(
                        selected = selectedSnapPosition == position,
                        onClick = { selectedSnapPosition = position },
                        label = { Text(position.name) }
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Animation style selector
            Text(
                text = "Snap Animation",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
            
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("Smooth", "Snappy", "Bouncy", "Gentle", "iOS", "Material", "Instant").forEach { anim ->
                    FilterChip(
                        selected = selectedAnimation == anim,
                        onClick = { selectedAnimation = anim },
                        label = { Text(anim) }
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Animation description
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Text(
                    text = getAnimationDescription(selectedAnimation),
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(12.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Smooth Fusion toggle
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f).padding(end = 8.dp)) {
                    Text(
                        text = "Smooth Fusion",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "Snap starts when velocity decays",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Switch(
                    checked = smoothFusionEnabled,
                    onCheckedChange = { smoothFusionEnabled = it }
                )
            }
            
            // Fusion ratio slider (only visible when fusion is enabled)
            if (smoothFusionEnabled) {
                Spacer(modifier = Modifier.height(8.dp))
                
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Fusion Point",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "${(fusionRatio * 100).toInt()}% velocity",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Slider(
                        value = fusionRatio,
                        onValueChange = { fusionRatio = it },
                        valueRange = 0.05f..0.4f,
                        steps = 6
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Earlier (smoother)",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "Later (snappier)",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Gallery with snap behavior
            Text(
                text = if (smoothFusionEnabled) "Try a slow swipe to see the fusion effect" else "Swipe to see snap behavior",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            LazyRow(
                state = listState,
                flingBehavior = snapFlingBehavior(
                    lazyListState = listState,
                    snapPosition = selectedSnapPosition,
                    snapAnimation = snapAnimation,
                    smoothFusion = smoothFusionEnabled,
                    fusionVelocityRatio = fusionRatio
                ),
                contentPadding = PaddingValues(horizontal = 48.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(20) { index ->
                    GalleryCard(
                        index = index,
                        modifier = Modifier.width(280.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Info card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "How it works",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "The snap animation controls how items settle into position after scrolling. " +
                                "Each preset uses different physics:\n\n" +
                                "• Spring-based: Stiffness + damping ratio\n" +
                                "• Tween-based: Duration + easing curve\n\n" +
                                "Dynamic Fusion: Instead of waiting for fling to stop, snap starts when " +
                                "velocity decays below a threshold. The fusion point slider controls when " +
                                "this transition happens - earlier = smoother, later = snappier.",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

private fun getAnimationDescription(animation: String): String = when (animation) {
    "Smooth" -> "Smooth: Gentle, fluid snap with no bounce. Best for most carousels and galleries."
    "Snappy" -> "Snappy: Quick, responsive snap. Great for fast-paced UIs and interactive lists."
    "Bouncy" -> "Bouncy: Playful snap with slight overshoot. Perfect for games and fun UIs."
    "Gentle" -> "Gentle: Very slow, relaxed snap. Ideal for luxury/premium apps."
    "iOS" -> "iOS: Apple-style animation. Familiar feel for cross-platform users."
    "Material" -> "Material: Google Material Design timing. Modern and consistent."
    "Instant" -> "Instant: Near-instant snap. Best for utility apps or accessibility."
    else -> ""
}

@Composable
private fun GalleryCard(
    index: Int,
    modifier: Modifier = Modifier
) {
    val gradients = listOf(
        listOf(Color(0xFF667eea), Color(0xFF764ba2)),
        listOf(Color(0xFF11998e), Color(0xFF38ef7d)),
        listOf(Color(0xFFee0979), Color(0xFFff6a00)),
        listOf(Color(0xFF654ea3), Color(0xFFeaafc8)),
        listOf(Color(0xFF00c6ff), Color(0xFF0072ff))
    )
    
    val gradient = gradients[index % gradients.size]
    
    Card(
        modifier = modifier.height(200.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(gradient)
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Card ${index + 1}",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Swipe to navigate",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }
        }
    }
}
