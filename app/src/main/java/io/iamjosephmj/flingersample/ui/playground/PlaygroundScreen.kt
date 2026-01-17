/*
* MIT License
*
* Copyright (c) 2021 Joseph James
*/

package io.iamjosephmj.flingersample.ui.playground

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import io.iamjosephmj.flinger.configs.FlingConfiguration
import io.iamjosephmj.flinger.flings.flingBehavior
import io.iamjosephmj.flingersample.ui.components.FlingCurveCanvas
import kotlin.math.ln

/**
 * Interactive playground for tuning fling parameters in real-time.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaygroundScreen(navController: NavController) {
    // Fling parameters state
    var scrollFriction by remember { mutableFloatStateOf(0.008f) }
    var decelerationFriction by remember { mutableFloatStateOf(0.09f) }
    var decelerationRate by remember { mutableFloatStateOf((ln(0.78) / ln(0.9)).toFloat()) }
    var splineInflection by remember { mutableFloatStateOf(0.1f) }
    var splineStartTension by remember { mutableFloatStateOf(0.1f) }
    var splineEndTension by remember { mutableFloatStateOf(1.0f) }
    var numberOfSplinePoints by remember { mutableIntStateOf(100) }
    
    // Build configuration from current state
    val currentConfig = remember(
        scrollFriction, decelerationFriction, decelerationRate,
        splineInflection, splineStartTension, splineEndTension, numberOfSplinePoints
    ) {
        FlingConfiguration.Builder()
            .scrollViewFriction(scrollFriction)
            .decelerationFriction(decelerationFriction)
            .decelerationRate(decelerationRate)
            .splineInflection(splineInflection)
            .splineStartTension(splineStartTension)
            .splineEndTension(splineEndTension)
            .numberOfSplinePoints(numberOfSplinePoints)
            .build()
    }
    
    fun resetToDefaults() {
        scrollFriction = 0.008f
        decelerationFriction = 0.09f
        decelerationRate = (ln(0.78) / ln(0.9)).toFloat()
        splineInflection = 0.1f
        splineStartTension = 0.1f
        splineEndTension = 1.0f
        numberOfSplinePoints = 100
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Fling Playground") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { resetToDefaults() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Reset")
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
            // Fling Curve Visualization
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Fling Curve",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    FlingCurveCanvas(
                        config = currentConfig,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Position (solid)",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "Velocity (faded)",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                        )
                    }
                }
            }
            
            // Scrollable preview + parameters
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                flingBehavior = flingBehavior(scrollConfiguration = currentConfig),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    Text(
                        text = "Try scrolling this list!",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
                
                // Preview items
                items(10) { index ->
                    PreviewCard(index = index)
                }
                
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Parameters",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                
                item {
                    ParameterSlider(
                        label = "Scroll Friction",
                        value = scrollFriction,
                        onValueChange = { scrollFriction = it },
                        valueRange = 0.001f..0.1f,
                        description = "Lower = longer scroll distance"
                    )
                }
                
                item {
                    ParameterSlider(
                        label = "Deceleration Friction",
                        value = decelerationFriction,
                        onValueChange = { decelerationFriction = it },
                        valueRange = 0.01f..1.0f,
                        description = "Higher = quicker stop"
                    )
                }
                
                item {
                    ParameterSlider(
                        label = "Deceleration Rate",
                        value = decelerationRate,
                        onValueChange = { decelerationRate = it },
                        valueRange = 1.0f..5.0f,
                        description = "Curve steepness"
                    )
                }
                
                item {
                    ParameterSlider(
                        label = "Spline Inflection",
                        value = splineInflection,
                        onValueChange = { splineInflection = it },
                        valueRange = 0.01f..0.5f,
                        description = "Curve transition point"
                    )
                }
                
                item {
                    ParameterSlider(
                        label = "Spline Start Tension",
                        value = splineStartTension,
                        onValueChange = { splineStartTension = it },
                        valueRange = 0.01f..1.0f,
                        description = "Initial momentum"
                    )
                }
                
                item {
                    ParameterSlider(
                        label = "Spline End Tension",
                        value = splineEndTension,
                        onValueChange = { splineEndTension = it },
                        valueRange = 0.1f..2.0f,
                        description = "Final momentum"
                    )
                }
                
                item {
                    ParameterSlider(
                        label = "Spline Points",
                        value = numberOfSplinePoints.toFloat(),
                        onValueChange = { numberOfSplinePoints = it.toInt() },
                        valueRange = 10f..300f,
                        description = "Curve resolution"
                    )
                }
                
                item {
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}

@Composable
private fun PreviewCard(index: Int) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Preview Item ${index + 1}",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer
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
    description: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
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
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f, fill = false)
                )
                Text(
                    text = String.format("%.4f", value),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Slider(
                value = value,
                onValueChange = onValueChange,
                valueRange = valueRange,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
