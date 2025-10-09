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
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

import com.google.firebase.database.ValueEventListener


@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository,
    auth: FirebaseAuth
) : ViewModel() {

    // --- StateFlows ---
    private val _emailUserName = MutableStateFlow<String?>(null)
    val emailUserName: StateFlow<String?> get() = _emailUserName

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> get() = _authState

    private val _deliveryAddress = MutableStateFlow("")
    val deliveryAddress: StateFlow<String> get() = _deliveryAddress

    private val _currentUser = MutableStateFlow(auth.currentUser)
    val currentUser: StateFlow<FirebaseUser?> get() = _currentUser

    init {
        // Auth state listener
        auth.addAuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            _currentUser.value = user

            user?.uid?.let { uid ->
                loadDeliveryAddress(uid)
            }

            _authState.value = if (user != null) {
                AuthState.Success("Logged in as ${user.email}")
            } else {
                AuthState.Error("No user detected")
            }
        }
    }

    // --- Login ---
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

    // --- Delivery Address ---
    private fun loadDeliveryAddress(uid: String) {
        val ref = Firebase.database.getReference("users/$uid/deliveryAddress")


        ref.get().addOnSuccessListener { snapshot ->
            _deliveryAddress.value = snapshot.getValue<String>() ?: ""
        }.addOnFailureListener {
            _deliveryAddress.value = ""
        }


        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                _deliveryAddress.value = snapshot.getValue<String>() ?: ""
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("BurgerApp", "Failed to read value.", error.toException())
            }
        })
    }

    fun updateDeliveryAddress(uid: String, newAddress: String) {
        _deliveryAddress.value = newAddress
        Firebase.database.getReference("users/$uid/deliveryAddress").setValue(newAddress)
    }

    // --- Registration ---
    fun register(name: String, email: String, password: String) {
        if (name.isBlank() || email.isBlank() || password.isBlank()) {
            _authState.value = AuthState.Error("Name, Email or Password cannot be empty")
            return
        }

        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val result = repository.register(email, password)
                val user = result.user

                user?.let {
                    // Save name-email in Realtime Database
                    repository.saveUserNameToDatabase(it.uid, name, email)

                    // Update Firebase displayName
                    it.updateProfile(userProfileChangeRequest { displayName = name }).addOnSuccessListener {
                        FirebaseAuth.getInstance().currentUser?.reload()?.addOnSuccessListener {
                            _currentUser.value = FirebaseAuth.getInstance().currentUser
                        }
                    }
                }

                _authState.value = AuthState.Success("Registration successful")
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Registration Failed")
            }
        }
    }

    // --- Reset Password ---
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

    // --- Google Auth ---
    fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                repository.firebaseAuthWithGoogle(account)
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: AuthMessages.GOOGLE_FAILED)
            }
        }
    }
}
