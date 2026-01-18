/*
* MIT License
*
* Copyright (c) 2021 Joseph James
*/

package io.iamjosephmj.flingersample.ui.playground

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.text.style.TextOverflow
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
 * Interactive playground for tuning ALL fling parameters in real-time.
 * 
 * Parameters are organized into categories:
 * - Friction: scrollFriction, decelerationFriction
 * - Physics: gravitationalForce, inchesPerMeter, decelerationRate
 * - Spline: splineInflection, splineStartTension, splineEndTension, numberOfSplinePoints
 * - Threshold: absVelocityThreshold
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaygroundScreen(navController: NavController) {
    // =========================================================================
    // ALL FlingConfiguration Parameters
    // =========================================================================
    
    // Friction Parameters
    var scrollFriction by remember { mutableFloatStateOf(0.008f) }
    var decelerationFriction by remember { mutableFloatStateOf(0.09f) }
    
    // Physics Parameters
    var gravitationalForce by remember { mutableFloatStateOf(9.80665f) }
    var inchesPerMeter by remember { mutableFloatStateOf(39.37f) }
    var decelerationRate by remember { mutableFloatStateOf((ln(0.78) / ln(0.9)).toFloat()) }
    
    // Spline Parameters
    var splineInflection by remember { mutableFloatStateOf(0.1f) }
    var splineStartTension by remember { mutableFloatStateOf(0.1f) }
    var splineEndTension by remember { mutableFloatStateOf(1.0f) }
    var numberOfSplinePoints by remember { mutableIntStateOf(100) }
    
    // Threshold Parameters
    var absVelocityThreshold by remember { mutableFloatStateOf(0f) }
    
    // Category selection
    var selectedCategory by remember { mutableStateOf("All") }
    
    // Build configuration from current state - NO remember to ensure it updates every recomposition
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
                                contentDescription = "Reset to defaults",
                                tint = MaterialTheme.colorScheme.secondary
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
                    )
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // Fling Curve Visualization
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.9f)
                    ),
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Fling Curve",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            // Legend
                            Row {
                                LegendItem(color = AuroraCyan, label = "Position")
                                Spacer(modifier = Modifier.width(12.dp))
                                LegendItem(color = AuroraMagenta, label = "Velocity", dashed = true)
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        FlingCurveCanvas(
                            config = currentConfig,
                            modifier = Modifier.fillMaxWidth(),
                            animated = false
                        )
                    }
                }
                
                // Category Filter Chips
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CategoryChip("All", Icons.Default.Tune, selectedCategory) { selectedCategory = it }
                    CategoryChip("Friction", Icons.Default.Speed, selectedCategory) { selectedCategory = it }
                    CategoryChip("Physics", Icons.Default.Science, selectedCategory) { selectedCategory = it }
                    CategoryChip("Spline", Icons.Default.Timeline, selectedCategory) { selectedCategory = it }
                }
                
                // Parameters + Preview List
                // Use key() to force LazyColumn recreation when fling config changes
                // This is necessary because the library's flingBehavior() uses remember internally
                key(
                    scrollFriction, decelerationFriction, gravitationalForce, inchesPerMeter,
                    decelerationRate, splineInflection, splineStartTension, splineEndTension,
                    numberOfSplinePoints, absVelocityThreshold
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        flingBehavior = flingBehavior(scrollConfiguration = currentConfig),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                    // =========================================================
                    // FRICTION PARAMETERS
                    // =========================================================
                    if (selectedCategory == "All" || selectedCategory == "Friction") {
                        item {
                            CategoryHeader(
                                title = "Friction Parameters",
                                icon = Icons.Default.Speed,
                                color = AuroraCyan
                            )
                        }
                        
                        item {
                            ParameterSlider(
                                label = "Scroll View Friction",
                                value = scrollFriction,
                                onValueChange = { scrollFriction = it },
                                valueRange = 0.001f..0.1f,
                                description = "Friction applied to scroll. Lower = longer scroll distance.",
                                accentColor = AuroraCyan
                            )
                        }
                        
                        item {
                            ParameterSlider(
                                label = "Deceleration Friction",
                                value = decelerationFriction,
                                onValueChange = { decelerationFriction = it },
                                valueRange = 0.01f..1.0f,
                                description = "Friction during deceleration. Higher = quicker stop.",
                                accentColor = AuroraCyan
                            )
                        }
                    }
                    
                    // =========================================================
                    // PHYSICS PARAMETERS
                    // =========================================================
                    if (selectedCategory == "All" || selectedCategory == "Physics") {
                        item {
                            CategoryHeader(
                                title = "Physics Parameters",
                                icon = Icons.Default.Science,
                                color = AuroraViolet
                            )
                        }
                        
                        item {
                            ParameterSlider(
                                label = "Gravitational Force",
                                value = gravitationalForce,
                                onValueChange = { gravitationalForce = it },
                                valueRange = 1f..20f,
                                description = "Gravitational obstruction (default: 9.80665 m/s²).",
                                accentColor = AuroraViolet
                            )
                        }
                        
                        item {
                            ParameterSlider(
                                label = "Inches Per Meter",
                                value = inchesPerMeter,
                                onValueChange = { inchesPerMeter = it },
                                valueRange = 10f..100f,
                                description = "Screen density factor (default: 39.37 in/m).",
                                accentColor = AuroraViolet
                            )
                        }
                        
                        item {
                            ParameterSlider(
                                label = "Deceleration Rate",
                                value = decelerationRate,
                                onValueChange = { decelerationRate = it },
                                valueRange = 0.5f..10f,
                                description = "Rate of velocity decay. Higher = steeper curve.",
                                accentColor = AuroraViolet
                            )
                        }
                        
                        item {
                            ParameterSlider(
                                label = "Velocity Threshold",
                                value = absVelocityThreshold,
                                onValueChange = { absVelocityThreshold = it },
                                valueRange = 0f..100f,
                                description = "Min velocity to continue animation. 0 = use default.",
                                accentColor = AuroraViolet
                            )
                        }
                    }
                    
                    // =========================================================
                    // SPLINE PARAMETERS
                    // =========================================================
                    if (selectedCategory == "All" || selectedCategory == "Spline") {
                        item {
                            CategoryHeader(
                                title = "Spline Parameters",
                                icon = Icons.Default.Timeline,
                                color = AuroraMagenta
                            )
                        }
                        
                        item {
                            ParameterSlider(
                                label = "Spline Inflection",
                                value = splineInflection,
                                onValueChange = { splineInflection = it },
                                valueRange = 0.01f..0.5f,
                                description = "Where start/end tension lines cross. Affects curve shape.",
                                accentColor = AuroraMagenta
                            )
                        }
                        
                        item {
                            ParameterSlider(
                                label = "Spline Start Tension",
                                value = splineStartTension,
                                onValueChange = { splineStartTension = it },
                                valueRange = 0.01f..1.0f,
                                description = "Initial momentum at start of fling.",
                                accentColor = AuroraMagenta
                            )
                        }
                        
                        item {
                            ParameterSlider(
                                label = "Spline End Tension",
                                value = splineEndTension,
                                onValueChange = { splineEndTension = it },
                                valueRange = 0.1f..2.0f,
                                description = "Final momentum as fling ends.",
                                accentColor = AuroraMagenta
                            )
                        }
                        
                        item {
                            ParameterSlider(
                                label = "Spline Points",
                                value = numberOfSplinePoints.toFloat(),
                                onValueChange = { numberOfSplinePoints = it.toInt() },
                                valueRange = 10f..500f,
                                description = "Number of sampling points. Higher = smoother curve.",
                                accentColor = AuroraMagenta
                            )
                        }
                    }
                    
                    // =========================================================
                    // PREVIEW SECTION
                    // =========================================================
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "↓ Scroll Preview - Test your configuration",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                    
                    // Preview items with gradient backgrounds
                    items(15) { index ->
                        PreviewCard(index = index)
                    }
                    
                    item {
                        Spacer(modifier = Modifier.height(32.dp))
                    }
                    }
                } // End of key() wrapper
            }
        }
    }
}

