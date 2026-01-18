/*
* MIT License
*
* Copyright (c) 2021 Joseph James
*/

package io.iamjosephmj.flingersample.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.iamjosephmj.flinger.configs.FlingConfiguration
import io.iamjosephmj.flinger.spline.AndroidFlingSpline
import io.iamjosephmj.flingersample.ui.theme.AuroraCyan
import io.iamjosephmj.flingersample.ui.theme.AuroraMagenta
import io.iamjosephmj.flingersample.ui.theme.AuroraViolet

/**
 * A Canvas composable that visualizes the fling deceleration curve
 * based on the provided configuration with glow effects and animations.
 */
@Composable
fun FlingCurveCanvas(
    config: FlingConfiguration,
    modifier: Modifier = Modifier,
    curveColor: Color = AuroraCyan,
    velocityColor: Color = AuroraMagenta,
    gridColor: Color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f),
    backgroundColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    showLabels: Boolean = true,
    animated: Boolean = true
) {
    val spline = remember(config) { AndroidFlingSpline(config) }
    val textMeasurer = rememberTextMeasurer()
    val axisColor = MaterialTheme.colorScheme.onSurface
    
    val infiniteTransition = rememberInfiniteTransition(label = "curve_glow")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow_pulse"
    )
    
    val actualGlowAlpha = if (animated) glowAlpha else 0.6f
    
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor)
            .padding(16.dp)
    ) {
        val width = size.width
        val height = size.height
        val padding = 30f
        val graphWidth = width - padding * 2
        val graphHeight = height - padding * 2
        
        // Draw subtle gradient background overlay
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(
                    curveColor.copy(alpha = 0.03f),
                    Color.Transparent,
                    velocityColor.copy(alpha = 0.02f)
                )
            )
        )
        
        // Draw animated grid lines with glow
        val gridLines = 5
        for (i in 0..gridLines) {
            val y = padding + (graphHeight / gridLines) * i
            val x = padding + (graphWidth / gridLines) * i
            
            // Horizontal grid lines
            drawLine(
                color = gridColor,
                start = Offset(padding, y),
                end = Offset(width - padding, y),
                strokeWidth = 1f,
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 4f), 0f)
            )
            
            // Vertical grid lines
            drawLine(
                color = gridColor,
                start = Offset(x, padding),
                end = Offset(x, height - padding),
                strokeWidth = 1f,
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 4f), 0f)
            )
        }
        
        // Draw position curve with gradient and glow
        val positionPath = Path()
        val samples = 100
        
        for (i in 0..samples) {
            val t = i.toFloat() / samples
            val distanceCoefficient = spline.flingDistanceCoefficient(t)
            val x = padding + t * graphWidth
            val y = height - padding - distanceCoefficient * graphHeight
            
            if (i == 0) {
                positionPath.moveTo(x, y)
            } else {
                positionPath.lineTo(x, y)
            }
        }
        
        // Glow effect for position curve (drawn first, thicker, more transparent)
        drawPath(
            path = positionPath,
            brush = Brush.horizontalGradient(
                colors = listOf(
                    curveColor.copy(alpha = actualGlowAlpha * 0.3f),
                    AuroraViolet.copy(alpha = actualGlowAlpha * 0.3f)
                )
            ),
            style = Stroke(width = 12f, cap = StrokeCap.Round)
        )
        
        // Main position curve with gradient
        drawPath(
            path = positionPath,
            brush = Brush.horizontalGradient(
                colors = listOf(curveColor, AuroraViolet)
            ),
            style = Stroke(width = 3f, cap = StrokeCap.Round)
        )
        
        // Draw velocity curve
        val velocityPath = Path()
        var maxVelocity = 0f
        
        // First pass to find max velocity
        for (i in 0..samples) {
            val t = i.toFloat() / samples
            val velocityCoefficient = spline.flingVelocityCoefficient(t)
            if (velocityCoefficient > maxVelocity) {
                maxVelocity = velocityCoefficient
            }
        }
        
        // Second pass to draw normalized velocity
        if (maxVelocity > 0) {
            for (i in 0..samples) {
                val t = i.toFloat() / samples
                val velocityCoefficient = spline.flingVelocityCoefficient(t)
                val x = padding + t * graphWidth
                val normalizedVelocity = velocityCoefficient / maxVelocity
                val y = height - padding - normalizedVelocity * graphHeight * 0.6f
                
                if (i == 0) {
                    velocityPath.moveTo(x, y)
                } else {
                    velocityPath.lineTo(x, y)
                }
            }
            
            // Glow for velocity curve
            drawPath(
                path = velocityPath,
                color = velocityColor.copy(alpha = actualGlowAlpha * 0.2f),
                style = Stroke(width = 8f, cap = StrokeCap.Round)
            )
            
            // Main velocity curve
            drawPath(
                path = velocityPath,
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        velocityColor.copy(alpha = 0.6f),
                        velocityColor.copy(alpha = 0.3f)
                    )
                ),
                style = Stroke(
                    width = 2f,
                    cap = StrokeCap.Round,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 5f), 0f)
                )
            )
        }
        
        // Draw axes with glow
        // X-axis (Time)
        drawLine(
            color = axisColor.copy(alpha = 0.6f),
            start = Offset(padding, height - padding),
            end = Offset(width - padding, height - padding),
            strokeWidth = 2f,
            cap = StrokeCap.Round
        )
        
        // Y-axis (Distance)
        drawLine(
            color = axisColor.copy(alpha = 0.6f),
            start = Offset(padding, padding),
            end = Offset(padding, height - padding),
            strokeWidth = 2f,
            cap = StrokeCap.Round
        )
        
        // Draw axis labels
        if (showLabels) {
            // Time label (X-axis)
            val timeLabel = "Time →"
            val timeMeasured = textMeasurer.measure(
                text = timeLabel,
                style = TextStyle(fontSize = 10.sp)
            )
            drawText(
                textMeasurer = textMeasurer,
                text = timeLabel,
                topLeft = Offset(
                    x = width - padding - timeMeasured.size.width,
                    y = height - padding + 8
                ),
                style = TextStyle(
                    fontSize = 10.sp,
                    color = axisColor.copy(alpha = 0.5f)
                )
            )
            
            // Distance label (Y-axis) - rotated would be ideal but keeping horizontal for simplicity
            val distLabel = "Distance"
            drawText(
                textMeasurer = textMeasurer,
                text = distLabel,
                topLeft = Offset(x = padding + 4, y = padding - 16),
                style = TextStyle(
                    fontSize = 10.sp,
                    color = axisColor.copy(alpha = 0.5f)
                )
            )
        }
        
        // Draw start/end markers
        drawCircle(
            color = curveColor,
            radius = 5f,
            center = Offset(padding, height - padding)
        )
        drawCircle(
            color = curveColor.copy(alpha = 0.4f),
            radius = 10f,
            center = Offset(padding, height - padding)
        )
        
        // End point
        val endY = height - padding - spline.flingDistanceCoefficient(1f) * graphHeight
        drawCircle(
            color = AuroraViolet,
            radius = 5f,
            center = Offset(width - padding, endY)
        )
        drawCircle(
            color = AuroraViolet.copy(alpha = 0.4f),
            radius = 10f,
            center = Offset(width - padding, endY)
        )
    }
}

