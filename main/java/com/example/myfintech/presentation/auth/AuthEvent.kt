package com.example.myfintech.presentation.auth

sealed class AuthEvent {
    data class NameChanged(val name: String): AuthEvent()
    data class EmailChanged(val email: String): AuthEvent()
    data class PasswordChanged(val password: String): AuthEvent()
    data class RepeatedPasswordChanged(val value: String): AuthEvent()
    object LoginClicked: AuthEvent()
    object RegisterClicked: AuthEvent()
    object GoogleSignInClicked: AuthEvent() // Event untuk Google Login
    object ErrorDismissed: AuthEvent()
}