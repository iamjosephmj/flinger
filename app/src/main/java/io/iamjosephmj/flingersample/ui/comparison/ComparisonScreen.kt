/*
* MIT License
*
* Copyright (c) 2021 Joseph James
*/

package io.iamjosephmj.flingersample.ui.comparison

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import io.iamjosephmj.flinger.behaviours.FlingPresets
import io.iamjosephmj.flingersample.ui.components.TranslucentBackground
import io.iamjosephmj.flingersample.ui.theme.AuroraCyan
import io.iamjosephmj.flingersample.ui.theme.AuroraMagenta
import io.iamjosephmj.flingersample.ui.theme.AuroraViolet

/**
 * Side-by-side comparison screen to compare different fling behaviors.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComparisonScreen(navController: NavController) {
    val presetOptions = listOf(
        "Android Native" to @Composable { FlingPresets.androidNative() },
        "Smooth" to @Composable { FlingPresets.smooth() },
        "iOS Style" to @Composable { FlingPresets.iOSStyle() },
        "Quick Stop" to @Composable { FlingPresets.quickStop() },
        "Bouncy" to @Composable { FlingPresets.bouncy() },
        "Floaty" to @Composable { FlingPresets.floaty() },
        "Snappy" to @Composable { FlingPresets.snappy() },
        "Ultra Smooth" to @Composable { FlingPresets.ultraSmooth() }
    )
    
    var leftPresetIndex by remember { mutableStateOf(0) } // Android Native
    var rightPresetIndex by remember { mutableStateOf(1) } // Smooth
    
    val leftFlingBehavior = presetOptions[leftPresetIndex].second()
    val rightFlingBehavior = presetOptions[rightPresetIndex].second()
    
    TranslucentBackground {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = { 
                        Text(
                            "Side-by-Side Comparison",
                            color = MaterialTheme.colorScheme.secondary
                        ) 
                    },
                    navigationIcon = {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
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
            // Instructions
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(
                                listOf(AuroraCyan.copy(alpha = 0.2f), AuroraViolet.copy(alpha = 0.2f))
                            )
                        )
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Scroll both lists to feel the difference between behaviors!",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            
            // Preset selectors
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                PresetDropdown(
                    label = "Left Panel",
                    options = presetOptions.map { it.first },
                    selectedIndex = leftPresetIndex,
                    onSelectionChange = { leftPresetIndex = it },
                    accentColor = AuroraCyan,
                    modifier = Modifier.weight(1f)
                )
                PresetDropdown(
                    label = "Right Panel",
                    options = presetOptions.map { it.first },
                    selectedIndex = rightPresetIndex,
                    onSelectionChange = { rightPresetIndex = it },
                    accentColor = AuroraMagenta,
                    modifier = Modifier.weight(1f)
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Side-by-side scrollable lists
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 16.dp)
            ) {
                // Left panel
                ComparisonPanel(
                    title = presetOptions[leftPresetIndex].first,
                    flingBehavior = leftFlingBehavior,
                    gradientColors = listOf(AuroraCyan.copy(alpha = 0.1f), AuroraViolet.copy(alpha = 0.05f)),
                    headerGradient = listOf(AuroraCyan, AuroraViolet),
                    modifier = Modifier.weight(1f)
                )
                
                // Animated Divider
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(2.dp)
                        .padding(horizontal = 4.dp, vertical = 8.dp)
                        .clip(RoundedCornerShape(1.dp))
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    AuroraCyan,
                                    AuroraViolet,
                                    AuroraMagenta
                                )
                            )
                        )
                )
                
                // Right panel
                ComparisonPanel(
                    title = presetOptions[rightPresetIndex].first,
                    flingBehavior = rightFlingBehavior,
                    gradientColors = listOf(AuroraMagenta.copy(alpha = 0.1f), AuroraViolet.copy(alpha = 0.05f)),
                    headerGradient = listOf(AuroraMagenta, AuroraViolet),
                    modifier = Modifier.weight(1f)
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
        }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PresetDropdown(
    label: String,
    options: List<String>,
    selectedIndex: Int,
    onSelectionChange: (Int) -> Unit,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    
    Column(modifier = modifier) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = accentColor,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(4.dp))
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it }
        ) {
            TextField(
                value = options[selectedIndex],
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                    .fillMaxWidth(),
                textStyle = MaterialTheme.typography.bodySmall,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    focusedIndicatorColor = accentColor,
                    unfocusedIndicatorColor = accentColor.copy(alpha = 0.5f)
                ),
                shape = RoundedCornerShape(12.dp)
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                options.forEachIndexed { index, option ->
                    DropdownMenuItem(
                        text = { 
                            Text(
                                option, 
                                style = MaterialTheme.typography.bodySmall,
                                color = if (index == selectedIndex) accentColor else MaterialTheme.colorScheme.onSurface
                            ) 
                        },
                        onClick = {
                            onSelectionChange(index)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun ComparisonPanel(
    title: String,
    flingBehavior: FlingBehavior,
    gradientColors: List<Color>,
    headerGradient: List<Color>,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()
    
    Column(
        modifier = modifier
            .fillMaxHeight()
    ) {
        // Panel header with gradient
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.linearGradient(headerGradient),
                    shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                )
                .padding(12.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        
        // Scrollable list
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(
                    brush = Brush.verticalGradient(gradientColors),
                    shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)
                ),
            flingBehavior = flingBehavior,
            verticalArrangement = Arrangement.spacedBy(4.dp),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(8.dp)
        ) {
            items(50) { index ->
                ComparisonItem(index = index)
            }
        }
    }
}

@Composable
private fun ComparisonItem(index: Int) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
        ),
        shape = RoundedCornerShape(10.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = "Item ${index + 1}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
