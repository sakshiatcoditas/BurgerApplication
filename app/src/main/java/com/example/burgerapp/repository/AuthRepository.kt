package com.example.burgerapp.repository

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

@Singleton
class AuthRepository @Inject constructor(
    private val auth: FirebaseAuth
) {

    // Login with email/password
    suspend fun login(email: String, password: String): AuthResult =
        auth.signInWithEmailAndPassword(email, password).await()

    // Register with email/password
    suspend fun register(email: String, password: String): AuthResult =
        auth.createUserWithEmailAndPassword(email, password).await()

    // Reset password
    suspend fun resetPassword(email: String): Void? =
        auth.sendPasswordResetEmail(email).await()

    // Firebase authentication with Google
    suspend fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        return suspendCoroutine { continuation ->
            auth.signInWithCredential(credential)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        continuation.resume(Unit) // now Success is emitted at the right time
                    } else {
                        continuation.resumeWithException(
                            task.exception ?: Exception("Google sign in failed")
                        )
                    }
                }
        }
    }

}