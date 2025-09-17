package com.example.burgerapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Surface
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.rememberNavController
import com.example.burgerapp.navigation.AuthNavGraph
import com.example.burgerapp.ui.theme.BurgerAppTheme
import com.example.burgerapp.viewmodel.AuthViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

class MainActivity : ComponentActivity() {

    private lateinit var authViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        authViewModel = AuthViewModel(application)

        // Configure Google Sign-In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(this, gso)

        // Launcher for Google Sign-In result
        val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(Exception::class.java)
                account?.let { authViewModel.firebaseAuthWithGoogle(it) }
            } catch (e: Exception) {
                authViewModel.setAuthMessage(e.message ?: "Google login failed")
            }
        }

        setContent {
            BurgerAppTheme {
                Surface(color = Color.White) {
                    val navController = rememberNavController()

                    AuthNavGraph(
                        navController = navController,
                        authViewModel = authViewModel,
                        onGoogleLoginClick = { launcher.launch(googleSignInClient.signInIntent) },
                        onGoogleRegisterClick = { launcher.launch(googleSignInClient.signInIntent) }
                    )
                }
            }
        }
    }
}
