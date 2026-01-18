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
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.navigation.NavController
import io.iamjosephmj.flinger.configs.FlingConfiguration
import io.iamjosephmj.flinger.snap.SnapAnimationConfig
import io.iamjosephmj.flinger.snap.SnapPosition
import io.iamjosephmj.flinger.snap.snapFlingBehavior
import io.iamjosephmj.flingersample.ui.components.GradientPresets
import io.iamjosephmj.flingersample.ui.components.TranslucentBackground
import io.iamjosephmj.flingersample.ui.theme.AuroraCyan
import io.iamjosephmj.flingersample.ui.theme.AuroraMagenta
import io.iamjosephmj.flingersample.ui.theme.AuroraViolet

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
                title = { 
                    Text(
                        "Pager-Style Demo",
                        color = AuroraViolet
                    ) 
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
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
                .verticalScroll(rememberScrollState())
        ) {
            // Preset selector
            Text(
                text = "Fling Physics",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(16.dp),
                color = MaterialTheme.colorScheme.onSurface
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
                        label = { Text(preset) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = AuroraViolet,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Snap animation selector
            Text(
                text = "Snap Animation",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(horizontal = 16.dp),
                color = MaterialTheme.colorScheme.onSurface
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
                        label = { Text(anim) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = AuroraCyan,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Smooth Fusion toggle
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f).padding(end = 8.dp)) {
                        Text(
                            text = "Smooth Fusion",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = if (smoothFusionEnabled) AuroraMagenta else MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Snap starts when velocity decays",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Switch(
                        checked = smoothFusionEnabled,
                        onCheckedChange = { smoothFusionEnabled = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = AuroraMagenta,
                            checkedTrackColor = AuroraMagenta.copy(alpha = 0.5f)
                        )
                    )
                }
                
                // Fusion ratio slider (only visible when fusion is enabled)
                if (smoothFusionEnabled) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .padding(bottom = 16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Fusion Point",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "${(fusionRatio * 100).toInt()}% velocity",
                                style = MaterialTheme.typography.bodyMedium,
                                color = AuroraMagenta,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        Slider(
                            value = fusionRatio,
                            onValueChange = { fusionRatio = it },
                            valueRange = 0.05f..0.4f,
                            steps = 6,
                            colors = SliderDefaults.colors(
                                thumbColor = AuroraMagenta,
                                activeTrackColor = AuroraMagenta,
                                inactiveTrackColor = AuroraMagenta.copy(alpha = 0.2f)
                            )
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
            
            // Page indicators with glow
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(10) { index ->
                    val isSelected = currentPage == index
                    val dotColor = if (isSelected) {
                        GradientPresets.forIndex(index).first()
                    } else {
                        MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                    }
                    
                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .size(if (isSelected) 12.dp else 8.dp)
                            .clip(CircleShape)
                            .background(dotColor)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Info card
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
                                listOf(AuroraViolet, AuroraMagenta)
                            )
                        )
                        .padding(20.dp)
                ) {
                    Column {
                        Text(
                            text = "How it works",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Pager-like behavior using snap fling physics:\n\n" +
                                    "• Standard: Balanced transitions\n" +
                                    "• iOS: Higher resistance, familiar feel\n" +
                                    "• Snappy: Quick page turns\n" +
                                    "• Smooth: Gliding transitions\n\n" +
                                    "Smooth Fusion: Snap starts while still scrolling.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }
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
    val gradient = GradientPresets.forIndex(page)
    
    Card(
        modifier = modifier.height(260.dp),
        shape = RoundedCornerShape(24.dp),
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
