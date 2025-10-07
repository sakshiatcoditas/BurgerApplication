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
import com.example.burgerapp.ui.presentation.*
import com.example.burgerapp.ui.presentation.chat_screen.ChatScreen
import com.example.burgerapp.ui.presentation.home_screen.HomeScreen
import com.example.burgerapp.viewmodel.AuthViewModel
import com.example.burgerapp.viewmodel.ChatViewModel
import com.example.burgerapp.viewmodel.DetailViewModel
import com.example.burgerapp.viewmodel.HomeViewModel
import com.google.android.gms.auth.api.signin.GoogleSignInClient

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

            HomeScreen(
                navController = navController,
                homeViewModel = homeViewModel
            )
        }

        composable("Chat") { backStackEntry ->

            val chatViewModel: ChatViewModel = hiltViewModel() // scoped to activity, not composable

            ChatScreen(navController = navController, viewModel = chatViewModel)
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
                    navController = navController, // pass NavController
                    onBackClick = { navController.popBackStack() }
                )
            }

        }


// --- Custom Screen ---
        composable(
            route = "customScreen/{burgerId}/{portion}/{spiceLevel}",
            arguments = listOf(
                navArgument("burgerId") { type = NavType.StringType },
                navArgument("portion") { type = NavType.IntType },
                navArgument("spiceLevel") { type = NavType.FloatType }
            )
        ) { backStackEntry ->
            val detailViewModel: DetailViewModel = hiltViewModel()
            val burgerId = backStackEntry.arguments?.getString("burgerId") ?: ""
            val portion = backStackEntry.arguments?.getInt("portion") ?: 1
            val spiceLevel = backStackEntry.arguments?.getFloat("spiceLevel") ?: 0.7f

            LaunchedEffect(burgerId) {
                detailViewModel.loadBurger(burgerId)
            }

            val burgerState by detailViewModel.burger.collectAsState()
            burgerState?.let { burger ->
                CustomScreen(
                    burger = burger,
                    initialPortion = portion,
                    initialSpiceLevel = spiceLevel,
                    onBackClick = { navController.popBackStack() }
                )
            }
        }



    }
}