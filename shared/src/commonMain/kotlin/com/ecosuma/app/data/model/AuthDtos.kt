package com.ecosuma.app.data.model

import kotlinx.serialization.Serializable

/** Lo que la app manda al backend al iniciar sesión. */
@Serializable
data class LoginRequestDto(val username: String, val password: String)

/** Usuario como lo manda el backend (el rol llega como texto). */
@Serializable
data class UserDto(
    val id: Int,
    val username: String,
    val fullName: String,
    val email: String,
    val role: String, // "CIUDADANO", "ADMINISTRADOR" o "ACOPIO"
    val colonia: String? = null,
    val puntosDisponibles: Int = 0,
    val puntosAcumulados: Int = 0,
    val kgReciclados: Double = 0.0,
    val entregas: Int = 0,
    val beneficiosUsados: Int = 0,
    val centro: String? = null
)

/** Respuesta completa del backend al login. */
@Serializable
data class LoginResponseDto(
    val success: Boolean,
    val message: String,
    val token: String? = null,
    val user: UserDto? = null
)