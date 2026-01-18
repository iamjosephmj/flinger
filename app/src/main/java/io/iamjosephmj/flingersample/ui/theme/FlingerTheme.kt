/*
* MIT License
*
* Copyright (c) 2021 Joseph James
*/

package io.iamjosephmj.flingersample.ui.theme

import android.app.Activity
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import io.iamjosephmj.flingersample.R

// ============================================================================
// AURORA THEME - Dark-mode-first design with electric cyan and magenta accents
// ============================================================================

// Primary Colors - Electric Cyan
val AuroraCyan = Color(0xFF00E5CC)
val AuroraCyanLight = Color(0xFF5EFFF0)
val AuroraCyanDark = Color(0xFF00B3A0)

// Secondary Colors - Magenta/Pink
val AuroraMagenta = Color(0xFFFF3CAC)
val AuroraMagentaLight = Color(0xFFFF7ACE)
val AuroraMagentaDark = Color(0xFFD61A8A)

// Tertiary Colors - Violet
val AuroraViolet = Color(0xFF785EF0)
val AuroraVioletLight = Color(0xFFA18FF5)
val AuroraVioletDark = Color(0xFF5A3FD6)

// Background Colors - Deep Space
val AuroraBackground = Color(0xFF0D0D12)
val AuroraBackgroundLight = Color(0xFFFAFBFC) // Cleaner, slightly cool white

// Surface Colors - Elevated Darks
val AuroraSurface = Color(0xFF161621)
val AuroraSurfaceLight = Color(0xFFFFFFFF)
val AuroraSurfaceVariant = Color(0xFF1E1E2E)
val AuroraSurfaceVariantLight = Color(0xFFF0F2F5) // Softer gray with better contrast

// On Colors
val AuroraOnBackground = Color(0xFFE8E8F0)
val AuroraOnBackgroundLight = Color(0xFF1C1C24) // Darker text for better readability
val AuroraOnSurface = Color(0xFFE8E8F0)
val AuroraOnSurfaceLight = Color(0xFF1C1C24)
val AuroraOnSurfaceVariant = Color(0xFFA0A0B0)
val AuroraOnSurfaceVariantLight = Color(0xFF4A4A5A) // Darker for better contrast

// Outline Colors
val AuroraOutline = Color(0xFF2E2E40)
val AuroraOutlineLight = Color(0xFFE0E2E8) // Softer outline for light mode
val AuroraOutlineVariant = Color(0xFF3A3A50)

// Error Colors
val AuroraError = Color(0xFFFF6B6B)
val AuroraErrorLight = Color(0xFFB3261E)
val AuroraOnError = Color(0xFF1A0000)
val AuroraOnErrorLight = Color(0xFFFFFFFF)
val AuroraErrorContainer = Color(0xFF3D1A1A)
val AuroraErrorContainerLight = Color(0xFFFFDAD6)

// Special Gradient Colors (for use in components)
val AuroraGradientStart = Color(0xFF00E5CC)
val AuroraGradientMiddle = Color(0xFF785EF0)
val AuroraGradientEnd = Color(0xFFFF3CAC)

// Glow Colors
val AuroraGlowCyan = Color(0x4000E5CC)
val AuroraGlowMagenta = Color(0x40FF3CAC)
val AuroraGlowViolet = Color(0x40785EF0)

// ============================================================================
// Extended Color Scheme for custom properties
// ============================================================================

data class ExtendedColors(
    val glowCyan: Color,
    val glowMagenta: Color,
    val glowViolet: Color,
    val gradientStart: Color,
    val gradientMiddle: Color,
    val gradientEnd: Color,
    val cardGlow: Color,
    val accentGradientStart: Color,
    val accentGradientEnd: Color
)

val LocalExtendedColors = staticCompositionLocalOf {
    ExtendedColors(
        glowCyan = AuroraGlowCyan,
        glowMagenta = AuroraGlowMagenta,
        glowViolet = AuroraGlowViolet,
        gradientStart = AuroraGradientStart,
        gradientMiddle = AuroraGradientMiddle,
        gradientEnd = AuroraGradientEnd,
        cardGlow = AuroraGlowCyan,
        accentGradientStart = AuroraCyan,
        accentGradientEnd = AuroraMagenta
    )
}

private val DarkExtendedColors = ExtendedColors(
    glowCyan = AuroraGlowCyan,
    glowMagenta = AuroraGlowMagenta,
    glowViolet = AuroraGlowViolet,
    gradientStart = AuroraGradientStart,
    gradientMiddle = AuroraGradientMiddle,
    gradientEnd = AuroraGradientEnd,
    cardGlow = AuroraGlowCyan,
    accentGradientStart = AuroraCyan,
    accentGradientEnd = AuroraMagenta
)

private val LightExtendedColors = ExtendedColors(
    glowCyan = AuroraCyan.copy(alpha = 0.08f),
    glowMagenta = AuroraMagenta.copy(alpha = 0.08f),
    glowViolet = AuroraViolet.copy(alpha = 0.08f),
    gradientStart = AuroraCyan,  // Use brighter colors for light mode gradients
    gradientMiddle = AuroraViolet,
    gradientEnd = AuroraMagenta,
    cardGlow = AuroraCyan.copy(alpha = 0.05f),
    accentGradientStart = AuroraCyan,
    accentGradientEnd = AuroraMagenta
)

// ============================================================================
// Color Schemes
// ============================================================================

