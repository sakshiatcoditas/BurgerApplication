package com.example.burgerapp.ui.presentation.register_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.burgerapp.AuthState
import com.example.burgerapp.R

import com.example.burgerapp.ui.theme.CherryRed
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(
    onRegisterClick: (String, String, String) -> Unit, // name, email, password
    onGoogleRegisterClick: () -> Unit,
    onNavigateToLogin: () -> Unit,
    authState: AuthState
) {
    var name by remember { mutableStateOf("") } // NEW
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    // Show snackbar on error
    LaunchedEffect(authState) {
        if (authState is AuthState.Error) {
            coroutineScope.launch { snackbarHostState.showSnackbar(authState.message) }
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

            Text("Create Account", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = White)
            Spacer(Modifier.height(32.dp))

            // --- Name ---
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name", color = White) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = White,
                    unfocusedBorderColor = White,
                    focusedLabelColor = White,
                    unfocusedLabelColor = White,
                    focusedTextColor = White,
                    unfocusedTextColor = White,
                    cursorColor = White
                )
            )

            Spacer(Modifier.height(16.dp))

            // Email
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email", color = White) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = White,
                    unfocusedBorderColor = White,
                    focusedLabelColor = White,
                    unfocusedLabelColor = White,
                    focusedTextColor = White,
                    unfocusedTextColor = White,
                    cursorColor = White
                )
            )

            Spacer(Modifier.height(16.dp))

            // Password
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password", color = White) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = White,
                    unfocusedBorderColor = White,
                    focusedLabelColor = White,
                    unfocusedLabelColor = White,
                    focusedTextColor = White,
                    unfocusedTextColor = White,
                    cursorColor = White
                )
            )

            Spacer(Modifier.height(16.dp))

            // Confirm Password
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirm Password", color = White) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = White,
                    unfocusedBorderColor = White,
                    focusedLabelColor = White,
                    unfocusedLabelColor = White,
                    focusedTextColor = White,
                    unfocusedTextColor = White,
                    cursorColor = White
                )
            )

            Spacer(Modifier.height(24.dp))

            // Register Button
            Button(
                onClick = {
                    if (password != confirmPassword) {
                        coroutineScope.launch { snackbarHostState.showSnackbar("Passwords do not match") }
                    } else if (name.isBlank()) {
                        coroutineScope.launch { snackbarHostState.showSnackbar("Name cannot be empty") }
                    } else {
                        onRegisterClick(name, email, password)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = White, contentColor = CherryRed),
                enabled = authState !is AuthState.Loading
            ) {
                Text("Register", fontSize = 18.sp, color = CherryRed)
            }

            Spacer(Modifier.height(16.dp))

            // Google Sign-In
            OutlinedButton(
                onClick = onGoogleRegisterClick,
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = White),
                enabled = authState !is AuthState.Loading
            ) {
                Text("Register with Google", color = White, fontSize = 16.sp)
            }

            Spacer(Modifier.height(24.dp))

            TextButton(onClick = onNavigateToLogin) {
                Text("Already have an account?", color = White)
            }

            if (authState is AuthState.Loading) {
                Spacer(Modifier.height(24.dp))
                CircularProgressIndicator(color = White)
            }
        }
    }
}