/**
 * A simpler mini curve preview for preset cards with glow effect.
 */
@Composable
fun MiniCurvePreview(
    config: FlingConfiguration,
    modifier: Modifier = Modifier,
    curveColor: Color = AuroraCyan,
    animated: Boolean = false
) {
    val spline = remember(config) { AndroidFlingSpline(config) }
    
    val infiniteTransition = rememberInfiniteTransition(label = "mini_glow")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.7f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "mini_glow_pulse"
    )
    
    val actualGlowAlpha = if (animated) glowAlpha else 0.5f
    
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp)
    ) {
        val width = size.width
        val height = size.height
        val padding = 4f
        val graphWidth = width - padding * 2
        val graphHeight = height - padding * 2
        
        val path = Path()
        val samples = 50
        
        for (i in 0..samples) {
            val t = i.toFloat() / samples
            val distanceCoefficient = spline.flingDistanceCoefficient(t)
            val x = padding + t * graphWidth
            val y = height - padding - distanceCoefficient * graphHeight
            
            if (i == 0) {
                path.moveTo(x, y)
            } else {
                path.lineTo(x, y)
            }
        }
        
        // Glow effect
        drawPath(
            path = path,
            color = curveColor.copy(alpha = actualGlowAlpha * 0.4f),
            style = Stroke(width = 8f, cap = StrokeCap.Round)
        )
        
        // Main curve
        drawPath(
            path = path,
            brush = Brush.horizontalGradient(
                colors = listOf(curveColor, AuroraViolet)
            ),
            style = Stroke(width = 2.5f, cap = StrokeCap.Round)
        )
        
        // Start dot
        drawCircle(
            color = curveColor,
            radius = 3f,
            center = Offset(padding, height - padding)
        )
        
        // End dot
        val endY = height - padding - spline.flingDistanceCoefficient(1f) * graphHeight
        drawCircle(
            color = AuroraViolet,
            radius = 3f,
            center = Offset(width - padding, endY)
        )
    }
}

/**
 * Compact curve indicator for very small spaces.
 */
@Composable
fun CurveIndicator(
    config: FlingConfiguration,
    modifier: Modifier = Modifier,
    color: Color = AuroraCyan
) {
    val spline = remember(config) { AndroidFlingSpline(config) }
    
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(24.dp)
    ) {
        val width = size.width
        val height = size.height
        val padding = 2f
        
        val path = Path()
        val samples = 20
        
        for (i in 0..samples) {
            val t = i.toFloat() / samples
            val distanceCoefficient = spline.flingDistanceCoefficient(t)
            val x = padding + t * (width - padding * 2)
            val y = height - padding - distanceCoefficient * (height - padding * 2)
            
            if (i == 0) {
                path.moveTo(x, y)
            } else {
                path.lineTo(x, y)
            }
        }
        
        drawPath(
            path = path,
            color = color,
            style = Stroke(width = 2f, cap = StrokeCap.Round)
        )
    }
}
