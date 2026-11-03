package com.ecosuma.models

import kotlinx.serialization.Serializable

@Serializable
data class ConfirmarRegistroRequest(
    val confirmadoPor: Int
)