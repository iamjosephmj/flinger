/*
* MIT License
*
* Copyright (c) 2021 Joseph James
*/

package io.iamjosephmj.flingersample.ui.demos

import androidx.compose.foundation.background
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import io.iamjosephmj.flinger.adaptive.AdaptiveMode
import io.iamjosephmj.flinger.behaviours.FlingPresets
import io.iamjosephmj.flingersample.ui.components.GradientPresets
import io.iamjosephmj.flingersample.ui.components.TranslucentBackground
import io.iamjosephmj.flingersample.ui.theme.AuroraCyan
import io.iamjosephmj.flingersample.ui.theme.AuroraMagenta
import io.iamjosephmj.flingersample.ui.theme.AuroraViolet

/**
 * Demo screen showcasing the velocity-aware adaptive fling behavior.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdaptiveDemo(navController: NavController) {
    var selectedMode by remember { mutableStateOf(AdaptiveMode.Balanced) }
    
    val modeColor = when (selectedMode) {
        AdaptiveMode.Balanced -> AuroraCyan
        AdaptiveMode.Precision -> AuroraViolet
        AdaptiveMode.Momentum -> AuroraMagenta
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "Adaptive Fling Demo",
                        color = modeColor
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
        ) {
            // Mode selector
            Text(
                text = "Adaptive Mode",
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
                AdaptiveMode.entries.forEach { mode ->
                    val chipColor = when (mode) {
                        AdaptiveMode.Balanced -> AuroraCyan
                        AdaptiveMode.Precision -> AuroraViolet
                        AdaptiveMode.Momentum -> AuroraMagenta
                    }
                    FilterChip(
                        selected = selectedMode == mode,
                        onClick = { selectedMode = mode },
                        label = { Text(mode.name) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = chipColor,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Mode description
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(
                                listOf(modeColor.copy(alpha = 0.2f), modeColor.copy(alpha = 0.05f))
                            )
                        )
                        .padding(16.dp)
                ) {
                    Text(
                        text = getModeDescription(selectedMode),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "↓ Try gentle vs aggressive swipes",
                style = MaterialTheme.typography.bodyMedium,
                color = modeColor,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Scrollable list with adaptive behavior
            LazyColumn(
                modifier = Modifier.weight(1f),
                flingBehavior = FlingPresets.adaptive(mode = selectedMode)
            ) {
                items(100) { index ->
                    AdaptiveListItem(index, modeColor)
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
                                        listOf(AuroraCyan, AuroraViolet, AuroraMagenta)
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
                                    text = "Adaptive fling uses different physics based on velocity:\n\n" +
                                            "• Gentle swipes → precise, controlled scrolling\n" +
                                            "• Aggressive swipes → long momentum scrolls\n\n" +
                                            "Each mode balances these behaviors differently.",
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

@Composable
private fun AdaptiveListItem(index: Int, accentColor: Color) {
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
                text = "Item ${index + 1}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = accentColor
            )
            Text(
                text = "Scroll with different velocities to see adaptive behavior",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

private fun getModeDescription(mode: AdaptiveMode): String = when (mode) {
    AdaptiveMode.Balanced -> "Balanced: Controlled for gentle swipes, more momentum for aggressive swipes. Best for general use."
    AdaptiveMode.Precision -> "Precision: Prioritizes control. Even aggressive swipes stop quickly. Best for text-heavy content."
    AdaptiveMode.Momentum -> "Momentum: Prioritizes distance. Swipes travel farther. Best for galleries and feeds."
}
