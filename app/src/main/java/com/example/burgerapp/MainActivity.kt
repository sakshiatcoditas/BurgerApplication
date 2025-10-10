package com.example.burgerapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.material3.Surface
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.navigation.compose.rememberNavController
import com.example.burgerapp.navigation.AuthNavGraph
import com.example.burgerapp.ui.theme.BurgerAppTheme
import com.example.burgerapp.utils.GoogleSignInManager
import com.example.burgerapp.viewmodel.AuthViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint



@AndroidEntryPoint
class MainActivity : ComponentActivity() {




    private val authViewModel: AuthViewModel by viewModels()
    private lateinit var googleSignInManager: GoogleSignInManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        googleSignInManager = GoogleSignInManager(this, authViewModel)

        val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            googleSignInManager.handleSignInResult(result.data)
        }

        setContent {

            val systemUiController = rememberSystemUiController()

            //  Hide system nav bar
            SideEffect {
                systemUiController.isNavigationBarVisible = false
            }

            BurgerAppTheme {
                val navController = rememberNavController()

                Surface(color = White) {
                    AuthNavGraph( // AppNavigation
                        navController = navController,
                        onGoogleLoginClick = { launcher.launch(googleSignInManager.googleSignInClient.signInIntent) },
                        onGoogleRegisterClick = { launcher.launch(googleSignInManager.googleSignInClient.signInIntent) },
                        googleSignInClient = googleSignInManager.googleSignInClient
                    )
                }
            }
        }
    }
}