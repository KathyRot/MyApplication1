package com.ecosuma.models

import kotlinx.serialization.Serializable

@Serializable
data class RegistroReciclaje(
    val id: Int,
    val usuarioId: Int,
    val centroAcopioId: Int,
    val materialId: Int,
    val cantidadKg: Double,
    val puntosObtenidos: Int = 0,
    val fecha: String,
    val estado: String = "PENDIENTE",
    val registradoPor: Int,
    val confirmadoPor: Int? = null
)