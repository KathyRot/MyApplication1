package com.ecosuma.app

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.ecosuma.app.composeApp.screens.LoginScreen
import com.ecosuma.app.composeApp.theme.EcoSumaTheme
import com.ecosuma.app.domain.model.User

/**
 * Versión temporal para la rama feature/frontend-login:
 * permite probar el login antes de que existan las vistas por rol.
 * El Integrante 3 la cambia por la versión final que usa AppNavigation.
 */
@Composable
fun App() {
    EcoSumaTheme {
        var user by remember { mutableStateOf<User?>(null) }
        val logged = user
        if (logged == null) {
            LoginScreen(onLoginSuccess = { user = it })
        } else {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    "Sesión iniciada: ${logged.fullName} (${logged.role.displayName})",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}