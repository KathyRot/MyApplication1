package com.ecosuma.models

import kotlinx.serialization.Serializable

/** Roles de EcoSuma, según la sección 19 de la propuesta. */
@Serializable
enum class Role { CIUDADANO, ADMINISTRADOR, ACOPIO }

/** Usuario tal como está guardado en users.json (incluye la contraseña). */
@Serializable
data class UserRecord(
    val id: Int,
    val username: String,
    val password: String,
    val fullName: String,
    val email: String,
    val role: Role,
    // Solo para ciudadanos
    val colonia: String? = null,
    val puntosDisponibles: Int = 0,
    val puntosAcumulados: Int = 0,
    val kgReciclados: Double = 0.0,
    val entregas: Int = 0,
    val beneficiosUsados: Int = 0,
    // Solo para personal de acopio
    val centro: String? = null
)

/** Estructura completa del archivo users.json. */
@Serializable
data class UsersFile(val users: List<UserRecord>)

/** Lo que manda la app al iniciar sesión. */
@Serializable
data class LoginRequest(val username: String, val password: String)

/** Usuario que se devuelve a la app (sin la contraseña). */
@Serializable
data class UserDto(
    val id: Int,
    val username: String,
    val fullName: String,
    val email: String,
    val role: Role,
    val colonia: String? = null,
    val puntosDisponibles: Int = 0,
    val puntosAcumulados: Int = 0,
    val kgReciclados: Double = 0.0,
    val entregas: Int = 0,
    val beneficiosUsados: Int = 0,
    val centro: String? = null
)

/** Respuesta del backend al login (correcto o incorrecto). */
@Serializable
data class LoginResponse(
    val success: Boolean,
    val message: String,
    val token: String? = null,
    val user: UserDto? = null
)

/** Convierte el registro del JSON en lo que se manda a la app, quitando la contraseña. */
fun UserRecord.toDto() = UserDto(
    id = id, username = username, fullName = fullName, email = email, role = role,
    colonia = colonia, puntosDisponibles = puntosDisponibles, puntosAcumulados = puntosAcumulados,
    kgReciclados = kgReciclados, entregas = entregas, beneficiosUsados = beneficiosUsados,
    centro = centro
)