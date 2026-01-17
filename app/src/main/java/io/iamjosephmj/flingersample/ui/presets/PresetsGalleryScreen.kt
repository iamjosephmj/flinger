/*
* MIT License
*
* Copyright (c) 2021 Joseph James
*/

package io.iamjosephmj.flingersample.ui.presets

import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.items
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
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import io.iamjosephmj.flinger.behaviours.FlingPresets
import io.iamjosephmj.flinger.configs.FlingConfiguration
import io.iamjosephmj.flingersample.ui.components.MiniCurvePreview

/**
 * Data class representing a preset for display.
 */
data class PresetInfo(
    val name: String,
    val description: String,
    val bestFor: String,
    val config: FlingConfiguration,
    val getFlingBehavior: @Composable () -> FlingBehavior
)

/**
 * Gallery screen showing all available presets with visual previews.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PresetsGalleryScreen(navController: NavController) {
    val presets = remember {
        listOf(
            PresetInfo(
                name = "Smooth",
                description = "Balanced, smooth scrolling with default parameters",
                bestFor = "General purpose, most list UIs",
                config = FlingConfiguration.Builder().build(),
                getFlingBehavior = { FlingPresets.smooth() }
            ),
            PresetInfo(
                name = "iOS Style",
                description = "Higher friction mimicking iOS scroll physics",
                bestFor = "Cross-platform apps, photo browsers",
                config = FlingConfiguration.Builder().scrollViewFriction(0.04f).build(),
                getFlingBehavior = { FlingPresets.iOSStyle() }
            ),
            PresetInfo(
                name = "Quick Stop",
                description = "High deceleration for precise control",
                bestFor = "Text-heavy content, settings screens",
                config = FlingConfiguration.Builder().decelerationFriction(0.5f).build(),
                getFlingBehavior = { FlingPresets.quickStop() }
            ),
            PresetInfo(
                name = "Bouncy",
                description = "Playful, responsive feel with modified spline",
                bestFor = "Games, children's apps, playful UIs",
                config = FlingConfiguration.Builder()
                    .decelerationFriction(0.6f)
                    .splineInflection(0.4f)
                    .build(),
                getFlingBehavior = { FlingPresets.bouncy() }
            ),
            PresetInfo(
                name = "Floaty",
                description = "Long-distance gliding with low friction",
                bestFor = "Photo galleries, social feeds",
                config = FlingConfiguration.Builder()
                    .scrollViewFriction(0.09f)
                    .decelerationFriction(0.015f)
                    .build(),
                getFlingBehavior = { FlingPresets.floaty() }
            ),
            PresetInfo(
                name = "Snappy",
                description = "Quick response with controlled momentum",
                bestFor = "E-commerce, chat apps",
                config = FlingConfiguration.Builder()
                    .scrollViewFriction(0.03f)
                    .decelerationFriction(0.2f)
                    .build(),
                getFlingBehavior = { FlingPresets.snappy() }
            ),
            PresetInfo(
                name = "Ultra Smooth",
                description = "Premium feel with extended momentum",
                bestFor = "Luxury apps, reading apps",
                config = FlingConfiguration.Builder()
                    .scrollViewFriction(0.006f)
                    .decelerationFriction(0.05f)
                    .numberOfSplinePoints(150)
                    .build(),
                getFlingBehavior = { FlingPresets.ultraSmooth() }
            ),
            PresetInfo(
                name = "Smooth Curve",
                description = "Modified spline for unique feel",
                bestFor = "Creative apps, experimentation",
                config = FlingConfiguration.Builder()
                    .splineInflection(0.16f)
                    .build(),
                getFlingBehavior = { FlingPresets.smoothCurve() }
            ),
            PresetInfo(
                name = "Android Native",
                description = "Default Android fling behavior",
                bestFor = "Baseline comparison",
                config = FlingConfiguration.Builder().build(),
                getFlingBehavior = { FlingPresets.androidNative() }
            )
        )
    }
    
    var selectedPreset by remember { mutableStateOf<PresetInfo?>(null) }
    val currentFlingBehavior = selectedPreset?.getFlingBehavior?.invoke() ?: FlingPresets.smooth()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Preset Gallery") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
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
        ) {
            // Selected preset info
            if (selectedPreset != null) {
                SelectedPresetHeader(preset = selectedPreset!!)
            } else {
                Text(
                    text = "Select a preset to try it",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(16.dp)
                )
            }
            
            // List with selected fling behavior
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                flingBehavior = currentFlingBehavior
            ) {
                item {
                    Text(
                        text = "Available Presets",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
                
                items(presets) { preset ->
                    PresetCard(
                        preset = preset,
                        isSelected = selectedPreset == preset,
                        onClick = { selectedPreset = preset }
                    )
                }
                
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Scroll this list to feel the selected preset!",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
                
                // Extra items to allow scrolling
                items(20) { index ->
                    TestScrollCard(index = index)
                }
            }
        }
    }
}

@Composable
private fun SelectedPresetHeader(preset: PresetInfo) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Active: ${preset.name}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = preset.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
            )
        }
    }
}

@Composable
private fun PresetCard(
    preset: PresetInfo,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) 
                MaterialTheme.colorScheme.secondaryContainer 
            else 
                MaterialTheme.colorScheme.surfaceVariant
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Mini curve preview
            Card(
                modifier = Modifier.width(100.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                MiniCurvePreview(
                    config = preset.config,
                    modifier = Modifier.padding(8.dp),
                    curveColor = if (isSelected)
                        MaterialTheme.colorScheme.secondary
                    else
                        MaterialTheme.colorScheme.primary
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = preset.name,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold
                    )
                    if (isSelected) {
                        Spacer(modifier = Modifier.width(8.dp))
                        FilterChip(
                            selected = true,
                            onClick = {},
                            label = { Text("Active", style = MaterialTheme.typography.labelSmall) }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = preset.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Best for: ${preset.bestFor}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
private fun TestScrollCard(index: Int) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Test Item ${index + 1}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
