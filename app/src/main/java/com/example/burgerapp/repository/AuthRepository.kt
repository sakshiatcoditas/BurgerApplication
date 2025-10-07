package com.example.burgerapp.repository

import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.FirebaseDatabase
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
        auth.createUserWithEmailAndPassword(email,password).await()

    suspend fun saveUserNameToDatabase(userId: String, name: String, email: String) {
        val dbRef = FirebaseDatabase.getInstance().reference.child("users").child(userId)
        dbRef.setValue(mapOf(
            "name" to name,
            "email" to email
        )).await()
    }




    // Reset password
    suspend fun resetPassword(email: String): Void? =
        auth.sendPasswordResetEmail(email).await()

    // Firebase authentication with Google
    suspend fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        return suspendCoroutine { continuation ->
            auth.signInWithCredential(credential)
                .addOnCompleteListener { task ->
                    Log.d("BurgerApp","Ra1 :: $task")
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