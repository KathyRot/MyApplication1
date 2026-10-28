package com.ecosuma

import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.testApplication
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ServerTest {

    @Test
    fun `test root endpoint`() = testApplication {
        configure()

        val response = client.get("/")

        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun `test obtener centros de acopio`() = testApplication {
        configure()

        val response = client.get("/api/centros-acopio")

        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun `test obtener centros cercanos`() = testApplication {
        configure()

        val response = client.get(
            "/api/centros-acopio/cercanos?latitud=20.0836&longitud=-98.3629"
        )

        assertEquals(HttpStatusCode.OK, response.status)

        val contenido = response.bodyAsText()

        assertTrue(contenido.contains("distanciaKm"))
        assertTrue(contenido.contains("Centro de Acopio EcoSuma"))
    }

    @Test
    fun `test rechazar coordenadas invalidas`() = testApplication {
        configure()

        val response = client.get(
            "/api/centros-acopio/cercanos?latitud=200&longitud=-98.3629"
        )

        assertEquals(
            HttpStatusCode.BadRequest,
            response.status
        )

        val contenido = response.bodyAsText()

        assertTrue(
            contenido.contains("Debes proporcionar coordenadas válidas")
        )
    }
}