package com.ecosuma.app.composeApp.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Forest = Color(0xFF1B5E3B)   // verde principal
val Leaf = Color(0xFF6FA83A)     // verde claro
val Points = Color(0xFFE0A100)   // color de los puntos
val Field = Color(0xFFEEF3EC)    // fondo
val Bark = Color(0xFF26332B)     // texto

/** Un color por rol, para que cada vista se reconozca de inmediato. */
object RoleColors {
    val Ciudadano = Forest
    val Administrador = Color(0xFF24476B)
    val Acopio = Color(0xFF7A5417)
}

private val EcoColors = lightColorScheme(
    primary = Forest,
    onPrimary = Color.White,
    secondary = Leaf,
    tertiary = Points,
    background = Field,
    surface = Color.White,
    onBackground = Bark,
    onSurface = Bark,
    error = Color(0xFFB3261E)
)

@Composable
fun EcoSumaTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = EcoColors, content = content)
}