package com.ecosuma.app.domain.model

/** Roles de EcoSuma, según la sección 19 de la propuesta. */
enum class Role(val displayName: String) {
    CIUDADANO("Ciudadano"),
    ADMINISTRADOR("Administrador"),
    ACOPIO("Personal de acopio");

    companion object {
        /** Convierte el texto que manda el backend ("CIUDADANO", etc.) en un Role. */
        fun fromValue(value: String): Role? =
            entries.firstOrNull { it.name.equals(value, ignoreCase = true) }
    }
}

/** Usuario que inició sesión, tal como lo usa la app. */
data class User(
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