private val DarkColorScheme = darkColorScheme(
    primary = AuroraCyan,
    onPrimary = Color(0xFF003730),
    primaryContainer = AuroraCyanDark,
    onPrimaryContainer = Color(0xFFB0FFF0),
    secondary = AuroraMagenta,
    onSecondary = Color(0xFF3D0028),
    secondaryContainer = AuroraMagentaDark,
    onSecondaryContainer = Color(0xFFFFD9E8),
    tertiary = AuroraViolet,
    onTertiary = Color(0xFF1E0050),
    tertiaryContainer = AuroraVioletDark,
    onTertiaryContainer = Color(0xFFE0D6FF),
    background = AuroraBackground,
    onBackground = AuroraOnBackground,
    surface = AuroraSurface,
    onSurface = AuroraOnSurface,
    surfaceVariant = AuroraSurfaceVariant,
    onSurfaceVariant = AuroraOnSurfaceVariant,
    surfaceContainerHighest = Color(0xFF252535),
    surfaceContainerHigh = Color(0xFF20202E),
    surfaceContainer = Color(0xFF1A1A28),
    surfaceContainerLow = Color(0xFF14141F),
    surfaceContainerLowest = Color(0xFF0A0A0F),
    outline = AuroraOutline,
    outlineVariant = AuroraOutlineVariant,
    error = AuroraError,
    onError = AuroraOnError,
    errorContainer = AuroraErrorContainer,
    onErrorContainer = Color(0xFFFFB4AB),
    inverseSurface = AuroraOnBackground,
    inverseOnSurface = AuroraBackground,
    inversePrimary = AuroraCyanDark,
    scrim = Color(0xFF000000)
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF00897B), // Teal 600 - good contrast on white
    onPrimary = Color.White,
    primaryContainer = Color(0xFFB2DFDB), // Light teal container
    onPrimaryContainer = Color(0xFF00251A),
    secondary = Color(0xFFD81B60), // Pink 600 - vibrant but readable
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFF8BBD0), // Light pink container
    onSecondaryContainer = Color(0xFF3E001A),
    tertiary = Color(0xFF5E35B1), // Deep Purple 500
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFD1C4E9), // Light purple container
    onTertiaryContainer = Color(0xFF1E0050),
    background = AuroraBackgroundLight,
    onBackground = AuroraOnBackgroundLight,
    surface = AuroraSurfaceLight,
    onSurface = AuroraOnSurfaceLight,
    surfaceVariant = AuroraSurfaceVariantLight,
    onSurfaceVariant = AuroraOnSurfaceVariantLight,
    surfaceContainerHighest = Color(0xFFE4E6EB),
    surfaceContainerHigh = Color(0xFFECEEF2),
    surfaceContainer = Color(0xFFF2F4F7),
    surfaceContainerLow = Color(0xFFF7F8FA),
    surfaceContainerLowest = Color(0xFFFFFFFF),
    outline = AuroraOutlineLight,
    outlineVariant = Color(0xFFD8DAE0),
    error = AuroraErrorLight,
    onError = AuroraOnErrorLight,
    errorContainer = AuroraErrorContainerLight,
    onErrorContainer = Color(0xFF410002),
    inverseSurface = Color(0xFF2E3033),
    inverseOnSurface = Color(0xFFF0F1F4),
    inversePrimary = AuroraCyan,
    scrim = Color(0xFF000000)
)

// ============================================================================
// Typography - Using system fonts with custom weights for now
// (Custom fonts can be added later via res/font/)
// ============================================================================

private val AuroraTypography = Typography(
    // Display styles - Large, impactful text
    displayLarge = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 57.sp,
        lineHeight = 64.sp,
        letterSpacing = (-0.25).sp
    ),
    displayMedium = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 45.sp,
        lineHeight = 52.sp,
        letterSpacing = 0.sp
    ),
    displaySmall = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 36.sp,
        lineHeight = 44.sp,
        letterSpacing = 0.sp
    ),
    
    // Headline styles - Section headers
    headlineLarge = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = 0.sp
    ),
    headlineMedium = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 28.sp,
        lineHeight = 36.sp,
        letterSpacing = 0.sp
    ),
    headlineSmall = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.sp
    ),
    
    // Title styles - Card titles, list headers
    titleLarge = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    titleMedium = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp
    ),
    titleSmall = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ),
    
    // Body styles - Main content text
    bodyLarge = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    bodyMedium = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp
    ),
    bodySmall = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.4.sp
    ),
    
    // Label styles - Buttons, chips, small UI elements
    labelLarge = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ),
    labelMedium = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    ),
    labelSmall = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
)

// ============================================================================
// Shapes
// ============================================================================

private val AuroraShapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(24.dp),
    extraLarge = RoundedCornerShape(32.dp)
)

// ============================================================================
// Theme Composable
// ============================================================================

@Composable
fun FlingerTheme(
    content: @Composable () -> Unit
) {
    // Dark theme only - Aurora theme is designed for dark mode
    val colorScheme = DarkColorScheme
    val extendedColors = DarkExtendedColors
    
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            // Make status bar transparent for edge-to-edge
            window.statusBarColor = Color.Transparent.toArgb()
            window.navigationBarColor = Color.Transparent.toArgb()
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = false
                isAppearanceLightNavigationBars = false
            }
        }
    }

    CompositionLocalProvider(
        LocalExtendedColors provides extendedColors
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = AuroraTypography,
            shapes = AuroraShapes,
            content = content
        )
    }
}

// ============================================================================
// Extension for accessing extended colors
// ============================================================================

object FlingerTheme {
    val extendedColors: ExtendedColors
        @Composable
        get() = LocalExtendedColors.current
}
