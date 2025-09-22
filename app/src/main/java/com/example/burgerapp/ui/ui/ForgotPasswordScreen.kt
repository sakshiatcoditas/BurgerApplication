package com.example.burgerapp.ui.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.burgerapp.ui.theme.CherryRed
import kotlinx.coroutines.launch

@Composable
fun ForgotPasswordScreen(
    onSendResetClick: (String) -> Unit,
    onBackToLoginClick: () -> Unit,
    message: String = ""
) {
    var email by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(message) {
        if (message.isNotBlank()) {
            coroutineScope.launch { snackbarHostState.showSnackbar(message) }
        }
    }

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(CherryRed)
                .padding(24.dp)
                .padding(padding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Forgot Password", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.White)

            Spacer(Modifier.height(16.dp))

            Text(
                "Enter your registered email address. We'll send you a reset link.",
                fontSize = 16.sp,
                color = Color.White
            )

            Spacer(Modifier.height(32.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email", color = Color.White) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.White,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.White,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    cursorColor = Color.White
                )
            )

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = { onSendResetClick(email) },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = CherryRed)
            ) {
                Text("Send Reset Link", fontSize = 18.sp, color = CherryRed)
            }

            Spacer(Modifier.height(16.dp))

            TextButton(onClick = onBackToLoginClick) {
                Text("Back to Login", color = Color.White)
            }
        }
    }
}
