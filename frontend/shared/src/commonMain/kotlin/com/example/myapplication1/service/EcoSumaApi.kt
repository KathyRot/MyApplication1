package com.example.myapplication1.service

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.call.body
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class EcoSumaApi {

    private val client = HttpClient {
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                }
            )
        }
    }

    suspend fun probarConexion(): String {
        return client
            .get("https://jsonplaceholder.typicode.com/posts/1")
            .body<String>()
    }

    fun getClient(): HttpClient {
        return client
    }
}