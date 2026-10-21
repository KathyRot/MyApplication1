package com.ecosuma.app.data.session

import com.ecosuma.app.domain.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/** Guarda en memoria al usuario que inició sesión (control de sesión básico). */
object SessionManager {
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    fun start(user: User) { _currentUser.value = user }
    fun end() { _currentUser.value = null }
}