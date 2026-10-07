package com.ecosuma.app.domain.repository

import com.ecosuma.app.domain.model.User

/**
 * Contrato del login: qué se puede hacer, sin decir cómo.
 * La implementación real (con Ktor) estará en la capa de datos.
 */
interface AuthRepository {
    suspend fun login(username: String, password: String): Result<User>
}