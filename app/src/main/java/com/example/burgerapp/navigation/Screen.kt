package com.example.burgerapp.navigation


// TODO
// explore enum and sealed class use case and limitations.


sealed class Screen(val route: String) {

    object Splash : Screen(Route.Splash.route)
    object Login : Screen(Route.Login.route)
    object Register : Screen(Route.Register.route)
    object ForgotPassword : Screen(Route.ForgotPassword.route)
    object Home : Screen(Route.Home.route)
    object Profile : Screen(Route.Profile.route)
    object Chat : Screen(Route.Chat.route)
    object Success : Screen(Route.Success.route)
    object OrderHistory : Screen(Route.OrderHistory.route)

    // Screens with arguments
    object BurgerDetail : Screen("burgerDetail/{burgerId}") {
        fun createRoute(burgerId: String) = "burgerDetail/$burgerId"
    }

    object CustomScreen : Screen("customScreen/{burgerId}/{portion}/{spiceLevel}") {
        fun createRoute(burgerId: String, portion: Int, spiceLevel: Float) =
            "customScreen/$burgerId/$portion/$spiceLevel"
    }

    object PaymentScreen : Screen("paymentScreen/{burgerId}/{portion}/{spiceLevel}/{totalPrice}/{burgerName}") {
        fun createRoute(
            burgerId: String,
            portion: Int,
            spiceLevel: Float,
            totalPrice: Float,
            burgerName: String
        ) = "paymentScreen/$burgerId/$portion/$spiceLevel/$totalPrice/$burgerName"
    }
}
