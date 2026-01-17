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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.iamjosephmj.flinger.configs.FlingConfiguration
import io.iamjosephmj.flinger.snap.SnapAnimationConfig
import io.iamjosephmj.flinger.snap.SnapPosition
import io.iamjosephmj.flinger.snap.snapFlingBehavior
import androidx.navigation.NavController

/**
 * Demo screen showcasing pager-like behavior with custom fling physics.
 * Uses LazyRow with snap behavior to demonstrate page-like navigation.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PagerDemo(navController: NavController) {
    var selectedPreset by remember { mutableStateOf("Standard") }
    var selectedSnapAnimation by remember { mutableStateOf("Smooth") }
    var smoothFusionEnabled by remember { mutableStateOf(false) }
    var fusionRatio by remember { mutableStateOf(0.15f) }
    val listState = rememberLazyListState()
    
    // Track current page based on scroll position
    val currentPage by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex
        }
    }
    
    // Different fling configurations for page-like behavior
    val flingConfig = when (selectedPreset) {
        "Standard" -> FlingConfiguration.Builder()
            .scrollViewFriction(0.015f)
            .decelerationFriction(0.12f)
            .build()
        "iOS" -> FlingConfiguration.Builder()
            .scrollViewFriction(0.025f)
            .decelerationFriction(0.2f)
            .build()
        "Snappy" -> FlingConfiguration.Builder()
            .scrollViewFriction(0.02f)
            .decelerationFriction(0.25f)
            .build()
        "Smooth" -> FlingConfiguration.Builder()
            .scrollViewFriction(0.008f)
            .decelerationFriction(0.08f)
            .build()
        else -> FlingConfiguration.Default
    }
    
    // Get the selected snap animation config
    val snapAnimation = when (selectedSnapAnimation) {
        "Smooth" -> SnapAnimationConfig.Smooth
        "Snappy" -> SnapAnimationConfig.Snappy
        "Bouncy" -> SnapAnimationConfig.Bouncy
        "Gentle" -> SnapAnimationConfig.Gentle
        "iOS" -> SnapAnimationConfig.IOS
        "Material" -> SnapAnimationConfig.Material
        else -> SnapAnimationConfig.Smooth
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pager-Style Demo") },
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
                .verticalScroll(rememberScrollState())
        ) {
            // Preset selector
            Text(
                text = "Fling Physics",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(16.dp)
            )
            
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("Standard", "iOS", "Snappy", "Smooth").forEach { preset ->
                    FilterChip(
                        selected = selectedPreset == preset,
                        onClick = { selectedPreset = preset },
                        label = { Text(preset) }
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Snap animation selector
            Text(
                text = "Snap Animation",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("Smooth", "Snappy", "Bouncy", "Gentle", "iOS", "Material").forEach { anim ->
                    FilterChip(
                        selected = selectedSnapAnimation == anim,
                        onClick = { selectedSnapAnimation = anim },
                        label = { Text(anim) }
                    )
                }
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
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Dynamic Smooth Fusion",
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
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Pager-like carousel with custom snap behavior
            LazyRow(
                state = listState,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp),
                flingBehavior = snapFlingBehavior(
                    lazyListState = listState,
                    snapPosition = SnapPosition.Center,
                    flingConfig = flingConfig,
                    snapAnimation = snapAnimation,
                    smoothFusion = smoothFusionEnabled,
                    fusionVelocityRatio = fusionRatio
                ),
                contentPadding = PaddingValues(horizontal = 48.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(10) { page ->
                    PagerCard(
                        page = page,
                        modifier = Modifier.width(280.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Page indicators
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(10) { index ->
                    val isSelected = currentPage == index
                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .size(if (isSelected) 10.dp else 8.dp)
                            .clip(CircleShape)
                            .background(
                                if (isSelected) 
                                    MaterialTheme.colorScheme.primary
                                else 
                                    MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                            )
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
                        text = "This demonstrates pager-like behavior using snap fling physics. " +
                                "Different presets provide varying page-turn feels:\n\n" +
                                "• Standard: Balanced page transitions\n" +
                                "• iOS: Higher resistance, iOS-like feel\n" +
                                "• Snappy: Quick page turns\n" +
                                "• Smooth: Longer, gliding transitions\n\n" +
                                "Dynamic Fusion: When enabled, the snap starts while still scrolling, " +
                                "creating seamless page turns.",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun PagerCard(
    page: Int,
    modifier: Modifier = Modifier
) {
    val gradients = listOf(
        listOf(Color(0xFF667eea), Color(0xFF764ba2)),
        listOf(Color(0xFF11998e), Color(0xFF38ef7d)),
        listOf(Color(0xFFee0979), Color(0xFFff6a00)),
        listOf(Color(0xFF654ea3), Color(0xFFeaafc8)),
        listOf(Color(0xFF00c6ff), Color(0xFF0072ff)),
        listOf(Color(0xFFf857a6), Color(0xFFff5858)),
        listOf(Color(0xFF4776E6), Color(0xFF8E54E9)),
        listOf(Color(0xFFFC466B), Color(0xFF3F5EFB)),
        listOf(Color(0xFF11998e), Color(0xFF38ef7d)),
        listOf(Color(0xFFFF512F), Color(0xFFDD2476))
    )
    
    Card(
        modifier = modifier.height(260.dp),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(gradients[page])
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Page ${page + 1}",
                    style = MaterialTheme.typography.displaySmall,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Swipe to see page physics",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White.copy(alpha = 0.8f),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
