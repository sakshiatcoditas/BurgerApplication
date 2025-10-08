package com.example.burgerapp.ui.presentation.profile_screen

import androidx.compose.ui.res.painterResource
import com.example.burgerapp.R
import com.example.burgerapp.viewmodel.AuthViewModel

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

import com.google.firebase.auth.FirebaseUser

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
import com.example.burgerapp.ui.presentation.home_screen.components.UserAvatar
import com.example.burgerapp.ui.theme.searchbarcolor

@Composable
fun ProfileScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    onLogoutClick: () -> Unit,
    onBackClick: () -> Unit
) {
    val user by viewModel.currentUser.collectAsState()
    val emailUserName by viewModel.emailUserName.collectAsState()
    val deliveryAddress by viewModel.deliveryAddress.collectAsState()

    var name by remember { mutableStateOf("") }
    val email = user?.email ?: ""
    val isEditable = user?.displayName.isNullOrEmpty()

    // Initialize name
    LaunchedEffect(user, emailUserName) {
        user?.let { firebaseUser ->
            if (!isEditable) {
                name = firebaseUser.displayName ?: ""
            } else {
                viewModel.fetchNameFromDatabase(firebaseUser.uid)
                name = emailUserName ?: ""
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0x88FF0000))
    ) {
        // White bottom dialog
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .offset(y = 200.dp)
                .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                .background(Color.White)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(19.dp)
        ) {
            Spacer(modifier = Modifier.height(13.dp))

            // Back button
            IconButton(onClick = onBackClick) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }

            // Avatar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 30.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                UserAvatar(
                    userName = user?.displayName ?: emailUserName,
                    userPhotoUrl = user?.photoUrl?.toString(),
                    modifier = Modifier
                        .size(139.dp)
                        .clip(RoundedCornerShape(16.dp))
                )
            }

            Spacer(Modifier.height(32.dp))

            // Name field
            OutlinedTextField(
                value = name,
                onValueChange = { newName -> if (isEditable) name = newName },
                label = { Text("Name") },
                singleLine = true,
                enabled = isEditable,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            // Email field (read-only)
            OutlinedTextField(
                value = email,
                onValueChange = {},
                label = { Text("Email") },
                singleLine = true,
                enabled = false,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            // Delivery Address (bind directly to ViewModel)
            OutlinedTextField(
                value = deliveryAddress,
                onValueChange = { newAddress ->
                    user?.uid?.let { uid ->
                        viewModel.updateDeliveryAddress(uid, newAddress)
                    }
                },
                label = { Text("Delivery Address") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            // Divider
            Spacer(modifier = Modifier.height(24.dp))
            HorizontalDivider(thickness = 1.dp, color = Color.Gray)
            Spacer(modifier = Modifier.height(16.dp))

            // Payment Details row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp)
                    .clickable { /* Navigate */ },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Payment Details", style = MaterialTheme.typography.bodyLarge)
                Icon(Icons.Filled.ArrowForward, contentDescription = null)
            }

            // Order History row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp)
                    .clickable { /* Navigate */ },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Order History", style = MaterialTheme.typography.bodyLarge)
                Icon(Icons.Filled.ArrowForward, contentDescription = null)
            }

            // Buttons row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Edit Profile Button
                Button(
                    onClick = {
                        user?.uid?.let { uid ->
                            viewModel.updateEmailUserName(uid, name)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.weight(1f).height(70.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text("Edit Profile", color = Color.White)
                        Spacer(Modifier.width(8.dp))
                        Icon(
                            painter = painterResource(id = R.drawable.edit),
                            contentDescription = "Edit",
                            tint = Color.White
                        )
                    }
                }

                // Logout Button
                OutlinedButton(
                    onClick = onLogoutClick,
                    shape = RoundedCornerShape(20.dp),
                    border = ButtonDefaults.outlinedButtonBorder.copy(width = 3.dp),
                    modifier = Modifier.weight(1f).height(70.dp)
                ) {
                    Text("Logout", color = Color.Black)
                }
            }
        }
    }
}
