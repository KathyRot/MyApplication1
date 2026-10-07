package com.ecosuma.app.data.remote

import com.ecosuma.app.BASE_URL
import com.ecosuma.app.data.model.LoginRequestDto
import com.ecosuma.app.data.model.LoginResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

/** Llamadas al backend relacionadas con la sesión. */
class AuthApi(private val client: HttpClient) {

    /**
     * POST /api/auth/login
     * Aunque las credenciales estén mal (401), el backend responde un LoginResponseDto,
     * así que siempre se puede leer el mensaje.
     */
    suspend fun login(request: LoginRequestDto): LoginResponseDto =
        client.post("$BASE_URL/api/auth/login") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
}