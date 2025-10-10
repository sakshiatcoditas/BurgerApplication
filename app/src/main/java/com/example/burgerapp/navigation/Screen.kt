package com.example.burgerapp.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Login : Screen("login")
    object Register : Screen("register")
    object ForgotPassword : Screen("forgot_password")
    object Home : Screen("home")

     object Profile: Screen("profile")


    object Chat : Screen("chat")
    object Success : Screen("successScreen")
    object OrderHistory : Screen("orderHistory")
}
