package com.ecosuma.app

import androidx.compose.runtime.Composable

object MapaBridge {

    var mostrarMapa: (@Composable () -> Unit)? = null

    @Composable
    fun Mapa() {
        val contenido = mostrarMapa

        if (contenido != null) {
            contenido()
        }
    }
}
