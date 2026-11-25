package com.ecosuma.app

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import kotlinx.browser.document

@OptIn(ExperimentalComposeUiApi::class)
fun main() {

    // Conectar la geolocalización del navegador.

    UbicacionBridge.solicitarUbicacionConError =
        { onResultado, onError ->

            UbicacionWeb.solicitar(
                onResultado = onResultado,
                onError = onError
            )
        }

    // Conectar el mapa de OpenStreetMap
    // con la interfaz compartida.

    MapaBridge.mostrarMapa = {
        MapaWeb()
    }

    // Iniciar la aplicación web.

    ComposeViewport(document.body!!) {
        App()
    }
}