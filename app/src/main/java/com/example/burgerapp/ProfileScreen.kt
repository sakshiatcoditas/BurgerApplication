package com.example.burgerapp

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.google.android.gms.auth.api.signin.GoogleSignInClient

@Composable
fun ProfileScreen(
    googleSignInClient: GoogleSignInClient,
    onLogoutClick: () -> Unit
) {
    val user = FirebaseAuth.getInstance().currentUser

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Profile",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        user?.let {
            Text(text = "Name: ${it.displayName ?: "N/A"}", fontSize = 18.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Email: ${it.email ?: "N/A"}", fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                // Sign out from Firebase
                FirebaseAuth.getInstance().signOut()

                // Sign out from Google
                googleSignInClient.signOut().addOnCompleteListener {
                    // Navigate to LoginScreen after logout
                    onLogoutClick()
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text("Logout", fontSize = 16.sp, color = MaterialTheme.colorScheme.onPrimary)
        }
    }
}