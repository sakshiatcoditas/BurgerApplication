package com.example.burgerapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.burgerapp.ui.ui.ForgotPasswordScreen
import com.example.burgerapp.ui.ui.LoginScreen
import com.example.burgerapp.ui.ui.RegisterScreen
import com.example.burgerapp.viewmodel.AuthViewModel

@Composable
fun AuthNavGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    onGoogleLoginClick: () -> Unit,
    onGoogleRegisterClick: () -> Unit
) {
    val message by authViewModel.authMessage.collectAsState()

    NavHost(navController = navController, startDestination = Screen.Login.route) {

        // Login
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginClick = { email, password -> authViewModel.login(email, password) },
                onGoogleLoginClick = onGoogleLoginClick,
                onNavigateToRegister = { navController.navigate(Screen.Register.route) },
                onNavigateToForgotPassword = { navController.navigate(Screen.ForgotPassword.route) },
                message = message
            )
        }

        // Register
        composable(Screen.Register.route) {
            RegisterScreen(
                onRegisterClick = { email, password -> authViewModel.register(email, password) },
                onGoogleRegisterClick = onGoogleRegisterClick,
                onNavigateToLogin = { navController.popBackStack() },
                message = message
            )
        }

        // Forgot Password
        composable(Screen.ForgotPassword.route) {
            ForgotPasswordScreen(
                onSendResetClick = { email -> authViewModel.resetPassword(email) },
                onBackToLoginClick = { navController.popBackStack() },
                message = message
            )
        }
    }
}