package com.ecosuma.app.di

import com.ecosuma.app.data.remote.AuthApi
import com.ecosuma.app.data.remote.HttpClientFactory
import com.ecosuma.app.data.repository.AuthRepositoryImpl
import com.ecosuma.app.domain.repository.AuthRepository
import com.ecosuma.app.domain.usecase.LoginUseCase

/**
 * Inyección de dependencias manual: crea cada pieza una sola vez
 * y la entrega a quien la necesite.
 */
object AppContainer {
    private val httpClient by lazy { HttpClientFactory.create() }
    private val authApi by lazy { AuthApi(httpClient) }
    val authRepository: AuthRepository by lazy { AuthRepositoryImpl(authApi) }
    val loginUseCase by lazy { LoginUseCase(authRepository) }
}