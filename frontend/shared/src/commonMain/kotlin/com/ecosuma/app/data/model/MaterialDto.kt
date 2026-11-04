package com.ecosuma.app.data.model

import kotlinx.serialization.Serializable

/**
 * Modelo de datos recibido desde el backend
 * de EcoSuma para los materiales reciclables.
 */
@Serializable
data class MaterialDto(
    val id: Int,
    val nombre: String,
    val categoria: String,
    val puntosPorKg: Int,
    val activo: Boolean = true
)