package com.example.burgerapp.navigation

import SuccessScreen
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
import com.example.burgerapp.ui.presentation.orderhistory_screen.OrderHistoryScreen
import com.example.burgerapp.ui.presentation.chat_screen.ChatScreen
import com.example.burgerapp.ui.presentation.custom_screen.CustomScreen
import com.example.burgerapp.ui.presentation.detail_screen.BurgerDetailScreen
import com.example.burgerapp.ui.presentation.forgotpassword_screen.ForgotPasswordScreen
import com.example.burgerapp.ui.presentation.home_screen.HomeScreen
import com.example.burgerapp.ui.presentation.login_screen.LoginScreen
import com.example.burgerapp.ui.presentation.payment_screen.PaymentScreen
import com.example.burgerapp.ui.presentation.profile_screen.ProfileScreen
import com.example.burgerapp.ui.presentation.register_screen.RegisterScreen
import com.example.burgerapp.ui.presentation.splash_screen.SplashScreen
import com.example.burgerapp.viewmodel.AuthViewModel
import com.example.burgerapp.viewmodel.ChatViewModel
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

        //  Splash Screen -
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
                onRegisterClick = { name, email, password -> authViewModel.register(name, email, password) },
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
            HomeScreen(navController = navController)
        }


        composable(Screen.Chat.route) {
            val viewModel: ChatViewModel = hiltViewModel()
            ChatScreen(
                viewModel = viewModel,
                onBackClick = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }



        // --- Profile Screen ---
        composable(Screen.Profile.route) {

            val authViewModel: AuthViewModel = hiltViewModel()

            ProfileScreen(
                navController = navController,
                viewModel = authViewModel,
                onLogoutClick = {
                    authViewModel.logout {
                        // Navigate to Login screen and clear backstack
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.Home.route) { inclusive = true }
                        }
                    }
                },
                onBackClick = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Profile.route) { inclusive = true }
                    }
                }
            )
        }



        composable(
            route = Screen.BurgerDetail.route,
            arguments = listOf(navArgument("burgerId") { type = NavType.StringType })
        ) { backStackEntry ->
            val detailViewModel: DetailViewModel = hiltViewModel()
            val burgerId = backStackEntry.arguments?.getString("burgerId") ?: ""

            LaunchedEffect(burgerId) { detailViewModel.loadBurger(burgerId) }

            val burgerState by detailViewModel.burger.collectAsState()
            burgerState?.let { burger ->
                BurgerDetailScreen(
                    burger = burger,
                    navController = navController,
                    onBackClick = { navController.popBackStack() }
                )
            }
        }

        // --- Custom Screen ---
        composable(
            route = Screen.CustomScreen.route,
            arguments = listOf(
                navArgument("burgerId") { type = NavType.StringType },
                navArgument("portion") { type = NavType.IntType },
                navArgument("spiceLevel") { type = NavType.FloatType }
            )
        ) { backStackEntry ->
            val detailViewModel: DetailViewModel = hiltViewModel()
            val burgerId = backStackEntry.arguments?.getString("burgerId") ?: ""
            val portion = backStackEntry.arguments?.getInt("portion") ?: 1
            val spiceLevel = backStackEntry.arguments?.getFloat("spiceLevel") ?: 0.5f

            LaunchedEffect(burgerId) { detailViewModel.loadBurger(burgerId) }

            val burgerState by detailViewModel.burger.collectAsState()
            burgerState?.let { burger ->
                CustomScreen(
                    burger = burger,
                    navController = navController,
                    initialPortion = portion,
                    initialSpiceLevel = spiceLevel,
                    onBackClick = { navController.popBackStack() }
                )
            }
        }

        // --- Payment Screen ---
        composable(
            route = Screen.PaymentScreen.route,
            arguments = listOf(
                navArgument("burgerId") { type = NavType.StringType },
                navArgument("portion") { type = NavType.IntType },
                navArgument("spiceLevel") { type = NavType.FloatType },
                navArgument("totalPrice") { type = NavType.FloatType },
                navArgument("burgerName") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val burgerId = backStackEntry.arguments?.getString("burgerId") ?: ""
            val portion = backStackEntry.arguments?.getInt("portion") ?: 1
            val spiceLevel = backStackEntry.arguments?.getFloat("spiceLevel") ?: 0.5f
            val totalPrice = backStackEntry.arguments?.getFloat("totalPrice") ?: 0f
            val burgerName = backStackEntry.arguments?.getString("burgerName") ?: "Custom Burger"

            PaymentScreen(
                burgerId = burgerId,
                burgerName = burgerName,
                portion = portion,
                spiceLevel = spiceLevel,
                totalPrice = totalPrice,
                onBackClick = { navController.popBackStack() },
                onPaymentSuccess = {
                    navController.navigate(
                        Screen.Success.route
                    ) {
                        popUpTo(
                            Screen.PaymentScreen.createRoute(
                                burgerId,
                                portion,
                                spiceLevel,
                                totalPrice,
                                burgerName
                            )
                        ) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Success.route) {
            SuccessScreen(
                onGoBack = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo("successScreen") { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.OrderHistory.route) {
            val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
            OrderHistoryScreen(
                userId = userId,
                onBackClick = { navController.popBackStack() }
            )
        }

    }
}