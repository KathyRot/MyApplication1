package com.ecosuma.data

import com.ecosuma.models.UserRecord
import com.ecosuma.models.UsersFile
import kotlinx.serialization.json.Json

/**
 * Repositorio encargado de leer los usuarios de users.json
 * y validar sus credenciales.
 */
class UserRepository(
    private val resourcePath: String = "data/users.json"
) {

    private val json = Json {
        ignoreUnknownKeys = true
    }

    /** Lee el archivo JSON y devuelve una lista de usuarios. */
    private fun loadUsers(): List<UserRecord> {
        val text = UserRepository::class.java.classLoader
            .getResource(resourcePath)
            ?.readText()
            ?: error("No se encontró el archivo $resourcePath en resources")

        val usersFile: UsersFile = json.decodeFromString(text)

        return usersFile.users
    }

    /** Comprueba si el usuario existe y si su contraseña es correcta. */
    fun authenticate(username: String, password: String): UserRecord? {
        val users: List<UserRecord> = loadUsers()

        return users.firstOrNull { user ->
            user.username.equals(username.trim(), ignoreCase = true) &&
                    user.password == password
        }
    }

    /** Todos los usuarios, para la tarjeta de usuarios de prueba (GET /api/auth/demo-users). */
    fun allUsers(): List<UserRecord> = loadUsers()
}