/*
* MIT License
*
* Copyright (c) 2021 Joseph James
*/

package io.iamjosephmj.flingersample.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import io.iamjosephmj.flinger.configs.FlingConfiguration
import io.iamjosephmj.flinger.spline.AndroidFlingSpline

/**
 * A Canvas composable that visualizes the fling deceleration curve
 * based on the provided configuration.
 */
@Composable
fun FlingCurveCanvas(
    config: FlingConfiguration,
    modifier: Modifier = Modifier,
    curveColor: Color = MaterialTheme.colorScheme.primary,
    gridColor: Color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
    backgroundColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    axisColor: Color = MaterialTheme.colorScheme.onSurface
) {
    val spline = remember(config) { AndroidFlingSpline(config) }
    
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .padding(16.dp)
    ) {
        val width = size.width
        val height = size.height
        val padding = 20f
        val graphWidth = width - padding * 2
        val graphHeight = height - padding * 2
        
        // Draw grid lines
        val gridLines = 5
        for (i in 0..gridLines) {
            val y = padding + (graphHeight / gridLines) * i
            drawLine(
                color = gridColor,
                start = Offset(padding, y),
                end = Offset(width - padding, y),
                strokeWidth = 1f
            )
            
            val x = padding + (graphWidth / gridLines) * i
            drawLine(
                color = gridColor,
                start = Offset(x, padding),
                end = Offset(x, height - padding),
                strokeWidth = 1f
            )
        }
        
        // Draw position curve (distance over time)
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
        
        drawPath(
            path = positionPath,
            color = curveColor,
            style = Stroke(width = 3f)
        )
        
        // Draw velocity curve (lighter)
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
                val y = height - padding - normalizedVelocity * graphHeight * 0.5f
                
                if (i == 0) {
                    velocityPath.moveTo(x, y)
                } else {
                    velocityPath.lineTo(x, y)
                }
            }
            
            drawPath(
                path = velocityPath,
                color = curveColor.copy(alpha = 0.4f),
                style = Stroke(width = 2f)
            )
        }
        
        // Draw axes
        drawLine(
            color = axisColor,
            start = Offset(padding, height - padding),
            end = Offset(width - padding, height - padding),
            strokeWidth = 2f
        )
        drawLine(
            color = axisColor,
            start = Offset(padding, padding),
            end = Offset(padding, height - padding),
            strokeWidth = 2f
        )
    }
}

/**
 * A simpler mini curve preview for preset cards.
 */
@Composable
fun MiniCurvePreview(
    config: FlingConfiguration,
    modifier: Modifier = Modifier,
    curveColor: Color = MaterialTheme.colorScheme.primary
) {
    val spline = remember(config) { AndroidFlingSpline(config) }
    
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
        
        drawPath(
            path = path,
            color = curveColor,
            style = Stroke(width = 2f)
        )
    }
}
