package com.example.burgerapp

import android.content.Context
import android.content.Intent
import com.example.burgerapp.viewmodel.AuthViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

class GoogleSignInManager(
    private val context: Context,
    private val authViewModel: AuthViewModel
) {

    private val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(context.getString(R.string.default_web_client_id))
        .requestEmail()
        .build()

    val googleSignInClient = GoogleSignIn.getClient(context, gso)

    fun handleSignInResult(data: Intent?) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        try {
            val account = task.getResult(Exception::class.java)
            account?.let { authViewModel.firebaseAuthWithGoogle(it) }
        } catch (e: Exception) {
            authViewModel.setAuthMessage(
                e.message ?: context.getString(R.string.google_login_failed)
            )
        }
    }
}