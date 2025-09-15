package com.example.burgerapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier

import com.example.burgerapp.ui.theme.BurgerAppTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BurgerAppTheme {
                // Show splash first
                SplashScreen()

                // Later: add navigation to Login/Register after delay
                LaunchedEffect(Unit) {
                    delay(3000) // 3 sec splash
                    // TODO: Navigate to Login/Register screen
                }
            }
        }
    }
}
