package com.ecosuma.app.data.repository

import com.ecosuma.app.data.model.LoginRequestDto
import com.ecosuma.app.data.model.UserDto
import com.ecosuma.app.data.remote.AuthApi
import com.ecosuma.app.domain.model.Role
import com.ecosuma.app.domain.model.User
import com.ecosuma.app.domain.repository.AuthRepository
import kotlin.coroutines.cancellation.CancellationException

class AuthRepositoryImpl(private val api: AuthApi) : AuthRepository {

    override suspend fun login(username: String, password: String): Result<User> =
        try {
            val response = api.login(LoginRequestDto(username.trim(), password))
            val dto = response.user
            when {
                // Credenciales incorrectas: se muestra el mensaje del backend
                !response.success || dto == null -> Result.failure(Exception(response.message))
                // Login correcto: se convierte el DTO en el modelo del dominio
                else -> dto.toDomain()?.let { Result.success(it) }
                    ?: Result.failure(Exception("Rol no reconocido: ${dto.role}"))
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(Exception("No se pudo conectar con el servidor. Verifica que el backend esté encendido."))
        }

    /** Convierte el UserDto (datos de red) en User (modelo de la app). */
    private fun UserDto.toDomain(): User? {
        val role = Role.fromValue(role) ?: return null
        return User(
            id = id, username = username, fullName = fullName, email = email, role = role,
            colonia = colonia, puntosDisponibles = puntosDisponibles, puntosAcumulados = puntosAcumulados,
            kgReciclados = kgReciclados, entregas = entregas, beneficiosUsados = beneficiosUsados,
            centro = centro
        )
    }
}