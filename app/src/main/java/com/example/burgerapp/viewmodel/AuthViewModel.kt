package com.example.burgerapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.burgerapp.repository.AuthRepository
import com.example.burgerapp.utils.AuthMessages
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _authMessage = MutableStateFlow("")
    val authMessage: StateFlow<String> get() = _authMessage

    fun setAuthMessage(message: String) {
        _authMessage.value = message
    }

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _authMessage.value = AuthMessages.EMPTY_EMAIL_PASSWORD
            return
        }

        repository.login(email, password)
            .addOnSuccessListener { _authMessage.value = AuthMessages.LOGIN_SUCCESS }
            .addOnFailureListener { _authMessage.value = it.message ?: AuthMessages.LOGIN_FAILED }
    }

    fun register(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _authMessage.value = AuthMessages.EMPTY_EMAIL_PASSWORD
            return
        }

        repository.register(email, password)
            .addOnSuccessListener { _authMessage.value = AuthMessages.REGISTER_SUCCESS }
            .addOnFailureListener { _authMessage.value = it.message ?: AuthMessages.REGISTER_FAILED }
    }

    fun resetPassword(email: String) {
        if (email.isBlank()) {
            _authMessage.value = AuthMessages.EMPTY_EMAIL
            return
        }

        repository.resetPassword(email)
            .addOnSuccessListener { _authMessage.value = AuthMessages.GOOGLE_SUCCESS }
            .addOnFailureListener { _authMessage.value = it.message ?: AuthMessages.GOOGLE_FAILED }
    }

    fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        repository.firebaseAuthWithGoogle(account)
            .addOnSuccessListener { _authMessage.value = AuthMessages.GOOGLE_SUCCESS }
            .addOnFailureListener { _authMessage.value = it.message ?: AuthMessages.GOOGLE_FAILED }
    }
}