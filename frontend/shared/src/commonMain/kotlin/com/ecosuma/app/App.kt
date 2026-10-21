package com.ecosuma.app

import androidx.compose.runtime.Composable
import com.ecosuma.app.composeApp.navigation.AppNavigation
import com.ecosuma.app.composeApp.theme.EcoSumaTheme

/**
 * Punto de entrada compartido de la interfaz.
 * Lo llaman MainActivity (Android), MainViewController (iOS) y main.kt (escritorio).
 */
@Composable
fun App() {
    EcoSumaTheme {
        AppNavigation()
    }
}