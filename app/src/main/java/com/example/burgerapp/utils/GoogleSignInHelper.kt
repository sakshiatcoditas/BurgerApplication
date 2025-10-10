package com.example.burgerapp.utils

import android.content.Context
import android.content.Intent
import com.example.burgerapp.viewmodel.AuthViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

class GoogleSignInManager(
    context: Context,
    private val authViewModel: AuthViewModel
) {

    // TODO: dont keep token in string.
    // it should be in sharedPreference or in encrypted format.
    private val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(context.getString(com.example.burgerapp.R.string.default_web_client_id))
        .requestEmail()
        .build()

    val googleSignInClient = GoogleSignIn.getClient(context, gso)

    // TODO :: mmove this into the viewmodel with proper Result
    // we can have this in module no need to pass viewmodel here.
    fun handleSignInResult(data: Intent?) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        try {
            val account = task.getResult(com.google.android.gms.common.api.ApiException::class.java)
            if (account != null) {
                authViewModel.firebaseAuthWithGoogle(account)
            } else {
                authViewModel.setAuthStateError("Google sign-in failed: null account")
            }
        } catch (e: com.google.android.gms.common.api.ApiException) {
            authViewModel.setAuthStateError("Google sign-in failed: ${e.statusCode}")
        } catch (e: Exception) {
            authViewModel.setAuthStateError(e.message ?: "Google login failed")
        }
    }

}
