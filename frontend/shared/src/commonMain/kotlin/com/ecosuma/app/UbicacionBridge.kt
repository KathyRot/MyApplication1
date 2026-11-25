package com.ecosuma.app

object UbicacionBridge {

    var solicitarUbicacion:
            (((Double, Double) -> Unit) -> Unit)? = null

    var solicitarUbicacionConError:
            (((Double, Double) -> Unit, (String) -> Unit) -> Unit)? = null

    fun obtenerUbicacion(
        onUbicacionObtenida: (Double, Double) -> Unit
    ) {
        solicitarUbicacion?.invoke(onUbicacionObtenida)
    }

    fun obtenerUbicacion(
        onUbicacionObtenida: (Double, Double) -> Unit,
        onError: (String) -> Unit
    ) {
        val servicio = solicitarUbicacionConError

        if (servicio != null) {
            servicio(onUbicacionObtenida, onError)
        } else {
            val servicioAnterior = solicitarUbicacion

            if (servicioAnterior != null) {
                servicioAnterior(onUbicacionObtenida)
            } else {
                onError(
                    "La ubicación no está disponible en este dispositivo."
                )
            }
        }
    }
}