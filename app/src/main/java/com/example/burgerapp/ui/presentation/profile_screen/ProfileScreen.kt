package com.example.burgerapp.ui.presentation.profile_screen

import androidx.compose.ui.res.painterResource
import com.example.burgerapp.R

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
import com.example.burgerapp.viewmodel.AuthViewModel
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
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf(user?.email ?: "") }
    var deliveryAddress by remember { mutableStateOf("") }

    val isEditable = user?.displayName.isNullOrEmpty()

    LaunchedEffect(user) {
        user?.let { firebaseUser ->
            if (!isEditable) {
                name = firebaseUser.displayName ?: ""
            } else {
                viewModel.fetchNameFromDatabase(firebaseUser.uid)
            }
        }
    }

    LaunchedEffect(emailUserName) {
        if (isEditable) {
            name = emailUserName ?: ""
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
                Icon(Icons.AutoMirrored.Filled.ArrowBack,  contentDescription = "Back")
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
                    userPhotoUrl = null,
                    modifier = Modifier
                        .size(width = 139.dp, height = 139.dp)
                        .clip(RoundedCornerShape(16.dp))
                )
            }

            Spacer(Modifier.height(32.dp))

            // Name field
            OutlinedTextField(
                value = name,
                onValueChange = { newName ->
                    if (isEditable) name = newName
                },
                label = { Text("Name") },
                singleLine = true,
                enabled = isEditable,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            // Email field (read-only)
            OutlinedTextField(
                value = email,
                onValueChange = { },
                label = { Text("Email") },
                singleLine = true,
                enabled = false,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            // Delivery Address field
            OutlinedTextField(
                value = deliveryAddress,
                onValueChange = { deliveryAddress = it },
                label = { Text("Delivery Address") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            // Divider line
            Spacer(modifier = Modifier.height(24.dp))
            HorizontalDivider(thickness = 1.dp, color = Color.Gray)
            Spacer(modifier = Modifier.height(16.dp))

// Payment Details row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp)
                    .clickable{},
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Payment Details",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.height(28.dp)
                )
                Icon(
                    imageVector = Icons.Filled.ArrowForward,
                    contentDescription = "Go to Payment Details"
                )
            }

// Order History row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp)
                    .clickable{},

                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Order History",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.height(28.dp)
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Go to Payment Details"
                )
            }

            // Buttons row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp)
                    .clickable{}, // optional spacing from above content
                horizontalArrangement = Arrangement.spacedBy(16.dp) // spacing between the buttons
            ) {
                // Edit Profile Button
                Button(
                    onClick = { /* Handle edit profile */ },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier
                        .weight(1f) // fills proportionally
                        .height(70.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text("Edit Profile", color = Color.White)
                        Spacer(modifier = Modifier.width(8.dp))
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
                    modifier = Modifier
                        .weight(1f)
                        .height(70.dp)
                ) {
                    Text("Logout", color = Color.Black)
                }
            }


        }
    }
}

