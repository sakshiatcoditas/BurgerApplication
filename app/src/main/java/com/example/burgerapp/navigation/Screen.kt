package com.example.burgerapp.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Login : Screen("login")
    object Register : Screen("register")
    object ForgotPassword : Screen("forgot_password")
    object Home : Screen("home")

    object BurgerDetail : Screen("burgerDetail/{burgerId}") {
        fun createRoute(burgerId: String) = "burgerDetail/$burgerId"
    }

    object Custom : Screen("customScreen/{burgerId}/{portion}/{spiceLevel}") {
        fun createRoute(burgerId: String, portion: Int, spice: Float) =
            "customScreen/$burgerId/$portion/$spice"
    }

}
