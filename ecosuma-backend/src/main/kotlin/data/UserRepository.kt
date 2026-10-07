package com.ecosuma.data

import com.ecosuma.models.UserRecord
import com.ecosuma.models.UsersFile
import kotlinx.serialization.json.Json

/**
 * Lee las credenciales predefinidas de resources/data/users.json
 * y las convierte en objetos con kotlinx.serialization.
 */
class UserRepository(resourcePath: String = "data/users.json") {

    private val json = Json { ignoreUnknownKeys = true }

    /** Se carga una sola vez, cuando se crea el repositorio. */
    private val users: List<UserRecord> = run {
        val text = UserRepository::class.java.classLoader
            .getResource(resourcePath)
            ?.readText()
            ?: error("No se encontró el archivo $resourcePath en resources")
        json.decodeFromString<UsersFile>(text).users
    }

    /** Devuelve el usuario si el nombre y la contraseña coinciden; si no, null. */
    fun authenticate(username: String, password: String): UserRecord? =
        users.firstOrNull {
            it.username.equals(username.trim(), ignoreCase = true) && it.password == password
        }
}