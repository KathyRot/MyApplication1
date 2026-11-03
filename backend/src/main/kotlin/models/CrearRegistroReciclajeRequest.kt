package com.ecosuma.models

import kotlinx.serialization.Serializable

@Serializable
data class CrearRegistroReciclajeRequest(
    val usuarioId: Int,
    val centroAcopioId: Int,
    val materialId: Int,
    val cantidadKg: Double
)