
package com.ecosuma.app

object UbicacionBridge {

    var solicitarUbicacion:
            (((Double, Double) -> Unit) -> Unit)? = null

    fun obtenerUbicacion(
        onUbicacionObtenida: (Double, Double) -> Unit
    ) {
        solicitarUbicacion?.invoke(onUbicacionObtenida)
    }
}
