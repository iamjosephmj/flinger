/*
* MIT License
*
* Copyright (c) 2021 Joseph James
*/

package io.iamjosephmj.flingersample.ui.presets

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.FlingBehavior
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
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import io.iamjosephmj.flingersample.R
import io.iamjosephmj.flinger.behaviours.FlingPresets
import io.iamjosephmj.flinger.configs.FlingConfiguration
import io.iamjosephmj.flingersample.ui.components.MiniCurvePreview
import io.iamjosephmj.flingersample.ui.components.TranslucentBackground
import io.iamjosephmj.flingersample.ui.theme.AuroraCyan
import io.iamjosephmj.flingersample.ui.theme.AuroraMagenta
import io.iamjosephmj.flingersample.ui.theme.AuroraViolet

/**
 * Data class representing a preset for display.
 */
data class PresetInfo(
    val name: String,
    val description: String,
    val bestFor: String,
    val config: FlingConfiguration,
    val gradient: List<Color>,
    val getFlingBehavior: @Composable () -> FlingBehavior
)

/**
 * Gallery screen showing all available presets with visual previews.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PresetsGalleryScreen(navController: NavController) {
    // Note: presets list uses stringResource which requires recomposition on locale change
    val presets = listOf(
        PresetInfo(
            name = stringResource(R.string.preset_smooth),
            description = stringResource(R.string.preset_smooth_desc),
            bestFor = stringResource(R.string.best_for_general),
            config = FlingConfiguration.Builder().build(),
            gradient = listOf(AuroraCyan, AuroraViolet),
            getFlingBehavior = { FlingPresets.smooth() }
        ),
        PresetInfo(
            name = stringResource(R.string.preset_ios_style),
            description = stringResource(R.string.preset_ios_desc),
            bestFor = stringResource(R.string.best_for_cross_platform),
            config = FlingConfiguration.Builder().scrollViewFriction(0.04f).build(),
            gradient = listOf(Color(0xFF007AFF), Color(0xFF5856D6)),
            getFlingBehavior = { FlingPresets.iOSStyle() }
        ),
        PresetInfo(
            name = stringResource(R.string.preset_quick_stop),
            description = stringResource(R.string.preset_quick_stop_desc),
            bestFor = stringResource(R.string.best_for_text),
            config = FlingConfiguration.Builder().decelerationFriction(0.5f).build(),
            gradient = listOf(Color(0xFFFF512F), Color(0xFFDD2476)),
            getFlingBehavior = { FlingPresets.quickStop() }
        ),
        PresetInfo(
            name = stringResource(R.string.preset_bouncy),
            description = stringResource(R.string.preset_bouncy_desc),
            bestFor = stringResource(R.string.best_for_games),
            config = FlingConfiguration.Builder()
                .decelerationFriction(0.6f)
                .splineInflection(0.4f)
                .build(),
            gradient = listOf(Color(0xFFFF6B6B), AuroraMagenta),
            getFlingBehavior = { FlingPresets.bouncy() }
        ),
        PresetInfo(
            name = stringResource(R.string.preset_floaty),
            description = stringResource(R.string.preset_floaty_desc),
            bestFor = stringResource(R.string.best_for_galleries),
            config = FlingConfiguration.Builder()
                .scrollViewFriction(0.09f)
                .decelerationFriction(0.015f)
                .build(),
            gradient = listOf(AuroraCyan, AuroraMagenta, Color(0xFFFFAA00)),
            getFlingBehavior = { FlingPresets.floaty() }
        ),
        PresetInfo(
            name = stringResource(R.string.preset_snappy),
            description = stringResource(R.string.preset_snappy_desc),
            bestFor = stringResource(R.string.best_for_ecommerce),
            config = FlingConfiguration.Builder()
                .scrollViewFriction(0.03f)
                .decelerationFriction(0.2f)
                .build(),
            gradient = listOf(AuroraMagenta, Color(0xFFE91E8C)),
            getFlingBehavior = { FlingPresets.snappy() }
        ),
        PresetInfo(
            name = stringResource(R.string.preset_ultra_smooth),
            description = stringResource(R.string.preset_ultra_smooth_desc),
            bestFor = stringResource(R.string.best_for_luxury),
            config = FlingConfiguration.Builder()
                .scrollViewFriction(0.006f)
                .decelerationFriction(0.05f)
                .numberOfSplinePoints(150)
                .build(),
            gradient = listOf(AuroraCyan, AuroraViolet, AuroraMagenta),
            getFlingBehavior = { FlingPresets.ultraSmooth() }
        ),
        PresetInfo(
            name = stringResource(R.string.preset_smooth_curve),
            description = stringResource(R.string.preset_smooth_curve_desc),
            bestFor = stringResource(R.string.best_for_creative),
            config = FlingConfiguration.Builder()
                .splineInflection(0.16f)
                .build(),
            gradient = listOf(AuroraViolet, Color(0xFF6246D8)),
            getFlingBehavior = { FlingPresets.smoothCurve() }
        ),
        PresetInfo(
            name = stringResource(R.string.preset_android_native),
            description = stringResource(R.string.preset_android_native_desc),
            bestFor = stringResource(R.string.best_for_baseline),
            config = FlingConfiguration.Builder().build(),
            gradient = listOf(Color(0xFF4CAF50), Color(0xFF2E7D32)),
            getFlingBehavior = { FlingPresets.androidNative() }
        )
    )
    
    var selectedPreset by remember { mutableStateOf<PresetInfo?>(null) }
    val currentFlingBehavior = selectedPreset?.getFlingBehavior?.invoke() ?: FlingPresets.smooth()
    
    TranslucentBackground {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = { 
                        Text(
                            stringResource(R.string.presets_gallery_title),
                            color = MaterialTheme.colorScheme.tertiary
                        ) 
                    },
                    navigationIcon = {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.action_back))
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
            // Selected preset info
            if (selectedPreset != null) {
                SelectedPresetHeader(preset = selectedPreset!!)
            } else {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
                    )
                ) {
                    Text(
                        text = stringResource(R.string.presets_select_prompt),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(16.dp)
                    )
                }
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
                        text = stringResource(R.string.presets_available),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
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
                        text = stringResource(R.string.presets_scroll_test),
                        style = MaterialTheme.typography.bodySmall,
                        color = AuroraCyan,
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
}

@Composable
private fun SelectedPresetHeader(preset: PresetInfo) {
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
                .background(Brush.linearGradient(preset.gradient))
                .padding(20.dp)
        ) {
            Column {
                Text(
                    text = stringResource(R.string.presets_active, preset.name),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = preset.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.9f)
                )
            }
        }
    }
}

@Composable
private fun PresetCard(
    preset: PresetInfo,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.02f else 1f,
        animationSpec = tween(200),
        label = "card_scale"
    )
    
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) 
                MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.9f)
            else 
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.8f)
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 6.dp else 2.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Mini curve preview with gradient
            Card(
                modifier = Modifier.width(100.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                MiniCurvePreview(
                    config = preset.config,
                    modifier = Modifier.padding(8.dp),
                    curveColor = preset.gradient.first(),
                    animated = isSelected
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
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    if (isSelected) {
                        Spacer(modifier = Modifier.width(8.dp))
                        FilterChip(
                            selected = true,
                            onClick = {},
                            label = { 
                                Text(
                                    stringResource(R.string.presets_active_chip), 
                                    style = MaterialTheme.typography.labelSmall
                                ) 
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = preset.gradient.first(),
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = preset.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stringResource(R.string.presets_best_for, preset.bestFor),
                    style = MaterialTheme.typography.labelSmall,
                    color = preset.gradient.first(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
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
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.presets_test_item, index + 1),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
