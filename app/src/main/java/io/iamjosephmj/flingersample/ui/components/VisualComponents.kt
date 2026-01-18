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
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.iamjosephmj.flingersample.ui.theme.AuroraCyan
import io.iamjosephmj.flingersample.ui.theme.AuroraMagenta
import io.iamjosephmj.flingersample.ui.theme.AuroraViolet
import io.iamjosephmj.flingersample.ui.theme.FlingerTheme
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

// ============================================================================
// Translucent Background - Animated gradient blobs for all screens
// ============================================================================

@Composable
fun TranslucentBackground(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "translucent_bg")

    val phase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(12000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bg_phase"
    )

    val phase2 by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(15000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bg_phase2"
    )

    val backgroundColor = MaterialTheme.colorScheme.background

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor)
            .drawBehind {
                // Large cyan glow - top left area
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            AuroraCyan.copy(alpha = 0.20f * (0.6f + phase * 0.4f)),
                            AuroraCyan.copy(alpha = 0.08f),
                            Color.Transparent
                        ),
                        center = Offset(
                            x = size.width * (0.15f + phase * 0.25f),
                            y = size.height * (0.2f + phase2 * 0.15f)
                        ),
                        radius = size.width * 0.6f
                    )
                )

                // Medium magenta glow - bottom right area
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            AuroraMagenta.copy(alpha = 0.18f * (0.6f + phase2 * 0.4f)),
                            AuroraMagenta.copy(alpha = 0.06f),
                            Color.Transparent
                        ),
                        center = Offset(
                            x = size.width * (0.85f - phase * 0.2f),
                            y = size.height * (0.75f - phase2 * 0.1f)
                        ),
                        radius = size.width * 0.55f
                    )
                )

                // Violet accent - center floating
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            AuroraViolet.copy(alpha = 0.15f * (0.5f + phase * 0.5f)),
                            AuroraViolet.copy(alpha = 0.04f),
                            Color.Transparent
                        ),
                        center = Offset(
                            x = size.width * (0.5f + phase2 * 0.15f - phase * 0.1f),
                            y = size.height * (0.45f + phase * 0.1f)
                        ),
                        radius = size.width * 0.4f
                    )
                )
            },
        content = content
    )
}

// Keep AuroraBackground as an alias for backwards compatibility
@Composable
fun AuroraBackground(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    TranslucentBackground(modifier = modifier, content = content)
}

// ============================================================================
// Glowing Icon - Simple icon with accent color (no blur artifacts)
// ============================================================================

@Composable
fun GlowingIcon(
    imageVector: ImageVector,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    tint: Color = MaterialTheme.colorScheme.primary,
    glowColor: Color = tint.copy(alpha = 0.6f),
    glowRadius: Dp = 12.dp,
    iconSize: Dp = 48.dp
) {
    Box(
        modifier = modifier.size(iconSize),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = contentDescription,
            modifier = Modifier.size(iconSize),
            tint = tint
        )
    }
}

// ============================================================================
// Preset Gradient Colors
// ============================================================================

object GradientPresets {
    val Cyan = listOf(Color(0xFF00E5CC), Color(0xFF00B3A0))
    val Magenta = listOf(Color(0xFFFF3CAC), Color(0xFFD61A8A))
    val Violet = listOf(Color(0xFF785EF0), Color(0xFF5A3FD6))
    val CyanToMagenta = listOf(Color(0xFF00E5CC), Color(0xFF785EF0), Color(0xFFFF3CAC))
    val CyanToViolet = listOf(Color(0xFF00E5CC), Color(0xFF785EF0))
    val VioletToMagenta = listOf(Color(0xFF785EF0), Color(0xFFFF3CAC))
    val Sunset = listOf(Color(0xFFFF6B6B), Color(0xFFFF3CAC))
    val Ocean = listOf(Color(0xFF00B4DB), Color(0xFF0083B0))
    val Forest = listOf(Color(0xFF11998E), Color(0xFF38EF7D))
    val Fire = listOf(Color(0xFFFF512F), Color(0xFFDD2476))
    val Night = listOf(Color(0xFF0F2027), Color(0xFF203A43), Color(0xFF2C5364))

    fun forIndex(index: Int): List<Color> {
        val presets = listOf(
            CyanToMagenta,
            CyanToViolet,
            VioletToMagenta,
            Cyan,
            Magenta,
            Violet,
            Ocean,
            Forest,
            Fire,
            Sunset
        )
        return presets[index % presets.size]
    }
}

// ============================================================================
// Surface with Glow Border
// ============================================================================

@Composable
fun GlowBorderSurface(
    modifier: Modifier = Modifier,
    glowColor: Color = AuroraCyan,
    glowIntensity: Float = 0.5f,
    shape: Shape = RoundedCornerShape(16.dp),
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .clip(shape)
            .background(MaterialTheme.colorScheme.surface)
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        glowColor.copy(alpha = glowIntensity),
                        glowColor.copy(alpha = glowIntensity * 0.3f),
                        glowColor.copy(alpha = glowIntensity)
                    )
                ),
                shape = shape
            ),
        content = content
    )
}

// ============================================================================
// Accent Dot
// ============================================================================

@Composable
fun AccentDot(
    modifier: Modifier = Modifier,
    color: Color = AuroraCyan,
    size: Dp = 8.dp,
    animated: Boolean = true
) {
    val infiniteTransition = rememberInfiniteTransition(label = "accent_dot")

    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        ),
        label = "dot_alpha"
    )

    val actualAlpha = if (animated) alpha else 1f

    Box(
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(50))
            .background(color.copy(alpha = actualAlpha))
            .drawBehind {
                drawCircle(
                    color = color.copy(alpha = actualAlpha * 0.3f),
                    radius = this.size.minDimension
                )
            }
    )
}
