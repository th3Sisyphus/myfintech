package com.example.myfintech.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myfintech.common.Resource
import com.example.myfintech.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(AuthState())
    val state = _state.asStateFlow()

    fun onEvent(event: AuthEvent) {
        when(event) {
            is AuthEvent.NameChanged -> {
                _state.update { it.copy(name = event.name, error = null) }
            }
            is AuthEvent.EmailChanged -> {
                _state.update { it.copy(email = event.email, error = null) }
            }
            is AuthEvent.PasswordChanged -> {
                _state.update { it.copy(password = event.password, error = null) }
            }
            is AuthEvent.RepeatedPasswordChanged -> {
                _state.update { it.copy(repeatedPassword = event.value, error = null) }
            }
            is AuthEvent.LoginClicked -> {
                login()
            }
            is AuthEvent.RegisterClicked -> {
                register()
            }
            is AuthEvent.GoogleSignInClicked -> {
                signInWithGoogle()
            }
            is AuthEvent.ErrorDismissed -> {
                _state.update { it.copy(error = null) }
            }
        }
    }

    private fun login() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val result = authRepository.login(_state.value.email, _state.value.password)
            when(result) {
                is Resource.Success -> {
                    _state.update { it.copy(isLoading = false, isAuthenticated = true) }
                }
                is Resource.Error -> {
                    _state.update { it.copy(isLoading = false, error = result.message ?: "Unknown Error") }
                }
                is Resource.Loading -> {
                    _state.update { it.copy(isLoading = true) }
                }
            }
        }
    }

    private fun register() {
        viewModelScope.launch {
            val name = _state.value.name
            val email = _state.value.email
            val password = _state.value.password
            val repeated = _state.value.repeatedPassword

            if (password != repeated) {
                _state.update { it.copy(error = "Passwords do not match") }
                return@launch
            }

            _state.update { it.copy(isLoading = true) }
            // Panggil Repository Register
            val result = authRepository.register(name, email, password)
            when(result) {
                is Resource.Success -> {
                    _state.update { it.copy(isLoading = false, isAuthenticated = true) }
                }
                is Resource.Error -> {
                    _state.update { it.copy(isLoading = false, error = result.message ?: "Unknown Error") }
                }
                is Resource.Loading -> {
                    _state.update { it.copy(isLoading = true) }
                }
            }
        }
    }

    private fun signInWithGoogle() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            // Logika Google Sign In di repo (biasanya memanggil Firebase)
            val result = authRepository.signInWithGoogle()
            when(result) {
                is Resource.Success -> {
                    _state.update { it.copy(isLoading = false, isAuthenticated = true) }
                }
                is Resource.Error -> {
                    _state.update { it.copy(isLoading = false, error = result.message ?: "Unknown Error") }
                }
                is Resource.Loading -> {
                    _state.update { it.copy(isLoading = true) }
                }
            }
        }
    }

    private fun processAuthResult(success: Boolean) {
        if (success) {
            _state.update { it.copy(isLoading = false, isAuthenticated = true) }
        } else {
            _state.update { it.copy(isLoading = false, error = "Authentication failed") }
        }
    }
}