package com.ecosuma.app.composeApp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ecosuma.app.composeApp.screens.LoginScreen
import com.ecosuma.app.composeApp.screens.roles.AcopioScreen
import com.ecosuma.app.composeApp.screens.roles.AdministradorScreen
import com.ecosuma.app.composeApp.screens.roles.CiudadanoScreen
import com.ecosuma.app.data.session.SessionManager

@Composable
fun AppNavigation(navController: NavHostController = rememberNavController()) {

    // Usuario con sesión iniciada (lo guarda SessionManager al hacer login)
    val user by SessionManager.currentUser.collectAsState()

    // Cerrar sesión: borra la sesión y todo el historial, para que "atrás" no regrese
    val logout: () -> Unit = {
        SessionManager.end()
        navController.navigate(Route.Login) {
            popUpTo(0) { inclusive = true }
        }
    }

    NavHost(navController = navController, startDestination = Route.Login) {

        composable<Route.Login> {
            LoginScreen(
                onLoginSuccess = { logged ->
                    SessionManager.start(logged)
                    // Va a la vista del rol y quita el login del historial
                    navController.navigate(logged.role.toRoute()) {
                        popUpTo(Route.Login) { inclusive = true }
                    }
                }
            )
        }

        composable<Route.Ciudadano> { user?.let { CiudadanoScreen(it, logout) } }
        composable<Route.Administrador> { user?.let { AdministradorScreen(it, logout) } }
        composable<Route.Acopio> { user?.let { AcopioScreen(it, logout) } }
    }
}