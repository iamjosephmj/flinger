/*
* MIT License
*
* Copyright (c) 2021 Joseph James
*/

package io.iamjosephmj.flingersample.ui.demos

import androidx.compose.foundation.background
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import io.iamjosephmj.flingersample.R
import io.iamjosephmj.flinger.configs.FlingConfiguration
import io.iamjosephmj.flinger.debug.FlingerDebugOverlay
import io.iamjosephmj.flingersample.ui.components.GradientPresets
import io.iamjosephmj.flingersample.ui.components.TranslucentBackground
import io.iamjosephmj.flingersample.ui.theme.AuroraCyan
import io.iamjosephmj.flingersample.ui.theme.AuroraMagenta
import io.iamjosephmj.flingersample.ui.theme.AuroraViolet

/**
 * Demo screen showcasing the debug overlay feature.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DebugOverlayDemo(navController: NavController) {
    var showVelocity by remember { mutableStateOf(true) }
    var showCurve by remember { mutableStateOf(true) }
    var showMetrics by remember { mutableStateOf(true) }
    var debugEnabled by remember { mutableStateOf(true) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        stringResource(R.string.debug_demo_title),
                        color = AuroraMagenta
                    ) 
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.action_back))
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
            // Controls
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.8f)
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = stringResource(R.string.debug_options),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = AuroraMagenta
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    ToggleRow(
                        label = stringResource(R.string.debug_overlay), 
                        checked = debugEnabled, 
                        onCheckedChange = { debugEnabled = it },
                        accentColor = AuroraCyan
                    )
                    ToggleRow(
                        label = stringResource(R.string.debug_show_velocity), 
                        checked = showVelocity, 
                        onCheckedChange = { showVelocity = it },
                        accentColor = AuroraViolet
                    )
                    ToggleRow(
                        label = stringResource(R.string.debug_show_curve), 
                        checked = showCurve, 
                        onCheckedChange = { showCurve = it },
                        accentColor = AuroraMagenta
                    )
                    ToggleRow(
                        label = stringResource(R.string.debug_show_metrics), 
                        checked = showMetrics, 
                        onCheckedChange = { showMetrics = it },
                        accentColor = AuroraCyan
                    )
                }
            }
            
            Text(
                text = stringResource(R.string.debug_scroll_prompt),
                style = MaterialTheme.typography.bodyMedium,
                color = AuroraCyan,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // List with debug overlay
            FlingerDebugOverlay(
                enabled = debugEnabled,
                showVelocity = showVelocity,
                showCurve = showCurve,
                showMetrics = showMetrics,
                configuration = FlingConfiguration.Builder().build(),
                modifier = Modifier.weight(1f)
            ) { flingBehavior ->
                LazyColumn(
                    flingBehavior = flingBehavior,
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(50) { index ->
                        DebugListItem(index)
                    }
                    
                    // Info card at the end
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
                                            listOf(AuroraMagenta, AuroraViolet, AuroraCyan)
                                        )
                                    )
                                    .padding(20.dp)
                            ) {
                                Column {
                                    Text(
                                        text = stringResource(R.string.debug_overlay_info),
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = stringResource(R.string.debug_overlay_info_desc),
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
}

@Composable
private fun ToggleRow(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    accentColor: Color = AuroraCyan
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f),
            color = if (checked) accentColor else MaterialTheme.colorScheme.onSurfaceVariant
        )
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = accentColor,
                checkedTrackColor = accentColor.copy(alpha = 0.5f)
            )
        )
    }
}

@Composable
private fun DebugListItem(index: Int) {
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
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Gradient indicator
            Box(
                modifier = Modifier
                    .height(40.dp)
                    .fillMaxWidth(0.02f)
                    .background(
                        Brush.verticalGradient(gradient),
                        RoundedCornerShape(2.dp)
                    )
            )
            Column(
                modifier = Modifier.padding(start = 12.dp)
            ) {
                Text(
                    text = stringResource(R.string.debug_item, index + 1),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = gradient.first()
                )
                Text(
                    text = stringResource(R.string.debug_item_desc),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
