package com.ecosuma.app.domain.usecase

import com.ecosuma.app.domain.model.User
import com.ecosuma.app.domain.repository.AuthRepository

/** Caso de uso: valida que los campos no estén vacíos y pide el login al repositorio. */
class LoginUseCase(private val repository: AuthRepository) {

    suspend operator fun invoke(username: String, password: String): Result<User> {
        if (username.isBlank()) return Result.failure(Exception("Escribe tu usuario"))
        if (password.isBlank()) return Result.failure(Exception("Escribe tu contraseña"))
        return repository.login(username, password)
    }
}