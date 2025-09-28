package com.example.burgerapp.ui.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.burgerapp.R
import com.example.burgerapp.ui.theme.LobsterFont
import kotlinx.coroutines.delay
import com.google.firebase.auth.FirebaseAuth

@Composable
fun SplashScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateToHome: () -> Unit
) {
    LaunchedEffect(Unit) {
        delay(3000) // 3 seconds
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            onNavigateToHome()
        } else {
            onNavigateToLogin()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
    ) {
        // Centered title
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(id = R.string.app_name),
                fontFamily = LobsterFont,
                fontSize = 60.sp,
                color = Color.White
            )
        }

        // Loader2: extreme bottom-left
        Image(
            painter = painterResource(id = R.drawable.splash_image_left2),
            contentDescription = "Loader 2",
            modifier = Modifier
                .align(Alignment.BottomStart)
                .offset(x = (-42).dp)
                .size(width = 246.dp, height = 288.dp)
        )

        // Loader1: also sticks to bottom, overlapping Loader2
        Image(
            painter = painterResource(id = R.drawable.splash_image_right1),
            contentDescription = "Loader 1",
            modifier = Modifier
                .align(Alignment.BottomStart)
                .offset(x = 134.dp)
                .size(width = 202.dp, height = 202.dp)
                .zIndex(1f)
        )
    }
}
