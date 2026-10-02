package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme =
  darkColorScheme(
    primary = TacticalGold,
    onPrimary = SurfaceDarkTactical,
    primaryContainer = ArmyGreenDark,
    onPrimaryContainer = TacticalGoldLight,
    secondary = ArmyGreenMedium,
    onSecondary = Color.White,
    secondaryContainer = CardDarkTactical,
    onSecondaryContainer = TacticalGoldLight,
    tertiary = CamoSlateLight,
    onTertiary = Color.White,
    background = SurfaceDarkTactical,
    onBackground = Color(0xFFE2E7E1),
    surface = CardDarkTactical,
    onSurface = Color(0xFFE2E7E1),
    surfaceVariant = Color(0xFF27332B),
    onSurfaceVariant = Color(0xFFBAC3BB)
  )

private val LightColorScheme =
  lightColorScheme(
    primary = ArmyGreenPrimary,
    onPrimary = Color.White,
    primaryContainer = ArmyGreenLight,
    onPrimaryContainer = ArmyGreenDark,
    secondary = TacticalGoldDark,
    onSecondary = Color.White,
    secondaryContainer = TacticalGoldLight,
    onSecondaryContainer = Color(0xFF2B1F00),
    tertiary = CamoSlate,
    onTertiary = Color.White,
    background = BackgroundLightTactical,
    onBackground = Color(0xFF131915),
    surface = CardLightTactical,
    onSurface = Color(0xFF131915),
    surfaceVariant = Color(0xFFE7ECE6),
    onSurfaceVariant = Color(0xFF434C45)
  )

@Composable
fun JungleBatchTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false, // Keep consistent military brand identity
  content: @Composable () -> Unit,
) {
  val colorScheme =
    when {
      dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
      }
      darkTheme -> DarkColorScheme
      else -> LightColorScheme
    }

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}

