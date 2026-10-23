package com.ecosuma.models

import kotlinx.serialization.Serializable

@Serializable
data class CentroAcopio(
    val id: Int,
    val nombre: String,
    val direccion: String,
    val latitud: Double,
    val longitud: Double,
    val horario: String,
    val telefono: String,
    val activo: Boolean = true
)