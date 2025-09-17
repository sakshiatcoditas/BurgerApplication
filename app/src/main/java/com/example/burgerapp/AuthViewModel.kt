package com.example.burgerapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _authMessage = MutableStateFlow("")
    val authMessage: StateFlow<String> get() = _authMessage

    // Public method to update messages
    fun setAuthMessage(message: String) {
        _authMessage.value = message
    }

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _authMessage.value = "Email and password cannot be empty"
            return
        }
        viewModelScope.launch {
            auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener { _authMessage.value = "Login successful" }
                .addOnFailureListener { _authMessage.value = it.message ?: "Login failed" }
        }
    }

    fun register(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _authMessage.value = "Email and password cannot be empty"
            return
        }
        viewModelScope.launch {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener { _authMessage.value = "Registration successful" }
                .addOnFailureListener { _authMessage.value = it.message ?: "Registration failed" }
        }
    }

    fun resetPassword(email: String) {
        if (email.isBlank()) {
            _authMessage.value = "Email cannot be empty"
            return
        }
        viewModelScope.launch {
            auth.sendPasswordResetEmail(email)
                .addOnSuccessListener { _authMessage.value = "Reset email sent" }
                .addOnFailureListener { _authMessage.value = it.message ?: "Failed to send reset email" }
        }
    }

    fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential)
            .addOnSuccessListener { _authMessage.value = "Google login successful" }
            .addOnFailureListener { _authMessage.value = it.message ?: "Google login failed" }
    }
}
