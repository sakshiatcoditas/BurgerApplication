package com.example.burgerapp.navigation

sealed class Screen(val route:String){
    object Login: Screen(route = "login")
    object Register: Screen(route = "register")
    object ForgotPassword: Screen(route = "forgot_password")
}