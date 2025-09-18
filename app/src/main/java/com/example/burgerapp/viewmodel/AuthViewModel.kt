package com.example.burgerapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.burgerapp.utils.AuthMessages
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _authMessage = MutableStateFlow("")
    val authMessage: StateFlow<String> get() = _authMessage

    // Public method to update messages
    fun setAuthMessage(message: String) {
        _authMessage.value = message
    }

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _authMessage.value = AuthMessages.EMPTY_EMAIL_PASSWORD

            return
        }
        viewModelScope.launch {
            auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener { _authMessage.value = AuthMessages.LOGIN_SUCCESS }
                .addOnFailureListener { _authMessage.value = it.message ?: AuthMessages.LOGIN_FAILED }
        }
    }

    fun register(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _authMessage.value = AuthMessages.EMPTY_EMAIL_PASSWORD
            return
        }
        viewModelScope.launch {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener { _authMessage.value = AuthMessages.REGISTER_SUCCESS }
                .addOnFailureListener { _authMessage.value = it.message ?: AuthMessages.REGISTER_FAILED }
        }
    }

    fun resetPassword(email: String) {
        if (email.isBlank()) {
            _authMessage.value = AuthMessages.EMPTY_EMAIL_PASSWORD
            return
        }
        viewModelScope.launch {
            auth.sendPasswordResetEmail(email)
                .addOnSuccessListener { _authMessage.value = AuthMessages.RESET_SUCCESS }
                .addOnFailureListener { _authMessage.value = it.message ?: AuthMessages.RESET_FAILED}
        }
    }

    fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential)
            .addOnSuccessListener { _authMessage.value = AuthMessages.GOOGLE_SUCCESS }
            .addOnFailureListener { _authMessage.value = it.message ?: AuthMessages.GOOGLE_FAILED }
    }
}