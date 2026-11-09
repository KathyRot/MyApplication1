
package com.ecosuma.app.data.model

import kotlinx.serialization.Serializable

/**
 * Centro de acopio cercano al ciudadano.
 * Incluye la distancia calculada por el backend.
 */
@Serializable
data class CentroCercanoDto(
    val id: Int,
    val nombre: String,
    val direccion: String,
    val latitud: Double,
    val longitud: Double,
    val horario: String,
    val telefono: String,
    val distanciaKm: Double
)
