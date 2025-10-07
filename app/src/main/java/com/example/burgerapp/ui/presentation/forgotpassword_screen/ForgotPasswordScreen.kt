package com.example.burgerapp.ui.presentation.forgotpassword_screen
import androidx.compose.ui.res.stringResource
import com.example.burgerapp.R
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.burgerapp.ui.theme.CherryRed
import kotlinx.coroutines.launch
import com.example.burgerapp.AuthState

@Composable
fun ForgotPasswordScreen(
    onSendResetClick: (String) -> Unit,
    onBackToLoginClick: () -> Unit,
    authState: AuthState
) {
    var email by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    // react to auth state (success/error)
    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Error -> coroutineScope.launch { snackbarHostState.showSnackbar(authState.message) }
            is AuthState.Success -> coroutineScope.launch { snackbarHostState.showSnackbar(authState.message) }
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
            Text(stringResource(R.string.forgot_password), fontSize = 28.sp, fontWeight = FontWeight.Bold, color = White)

            Spacer(Modifier.height(16.dp))

            Text(
                stringResource(R.string.forgot_password_description),
                fontSize = 16.sp,
                color = White
            )

            Spacer(Modifier.height(32.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text(stringResource(R.string.email), color = White) }, // TODO: use string resources
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

            Button(
                onClick = { onSendResetClick(email) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = White,
                    contentColor = CherryRed
                )
            ) {
                Text(stringResource(R.string.send_reset_link), fontSize = 18.sp, color = CherryRed)
            }

            Spacer(Modifier.height(16.dp))

            TextButton(onClick = onBackToLoginClick) {
                Text(stringResource(R.string.back_to_login), color =White)
            }

            // Loading indicator
            if (authState is AuthState.Loading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 24.dp),
                    color = White
                )
            }
        }
    }
}