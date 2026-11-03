package com.ecosuma.models

import kotlinx.serialization.Serializable

@Serializable
data class PuntosUsuarioResponse(
    val usuarioId: Int,
    val puntosAcumulados: Long
)