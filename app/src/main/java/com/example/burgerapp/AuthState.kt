package com.example.burgerapp
sealed class AuthState {
    object Idle : AuthState() // Default state
    object Loading : AuthState() // While waiting for Firebase response

    data class Success(val message: String) : AuthState() // Success state
    data class Error(val message: String) : AuthState() // Error state
}