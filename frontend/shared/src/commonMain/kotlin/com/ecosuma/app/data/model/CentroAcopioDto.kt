
package com.ecosuma.app.data.model

import kotlinx.serialization.Serializable

/**
 * Modelo de datos para recibir centros de acopio
 * desde el backend de EcoSuma.
 */
@Serializable
data class CentroAcopioDto(
    val id: Int,
    val nombre: String,
    val direccion: String,
    val latitud: Double? = null,
    val longitud: Double? = null,
    val horario: String,
    val telefono: String,
    val activo: Boolean = true
)
