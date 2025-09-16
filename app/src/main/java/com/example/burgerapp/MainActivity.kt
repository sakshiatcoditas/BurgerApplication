package com.example.burgerapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent


import com.example.burgerapp.ui.theme.BurgerAppTheme


//sigin with google works with this code
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BurgerAppTheme {

            }
        }
    }
}

