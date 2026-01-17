/*
* MIT License
*
* Copyright (c) 2021 Joseph James
*
* Permission is hereby granted, free of charge, to any person obtaining a copy
* of this software and associated documentation files (the "Software"), to deal
* in the Software without restriction, including without limitation the rights
* to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
* copies of the Software, and to permit persons to whom the Software is
* furnished to do so, subject to the following conditions:
*
* The above copyright notice and this permission notice shall be included in all
* copies or substantial portions of the Software.
*
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
* IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
* AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
* OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
* SOFTWARE.
*
*/

package io.iamjosephmj.flinger.debug

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.iamjosephmj.flinger.callbacks.FlingCallbacks
import io.iamjosephmj.flinger.configs.FlingConfiguration
import io.iamjosephmj.flinger.flings.flingBehavior
import kotlin.math.abs

/**
 * A debug overlay composable that visualizes fling behavior in real-time.
 *
 * This overlay is designed for development and debugging purposes. It shows:
 * - Real-time velocity indicator
 * - Live fling curve visualization
 * - Metrics panel with fling statistics
 *
 * ## Usage Example
 * ```kotlin
 * // Wrap your scrollable content
 * FlingerDebugOverlay(
 *     enabled = BuildConfig.DEBUG
 * ) { flingBehavior ->
 *     LazyColumn(
 *         flingBehavior = flingBehavior
 *     ) {
 *         items(100) { Text("Item $it") }
 *     }
 * }
 * ```
 *
 * ## Customization
 * ```kotlin
 * FlingerDebugOverlay(
 *     enabled = isDebugMode,
 *     showVelocity = true,
 *     showCurve = true,
 *     showMetrics = true,
 *     configuration = myCustomConfig
 * ) { flingBehavior ->
 *     LazyColumn(flingBehavior = flingBehavior) { ... }
 * }
 * ```
 *
 * @param enabled Whether the debug overlay is visible.
 * @param showVelocity Whether to show the velocity indicator.
 * @param showCurve Whether to show the fling curve visualization.
 * @param showMetrics Whether to show the metrics panel.
 * @param configuration The fling configuration to use and visualize.
 * @param content The scrollable content. Receives a flingBehavior with debugging enabled.
 *
 * @author Joseph James
 */
@Composable
fun FlingerDebugOverlay(
    enabled: Boolean = true,
    showVelocity: Boolean = true,
    showCurve: Boolean = true,
    showMetrics: Boolean = true,
    configuration: FlingConfiguration = FlingConfiguration.Default,
    modifier: Modifier = Modifier,
    content: @Composable (FlingBehavior) -> Unit
) {
    // Fling state tracking
    var isFlinging by remember { mutableStateOf(false) }
    var currentVelocity by remember { mutableFloatStateOf(0f) }
    var initialVelocity by remember { mutableFloatStateOf(0f) }
    var progress by remember { mutableFloatStateOf(0f) }
    var totalScrolled by remember { mutableFloatStateOf(0f) }
    var flingCount by remember { mutableIntStateOf(0) }
    var lastFlingStart by remember { mutableLongStateOf(0L) }
    var lastFlingDuration by remember { mutableLongStateOf(0L) }
    
    // Velocity history for curve visualization
    val velocityHistory = remember { mutableStateListOf<Float>() }
    
    // Create callbacks that update debug state
    val debugCallbacks = remember {
        FlingCallbacks(
            onFlingStart = { velocity ->
                isFlinging = true
                initialVelocity = velocity
                currentVelocity = velocity
                progress = 0f
                totalScrolled = 0f
                lastFlingStart = System.currentTimeMillis()
                velocityHistory.clear()
                velocityHistory.add(velocity)
            },
            onFlingProgress = { prog, velocity ->
                progress = prog
                currentVelocity = velocity
                if (velocityHistory.size < 100) {
                    velocityHistory.add(velocity)
                }
            },
            onFlingEnd = { scrolled, _ ->
                isFlinging = false
                totalScrolled = scrolled
                flingCount++
                lastFlingDuration = System.currentTimeMillis() - lastFlingStart
                currentVelocity = 0f
            }
        )
    }
    
    // Create fling behavior with debug callbacks
    val flingBehavior = flingBehavior(
        scrollConfiguration = configuration,
        callbacks = debugCallbacks
    )
    
    // Build metrics object
    val metrics = remember(
        isFlinging, currentVelocity, initialVelocity, progress,
        totalScrolled, flingCount, lastFlingDuration
    ) {
        FlingMetrics(
            isFlinging = isFlinging,
            initialVelocity = initialVelocity,
            currentVelocity = currentVelocity,
            progress = progress,
            totalScrolled = totalScrolled,
            flingCount = flingCount,
            lastFlingDuration = lastFlingDuration,
            configuration = configuration
        )
    }
    
    Box(modifier = modifier.fillMaxSize()) {
        // Main content
        content(flingBehavior)
        
        // Debug overlay
        AnimatedVisibility(
            visible = enabled,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Velocity indicator at top
                if (showVelocity) {
                    VelocityIndicator(
                        velocity = currentVelocity,
                        maxVelocity = 8000f,
                        isFlinging = isFlinging
                    )
                }
                
                // Curve visualization
                if (showCurve && velocityHistory.isNotEmpty()) {
                    VelocityCurve(
                        velocityHistory = velocityHistory,
                        maxVelocity = abs(initialVelocity).coerceAtLeast(1000f),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp)
                    )
                }
                
                Spacer(modifier = Modifier.weight(1f))
                
                // Metrics panel at bottom
                if (showMetrics) {
                    MetricsPanel(metrics = metrics)
                }
            }
        }
    }
}

