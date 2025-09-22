package com.example.burgerapp.ui.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.burgerapp.R
import com.example.burgerapp.ui.theme.CherryRed
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(
    onRegisterClick: (String, String) -> Unit,
    onGoogleRegisterClick: () -> Unit,
    onNavigateToLogin: () -> Unit,
    message: String = ""
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
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
            Text(text = stringResource(R.string.create_account), fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.White)

            Spacer(Modifier.height(32.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text(text = stringResource(R.string.email), color = Color.White) },
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

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text(text = stringResource(R.string.password), color = Color.White) },
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

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text(text = stringResource(R.string.confirm_password), color = Color.White) },
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

            // Register Button
            Button(
                onClick = {
                    if (password != confirmPassword) {
                        coroutineScope.launch { snackbarHostState.showSnackbar("Passwords do not match") }
                    } else {
                        onRegisterClick(email, password)
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = CherryRed)
            ) {
                Text(text = stringResource(R.string.register), fontSize = 18.sp, color = CherryRed)
            }

            Spacer(Modifier.height(16.dp))

            // Google Sign-In
            OutlinedButton(
                onClick = onGoogleRegisterClick,
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
            ) {
                Icon(painter = painterResource(id = R.drawable.ic_google), contentDescription = "Google Sign-In", tint = Color.White)
                Spacer(Modifier.width(8.dp))
                Text(text = stringResource(R.string.register_with_google), color = Color.White, fontSize = 16.sp)
            }

            Spacer(Modifier.height(24.dp))

            TextButton(onClick = onNavigateToLogin) {
                Text(text = stringResource(R.string.already_have_account), color = Color.White)

            }
        }
    }
}
