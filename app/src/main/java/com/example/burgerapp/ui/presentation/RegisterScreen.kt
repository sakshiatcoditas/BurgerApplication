package com.example.burgerapp.ui.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    onRegisterClick: (String, String) -> Unit,
    onGoogleRegisterClick: () -> Unit,
    onNavigateToLogin: () -> Unit,
    authState: AuthState
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    // Show snackbar on error
    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Error -> coroutineScope.launch {
                snackbarHostState.showSnackbar(authState.message)
            }
            else -> {}
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
            Text(
                stringResource(R.string.create_account),
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = White
            )

            Spacer(Modifier.height(32.dp))

            // Email
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text(stringResource(R.string.email), color = White) },
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
                label = { Text(stringResource(R.string.password), color = White) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = White,
                    unfocusedBorderColor = White,
                    focusedLabelColor = White,
                    unfocusedLabelColor =White,
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
                label = { Text(stringResource(R.string.confirm_password), color = White) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = White,
                    unfocusedBorderColor = White,
                    focusedLabelColor = White,
                    unfocusedLabelColor = White,
                    focusedTextColor =White,
                    unfocusedTextColor = White,
                    cursorColor = White
                )
            )

            Spacer(Modifier.height(24.dp))

            // Register Button
            Button(
                onClick = {
                    if (password != confirmPassword) {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("Passwords do not match")
                        }
                    } else {
                        onRegisterClick(email, password)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = White,
                    contentColor = CherryRed
                ),
                enabled = authState !is AuthState.Loading
            ) {
                Text(stringResource(R.string.register), fontSize = 18.sp, color = CherryRed)
            }

            Spacer(Modifier.height(16.dp))

            // Google Sign-In
            OutlinedButton(
                onClick = onGoogleRegisterClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor =White),
                enabled = authState !is AuthState.Loading
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_google),
                    contentDescription = stringResource(R.string.google_sign_in),
                    tint = White
                )
                Spacer(Modifier.width(8.dp))
                Text(stringResource(R.string.register_with_google), color = White, fontSize = 16.sp)
            }

            Spacer(Modifier.height(24.dp))

            TextButton(onClick = onNavigateToLogin) {
                Text(stringResource(R.string.already_have_account), color = White) // TODO: use string resources
            }

            // Loader
            if (authState is AuthState.Loading) {
                Spacer(Modifier.height(24.dp))
                CircularProgressIndicator(color = White)
            }
        }
    }
}