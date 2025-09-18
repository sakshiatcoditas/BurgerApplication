package com.example.burgerapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.material3.Surface
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.rememberNavController
import com.example.burgerapp.navigation.AuthNavGraph
import com.example.burgerapp.ui.theme.BurgerAppTheme
import com.example.burgerapp.viewmodel.AuthViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val authViewModel: AuthViewModel by viewModels()
    private lateinit var googleSignInManager: GoogleSignInManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        googleSignInManager = GoogleSignInManager(this, authViewModel)

        val launcher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            googleSignInManager.handleSignInResult(result.data)
        }

        setContent {
            BurgerAppTheme {
                Surface(color = androidx.compose.ui.graphics.Color.White) {
                    val navController = rememberNavController()

                    AuthNavGraph(
                        navController = navController,
                        authViewModel = authViewModel,
                        onGoogleLoginClick = { launcher.launch(googleSignInManager.googleSignInClient.signInIntent) },
                        onGoogleRegisterClick = { launcher.launch(googleSignInManager.googleSignInClient.signInIntent) }
                    )
                }
            }
        }
    }
}
