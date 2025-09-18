package com.example.burgerapp.repository

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val auth: FirebaseAuth
) {

    // Login with email/password
    fun login(email: String, password: String) =
        auth.signInWithEmailAndPassword(email, password)

    // Register with email/password
    fun register(email: String, password: String) =
        auth.createUserWithEmailAndPassword(email, password)

    // Reset password
    fun resetPassword(email: String) =
        auth.sendPasswordResetEmail(email)

    // Firebase authentication with Google
    fun firebaseAuthWithGoogle(account: GoogleSignInAccount) =
        auth.signInWithCredential(GoogleAuthProvider.getCredential(account.idToken, null))
}