
package com.ecosuma.app.di

import com.ecosuma.app.data.remote.AuthApi
import com.ecosuma.app.data.remote.HttpClientFactory
import com.ecosuma.app.data.remote.MaterialApi
import com.ecosuma.app.data.remote.CentroAcopioApi
import com.ecosuma.app.data.repository.AuthRepositoryImpl
import com.ecosuma.app.data.repository.MaterialRepositoryImpl
import com.ecosuma.app.data.repository.CentroAcopioRepositoryImpl
import com.ecosuma.app.domain.repository.AuthRepository
import com.ecosuma.app.domain.usecase.LoginUseCase

/**
 * Contenedor de dependencias de EcoSuma.
 * Comparte un mismo cliente HTTP entre los módulos.
 */
object AppContainer {

    // Cliente HTTP compartido
    private val httpClient by lazy {
        HttpClientFactory.create()
    }

    // Autenticación
    private val authApi by lazy {
        AuthApi(httpClient)
    }

    val authRepository: AuthRepository by lazy {
        AuthRepositoryImpl(authApi)
    }

    val loginUseCase by lazy {
        LoginUseCase(authRepository)
    }

    // Materiales reciclables
    val materialApi by lazy {
        MaterialApi(httpClient)
    }

    val materialRepository by lazy {
        MaterialRepositoryImpl(materialApi)
    }

    // Centros de acopio
    val centroAcopioApi by lazy {
        CentroAcopioApi(httpClient)
    }

    val centroAcopioRepository by lazy {
        CentroAcopioRepositoryImpl(centroAcopioApi)
    }
}
