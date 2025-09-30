package com.example.burgerapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.burgerapp.AuthState
import com.example.burgerapp.repository.AuthRepository
import com.example.burgerapp.utils.AuthMessages

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository,
    auth: FirebaseAuth

) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> get() = _authState


    // --- NEW: Current user flow ---
    private val _currentUser = MutableStateFlow(auth.currentUser)
    val currentUser: StateFlow<FirebaseUser?> get() = _currentUser

    init {

        auth.addAuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            _currentUser.value = user

            if (user != null) {
                _authState.value = AuthState.Success("Logged in as ${user.email}")
            } else {
                _authState.value = AuthState.Error("No user detected")
            }


        }
    }

    //We will need to clean up the listener as Remove listener when ViewModel is cleared (to avoid leaks)

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _authState.value = AuthState.Error(AuthMessages.EMPTY_EMAIL_PASSWORD)
            return
        }

        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                repository.login(email, password)

            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: AuthMessages.LOGIN_FAILED)
            }
        }
    }

    fun register(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _authState.value = AuthState.Error(AuthMessages.EMPTY_EMAIL_PASSWORD)
            return
        }

        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                repository.register(email, password)

            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: AuthMessages.REGISTER_FAILED)
            }
        }
    }

    fun resetPassword(email: String) {
        if (email.isBlank()) {
            _authState.value = AuthState.Error(AuthMessages.EMPTY_EMAIL)
            return
        }

        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                repository.resetPassword(email)
                _authState.value = AuthState.Success(AuthMessages.RESET_SUCCESS)
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: AuthMessages.RESET_FAILED)
            }
        }
    }

    fun setAuthStateError(message: String) {
        _authState.value = AuthState.Error(message)
    }


    fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                repository.firebaseAuthWithGoogle(account) // suspending, waits until Firebase signs in
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: AuthMessages.GOOGLE_FAILED)
            }
        }
    }



    fun resetAuthState() {
        _authState.value = AuthState.Idle
    }
}