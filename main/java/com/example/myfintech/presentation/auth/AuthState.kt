package com.example.myfintech.presentation.auth

data class AuthState(
    val name:String= "",
    val email: String = "",
    val password: String = "",
    val repeatedPassword: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isAuthenticated: Boolean = false
)