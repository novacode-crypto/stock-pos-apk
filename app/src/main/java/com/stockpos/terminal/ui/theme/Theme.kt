package com.stockpos.terminal.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Brand colors (same as PWA)
val Blue = Color(0xFF2563EB)
val BlueDark = Color(0xFF1E40AF)
val BlueLight = Color(0xFF3B82F6)
val Green = Color(0xFF16A34A)
val GreenLight = Color(0xFFDCFCE7)
val Yellow = Color(0xFFEAB308)
val YellowLight = Color(0xFFFEF3C7)
val Red = Color(0xFFDC2626)
val RedLight = Color(0xFFFEE2E2)
val Purple = Color(0xFF7C3AED)
val Cyan = Color(0xFF0891B2)
val Surface = Color(0xFFFFFFFF)
val Background = Color(0xFFF0F4F8)
val OnSurface = Color(0xFF0F172A)
val OnSurfaceVariant = Color(0xFF475569)
val Outline = Color(0xFFCBD5E1)

private val LightColors = lightColorScheme(
    primary = Blue,
    onPrimary = Color.White,
    primaryContainer = BlueDark,
    secondary = Purple,
    background = Background,
    surface = Surface,
    onBackground = OnSurface,
    onSurface = OnSurface,
    onSurfaceVariant = OnSurfaceVariant,
    outline = Outline,
    error = Red,
    onError = Color.White,
)

private val DarkColors = darkColorScheme(
    primary = BlueLight,
    onPrimary = Color.White,
    primaryContainer = BlueDark,
    secondary = Purple,
    background = Color(0xFF0B1220),
    surface = Color(0xFF111B2E),
    onBackground = Color(0xFFF4F7FC),
    onSurface = Color(0xFFF4F7FC),
    onSurfaceVariant = Color(0xFFB8C4D6),
    outline = Color(0xFF263650),
    error = Color(0xFFF06470),
    onError = Color.White,
)

private val AppTypography = Typography(
    headlineMedium = TextStyle(fontSize = 28.sp, fontWeight = FontWeight.Bold, letterSpacing = (-0.5).sp),
    headlineSmall = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.SemiBold),
    titleLarge = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.SemiBold),
    titleMedium = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Medium),
    bodyLarge = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Normal),
    bodyMedium = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Normal),
    bodySmall = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Normal),
    labelLarge = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.SemiBold),
    labelMedium = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Medium),
)

@Composable
fun StockPOSTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColors else LightColors
    MaterialTheme(
        colorScheme = colors,
        typography = AppTypography,
        content = content
    )
}
