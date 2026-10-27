package com.ecosuma

import com.ecosuma.utils.GeoUtils
import kotlin.test.Test
import kotlin.test.assertEquals

class GeoUtilsTest {

    @Test
    fun `test distancia entre coordenadas iguales`() {

        val distancia = GeoUtils.calcularDistancia(
            20.0836,
            -98.3629,
            20.0836,
            -98.3629
        )

        assertEquals(0.0, distancia, 0.001)
    }

    @Test
    fun `test distancia entre Tulancingo y Pachuca`() {

        val distancia = GeoUtils.calcularDistancia(
            20.0836,
            -98.3629,
            20.1011,
            -98.7591
        )

        assertEquals(41.42, distancia, 0.1)
    }
}