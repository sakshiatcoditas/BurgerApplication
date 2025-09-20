package com.example.burgerapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.rememberNavController
import com.example.burgerapp.navigation.AuthNavGraph
import com.example.burgerapp.ui.theme.BurgerAppTheme
import com.example.burgerapp.viewmodel.AuthViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable

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
                // Use rememberSaveable to preserve state across configuration changes
                var showSplash by rememberSaveable { mutableStateOf(true) }

                LaunchedEffect(Unit) {
                    delay(3000) // Show splash for 3 seconds
                    showSplash = false
                }

                Surface(color = Color.White) {
                    if (showSplash) {
                        // Splash screen
                        SplashScreen()
                    } else {
                        // Login/Register screen
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
}
