package com.ecosuma.models

import kotlinx.serialization.Serializable

@Serializable
data class Material(
    val id: Int,
    val nombre: String,
    val categoria: String,
    val puntosPorKg: Int,
    val activo: Boolean = true
)