@Composable
private fun LegendItem(
    color: Color,
    label: String,
    dashed: Boolean = false
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .width(16.dp)
                .height(if (dashed) 2.dp else 3.dp)
                .clip(RoundedCornerShape(1.dp))
                .background(color.copy(alpha = if (dashed) 0.5f else 1f))
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = color.copy(alpha = 0.8f)
        )
    }
}

@Composable
private fun CategoryChip(
    category: String,
    icon: ImageVector,
    selectedCategory: String,
    onSelect: (String) -> Unit
) {
    FilterChip(
        selected = selectedCategory == category,
        onClick = { onSelect(category) },
        label = { Text(category) },
        leadingIcon = {
            Icon(
                icon,
                contentDescription = null,
                modifier = Modifier.padding(end = 4.dp)
            )
        },
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = MaterialTheme.colorScheme.primary,
            selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
            selectedLeadingIconColor = MaterialTheme.colorScheme.onPrimary
        )
    )
}

@Composable
private fun CategoryHeader(
    title: String,
    icon: ImageVector,
    color: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = color,
            modifier = Modifier.padding(end = 8.dp)
        )
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = color
        )
    }
}

@Composable
private fun PreviewCard(index: Int) {
    val gradient = GradientPresets.forIndex(index)
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.linearGradient(gradient))
                .padding(16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = "Preview Item ${index + 1}",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = Color.White
            )
        }
    }
}

@Composable
private fun ParameterSlider(
    label: String,
    value: Float,
    onValueChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float>,
    description: String,
    accentColor: Color = AuroraCyan
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.85f)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f, fill = false),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = if (value >= 10f) String.format("%.2f", value) else String.format("%.4f", value),
                    style = MaterialTheme.typography.bodySmall,
                    color = accentColor,
                    fontWeight = FontWeight.Bold
                )
            }
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(8.dp))
            Slider(
                value = value,
                onValueChange = onValueChange,
                valueRange = valueRange,
                modifier = Modifier.fillMaxWidth(),
                colors = SliderDefaults.colors(
                    thumbColor = accentColor,
                    activeTrackColor = accentColor,
                    inactiveTrackColor = accentColor.copy(alpha = 0.2f)
                )
            )
            // Min/Max labels
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = String.format("%.2f", valueRange.start),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                )
                Text(
                    text = String.format("%.2f", valueRange.endInclusive),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                )
            }
        }
    }
}
