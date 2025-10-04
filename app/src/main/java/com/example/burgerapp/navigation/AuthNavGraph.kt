package com.example.burgerapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.burgerapp.AuthState
import com.example.burgerapp.ui.ui.*
import com.example.burgerapp.viewmodel.AuthViewModel
import com.example.burgerapp.viewmodel.DetailViewModel
import com.example.burgerapp.viewmodel.HomeViewModel
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth

@Composable
fun AuthNavGraph(
    navController: NavHostController,
    onGoogleLoginClick: () -> Unit,
    onGoogleRegisterClick: () -> Unit,
    googleSignInClient: GoogleSignInClient
) {
    NavHost(navController = navController, startDestination = Screen.Splash.route) {

        // --- Splash Screen ---
        composable(Screen.Splash.route) {
            SplashScreen(
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                },
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        // --- Login Screen ---
        composable(Screen.Login.route) {
            val authViewModel: AuthViewModel = hiltViewModel()
            val authState = authViewModel.authState.collectAsState().value

            LaunchedEffect(authState) {
                if (authState is AuthState.Success) {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            }

            LoginScreen(
                onLoginClick = { email, password -> authViewModel.login(email, password) },
                onGoogleLoginClick = onGoogleLoginClick,
                onNavigateToRegister = { navController.navigate(Screen.Register.route) },
                onNavigateToForgotPassword = { navController.navigate(Screen.ForgotPassword.route) },
                authState = authState
            )
        }

        // --- Register Screen ---
        composable(Screen.Register.route) {
            val authViewModel: AuthViewModel = hiltViewModel()
            val authState = authViewModel.authState.collectAsState().value

            LaunchedEffect(authState) {
                if (authState is AuthState.Success) {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                }
            }

            RegisterScreen(
                onRegisterClick = { email, password -> authViewModel.register(email, password) },
                onGoogleRegisterClick = onGoogleRegisterClick,
                onNavigateToLogin = { navController.popBackStack() },
                authState = authState
            )
        }

        // --- Forgot Password Screen ---
        composable(Screen.ForgotPassword.route) {
            val authViewModel: AuthViewModel = hiltViewModel()
            val authState = authViewModel.authState.collectAsState().value

            ForgotPasswordScreen(
                onSendResetClick = { email -> authViewModel.resetPassword(email) },
                onBackToLoginClick = { navController.popBackStack() },
                authState = authState
            )
        }

        // --- Home Screen ---
        composable(Screen.Home.route) {
            val homeViewModel: HomeViewModel = hiltViewModel()
            val firebaseUser = FirebaseAuth.getInstance().currentUser


            HomeScreen(
                navController = navController,
                homeViewModel = homeViewModel
            )
        }

        // --- Profile Screen ---
        composable("Profile") {
            val authViewModel: AuthViewModel = hiltViewModel()
            ProfileScreen(
                googleSignInClient = googleSignInClient,
                onLogoutClick = {
                    authViewModel.resetAuthState()
                    googleSignInClient.signOut().addOnCompleteListener {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.Home.route) { inclusive = true }
                        }
                    }
                }
            )
        }

        // In your AuthNavGraph or a separate AppNavGraph
        composable(
            route = "burgerDetail/{burgerId}",
            arguments = listOf(navArgument("burgerId") { type = NavType.StringType })
        ) { backStackEntry ->
            val detailViewModel: DetailViewModel = hiltViewModel()
            val burgerId = backStackEntry.arguments?.getString("burgerId") ?: ""

            // Load burger when this screen appears
            LaunchedEffect(burgerId) {
                detailViewModel.loadBurger(burgerId)
            }

            // Collect the burger StateFlow
            val burgerState by detailViewModel.burger.collectAsState()

            // Show the screen only if burger is not null
            burgerState?.let { burger ->
                BurgerDetailScreen(
                    burger = burger,
                    onBackClick = { navController.popBackStack() },
                    onOrderClick = { portion ->
                        // Handle order logic
                    }
                )
            }
        }


    }
}