/**
 * Visual indicator showing current fling velocity.
 */
@Composable
private fun VelocityIndicator(
    velocity: Float,
    maxVelocity: Float,
    isFlinging: Boolean
) {
    val absVelocity = abs(velocity)
    val fillFraction = (absVelocity / maxVelocity).coerceIn(0f, 1f)
    
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Color.Black.copy(alpha = 0.7f))
            .padding(12.dp)
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(
                            color = if (isFlinging) Color.Green else Color.Gray,
                            shape = RoundedCornerShape(4.dp)
                        )
                )
                Spacer(modifier = Modifier.width(8.dp))
                BasicText(
                    text = if (isFlinging) "FLINGING" else "IDLE",
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                )
                Spacer(modifier = Modifier.weight(1f))
                BasicText(
                    text = "%.0f px/s".format(velocity),
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                )
            }
            
            Spacer(modifier = Modifier.height(4.dp))
            
            // Velocity bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .background(Color.Gray.copy(alpha = 0.3f), RoundedCornerShape(2.dp))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(fillFraction)
                        .height(4.dp)
                        .background(
                            color = when {
                                fillFraction > 0.7f -> Color.Red
                                fillFraction > 0.4f -> Color.Yellow
                                else -> Color.Green
                            },
                            shape = RoundedCornerShape(2.dp)
                        )
                )
            }
        }
    }
}

/**
 * Real-time velocity curve visualization.
 */
@Composable
private fun VelocityCurve(
    velocityHistory: List<Float>,
    maxVelocity: Float,
    modifier: Modifier = Modifier
) {
    val curveColor = Color(0xFF6200EE) // Primary purple
    val backgroundColor = Color.Black.copy(alpha = 0.7f)
    
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            if (velocityHistory.isEmpty()) return@Canvas
            
            val width = size.width
            val height = size.height
            val path = Path()
            
            velocityHistory.forEachIndexed { index, velocity ->
                val x = (index.toFloat() / (velocityHistory.size - 1).coerceAtLeast(1)) * width
                val normalizedVelocity = abs(velocity) / maxVelocity
                val y = height - (normalizedVelocity * height)
                
                if (index == 0) {
                    path.moveTo(x, y)
                } else {
                    path.lineTo(x, y)
                }
            }
            
            drawPath(
                path = path,
                color = curveColor,
                style = Stroke(width = 2f)
            )
            
            // Draw baseline
            drawLine(
                color = Color.White.copy(alpha = 0.3f),
                start = Offset(0f, height),
                end = Offset(width, height),
                strokeWidth = 1f
            )
        }
    }
}

/**
 * Panel showing fling metrics and statistics.
 */
@Composable
private fun MetricsPanel(metrics: FlingMetrics) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Color.Black.copy(alpha = 0.7f))
            .padding(12.dp)
    ) {
        Column {
            BasicText(
                text = "FLING METRICS",
                style = TextStyle(
                    color = Color.White,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                MetricItem("Progress", metrics.progressText)
                MetricItem("Scrolled", metrics.scrolledText)
                MetricItem("Flings", metrics.flingCount.toString())
            }
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                MetricItem("Init Vel", "%.0f".format(metrics.initialVelocity))
                MetricItem("Duration", "${metrics.lastFlingDuration}ms")
            }
            
            // Show config summary
            metrics.configuration?.let { config ->
                Spacer(modifier = Modifier.height(8.dp))
                BasicText(
                    text = "friction: ${config.scrollFriction} | decel: ${config.decelerationFriction}",
                    style = TextStyle(
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 9.sp,
                        fontFamily = FontFamily.Monospace
                    )
                )
            }
        }
    }
}

@Composable
private fun MetricItem(label: String, value: String) {
    Column {
        BasicText(
            text = label,
            style = TextStyle(
                color = Color.White.copy(alpha = 0.6f),
                fontSize = 9.sp,
                fontFamily = FontFamily.Monospace
            )
        )
        BasicText(
            text = value,
            style = TextStyle(
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
            )
        )
    }
}
