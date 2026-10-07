package com.ecosuma.app.composeApp.navigation

import com.ecosuma.app.domain.model.Role
import kotlinx.serialization.Serializable

/**
 * Rutas de la app (Navigation Compose con rutas type-safe).
 * Los datos del usuario no viajan en la ruta: se leen de SessionManager.
 */
sealed interface Route {
    @Serializable data object Login : Route
    @Serializable data object Ciudadano : Route
    @Serializable data object Administrador : Route
    @Serializable data object Acopio : Route
}

/** Aquí se decide a qué vista va cada rol después del login. */
fun Role.toRoute(): Route = when (this) {
    Role.CIUDADANO -> Route.Ciudadano
    Role.ADMINISTRADOR -> Route.Administrador
    Role.ACOPIO -> Route.Acopio
}