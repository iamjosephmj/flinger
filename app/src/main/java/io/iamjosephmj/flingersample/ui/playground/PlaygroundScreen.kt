/*
* MIT License
*
* Copyright (c) 2021 Joseph James
*/

package io.iamjosephmj.flingersample.ui.playground

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import io.iamjosephmj.flinger.configs.FlingConfiguration
import io.iamjosephmj.flinger.flings.flingBehavior
import io.iamjosephmj.flingersample.ui.components.FlingCurveCanvas
import io.iamjosephmj.flingersample.ui.components.GradientPresets
import io.iamjosephmj.flingersample.ui.components.TranslucentBackground
import io.iamjosephmj.flingersample.ui.theme.AuroraCyan
import io.iamjosephmj.flingersample.ui.theme.AuroraMagenta
import io.iamjosephmj.flingersample.ui.theme.AuroraViolet
import kotlin.math.ln

/**
 * Interactive playground for tuning fling parameters in real-time.
 * 
 * Clean layout with:
 * - Collapsible curve visualization
 * - Compact parameter sliders
 * - Bottom horizontal scroll strip for testing
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaygroundScreen(navController: NavController) {
    // State for all parameters
    var scrollFriction by remember { mutableFloatStateOf(0.008f) }
    var decelerationFriction by remember { mutableFloatStateOf(0.09f) }
    var gravitationalForce by remember { mutableFloatStateOf(9.80665f) }
    var inchesPerMeter by remember { mutableFloatStateOf(39.37f) }
    var decelerationRate by remember { mutableFloatStateOf((ln(0.78) / ln(0.9)).toFloat()) }
    var splineInflection by remember { mutableFloatStateOf(0.1f) }
    var splineStartTension by remember { mutableFloatStateOf(0.1f) }
    var splineEndTension by remember { mutableFloatStateOf(1.0f) }
    var numberOfSplinePoints by remember { mutableIntStateOf(100) }
    var absVelocityThreshold by remember { mutableFloatStateOf(0f) }
    
    // UI state
    var selectedCategory by remember { mutableStateOf("Friction") }
    var showCurve by remember { mutableStateOf(true) }
    
    // Build configuration
    val currentConfig = FlingConfiguration.Builder()
        .scrollViewFriction(scrollFriction)
        .decelerationFriction(decelerationFriction)
        .gravitationalForce(gravitationalForce)
        .inchesPerMeter(inchesPerMeter)
        .decelerationRate(decelerationRate)
        .splineInflection(splineInflection)
        .splineStartTension(splineStartTension)
        .splineEndTension(splineEndTension)
        .numberOfSplinePoints(numberOfSplinePoints)
        .absVelocityThreshold(absVelocityThreshold)
        .build()
    
    fun resetToDefaults() {
        scrollFriction = 0.008f
        decelerationFriction = 0.09f
        gravitationalForce = 9.80665f
        inchesPerMeter = 39.37f
        decelerationRate = (ln(0.78) / ln(0.9)).toFloat()
        splineInflection = 0.1f
        splineStartTension = 0.1f
        splineEndTension = 1.0f
        numberOfSplinePoints = 100
        absVelocityThreshold = 0f
    }
    
    TranslucentBackground {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = { 
                        Text(
                            "Fling Playground",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.primary
                        ) 
                    },
                    navigationIcon = {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack, 
                                contentDescription = "Back",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = { resetToDefaults() }) {
                            Icon(
                                Icons.Default.Refresh, 
                                contentDescription = "Reset",
                                tint = MaterialTheme.colorScheme.secondary
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent
                    )
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // =====================================================
                // COLLAPSIBLE CURVE VISUALIZATION
                // =====================================================
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                        .clickable { showCurve = !showCurve },
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Fling Curve",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                LegendDot(color = AuroraCyan)
                                Text("Pos", style = MaterialTheme.typography.labelSmall, 
                                     color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Spacer(modifier = Modifier.width(8.dp))
                                LegendDot(color = AuroraMagenta)
                                Text("Vel", style = MaterialTheme.typography.labelSmall,
                                     color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Spacer(modifier = Modifier.width(8.dp))
                                Icon(
                                    if (showCurve) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                    contentDescription = "Toggle",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                        AnimatedVisibility(
                            visible = showCurve,
                            enter = expandVertically(),
                            exit = shrinkVertically()
                        ) {
                            FlingCurveCanvas(
                                config = currentConfig,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp),
                                animated = false
                            )
                        }
                    }
                }
                
                // =====================================================
                // CATEGORY CHIPS
                // =====================================================
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CompactChip("Friction", Icons.Default.Speed, selectedCategory == "Friction") { 
                        selectedCategory = "Friction" 
                    }
                    CompactChip("Physics", Icons.Default.Science, selectedCategory == "Physics") { 
                        selectedCategory = "Physics" 
                    }
                    CompactChip("Spline", Icons.Default.Timeline, selectedCategory == "Spline") { 
                        selectedCategory = "Spline" 
                    }
                    CompactChip("All", Icons.Default.Tune, selectedCategory == "All") { 
                        selectedCategory = "All" 
                    }
                }
                
                // =====================================================
                // PARAMETERS (Single scrollable list)
                // =====================================================
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // FRICTION
                    if (selectedCategory == "All" || selectedCategory == "Friction") {
                        item { SectionLabel("Friction", AuroraCyan) }
                        item {
                            CompactSlider(
                                label = "Scroll Friction",
                                value = scrollFriction,
                                onValueChange = { scrollFriction = it },
                                valueRange = 0.001f..0.1f,
                                accentColor = AuroraCyan
                            )
                        }
                        item {
                            CompactSlider(
                                label = "Deceleration Friction",
                                value = decelerationFriction,
                                onValueChange = { decelerationFriction = it },
                                valueRange = 0.01f..1.0f,
                                accentColor = AuroraCyan
                            )
                        }
                    }
                    
                    // PHYSICS
                    if (selectedCategory == "All" || selectedCategory == "Physics") {
                        item { SectionLabel("Physics", AuroraViolet) }
                        item {
                            CompactSlider(
                                label = "Gravity",
                                value = gravitationalForce,
                                onValueChange = { gravitationalForce = it },
                                valueRange = 1f..20f,
                                accentColor = AuroraViolet
                            )
                        }
                        item {
                            CompactSlider(
                                label = "Inches/Meter",
                                value = inchesPerMeter,
                                onValueChange = { inchesPerMeter = it },
                                valueRange = 10f..100f,
                                accentColor = AuroraViolet
                            )
                        }
                        item {
                            CompactSlider(
                                label = "Decel Rate",
                                value = decelerationRate,
                                onValueChange = { decelerationRate = it },
                                valueRange = 0.5f..10f,
                                accentColor = AuroraViolet
                            )
                        }
                        item {
                            CompactSlider(
                                label = "Velocity Threshold",
                                value = absVelocityThreshold,
                                onValueChange = { absVelocityThreshold = it },
                                valueRange = 0f..100f,
                                accentColor = AuroraViolet
                            )
                        }
                    }
                    
                    // SPLINE
                    if (selectedCategory == "All" || selectedCategory == "Spline") {
                        item { SectionLabel("Spline", AuroraMagenta) }
                        item {
                            CompactSlider(
                                label = "Inflection",
                                value = splineInflection,
                                onValueChange = { splineInflection = it },
                                valueRange = 0.01f..0.5f,
                                accentColor = AuroraMagenta
                            )
                        }
                        item {
                            CompactSlider(
                                label = "Start Tension",
                                value = splineStartTension,
                                onValueChange = { splineStartTension = it },
                                valueRange = 0.01f..1.0f,
                                accentColor = AuroraMagenta
                            )
                        }
                        item {
                            CompactSlider(
                                label = "End Tension",
                                value = splineEndTension,
                                onValueChange = { splineEndTension = it },
                                valueRange = 0.1f..2.0f,
                                accentColor = AuroraMagenta
                            )
                        }
                        item {
                            CompactSlider(
                                label = "Spline Points",
                                value = numberOfSplinePoints.toFloat(),
                                onValueChange = { numberOfSplinePoints = it.toInt() },
                                valueRange = 10f..500f,
                                accentColor = AuroraMagenta
                            )
                        }
                    }
                    
                    item { Spacer(modifier = Modifier.height(8.dp)) }
                }
                
                // =====================================================
                // BOTTOM TEST STRIP (Horizontal scroll with custom fling)
                // =====================================================
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                )
                
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Text(
                        text = "← Swipe to test fling →",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                    )
                    
                    key(
                        scrollFriction, decelerationFriction, gravitationalForce, inchesPerMeter,
                        decelerationRate, splineInflection, splineStartTension, splineEndTension,
                        numberOfSplinePoints, absVelocityThreshold
                    ) {
                        LazyRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp),
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            flingBehavior = flingBehavior(scrollConfiguration = currentConfig)
                        ) {
                            items(40) { index ->
                                TestCard(index = index)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LegendDot(color: Color) {
    Box(
        modifier = Modifier
            .size(8.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(color)
            .padding(end = 4.dp)
    )
    Spacer(modifier = Modifier.width(4.dp))
}

@Composable
private fun CompactChip(
    label: String,
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit
) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = { Text(label, style = MaterialTheme.typography.labelMedium) },
        leadingIcon = {
            Icon(icon, contentDescription = null, modifier = Modifier.size(16.dp))
        },
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = MaterialTheme.colorScheme.primary,
            selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
            selectedLeadingIconColor = MaterialTheme.colorScheme.onPrimary
        ),
        modifier = Modifier.height(32.dp)
    )
}

@Composable
private fun SectionLabel(title: String, color: Color) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelLarge,
        fontWeight = FontWeight.Bold,
        color = color,
        modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
    )
}

@Composable
private fun CompactSlider(
    label: String,
    value: Float,
    onValueChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float>,
    accentColor: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Label + Value
            Column(modifier = Modifier.width(100.dp)) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = if (value >= 10f) String.format("%.2f", value) else String.format("%.4f", value),
                    style = MaterialTheme.typography.labelSmall,
                    color = accentColor,
                    fontWeight = FontWeight.Bold
                )
            }
            
            // Slider
            Slider(
                value = value,
                onValueChange = onValueChange,
                valueRange = valueRange,
                modifier = Modifier.weight(1f),
                colors = SliderDefaults.colors(
                    thumbColor = accentColor,
                    activeTrackColor = accentColor,
                    inactiveTrackColor = accentColor.copy(alpha = 0.2f)
                )
            )
        }
    }
}

@Composable
private fun TestCard(index: Int) {
    val gradient = GradientPresets.forIndex(index)
    
    Card(
        modifier = Modifier
            .width(120.dp)
            .fillMaxHeight(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.linearGradient(gradient)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "${index + 1}",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White.copy(alpha = 0.9f)
            )
        }
    }
}
