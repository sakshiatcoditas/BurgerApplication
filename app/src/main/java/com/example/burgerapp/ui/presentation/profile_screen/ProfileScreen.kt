
package com.example.burgerapp.ui.presentation.profile_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.burgerapp.R
import com.example.burgerapp.ui.presentation.home_screen.components.UserAvatar
import com.example.burgerapp.viewmodel.AuthViewModel



@Composable
fun ProfileScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    onLogoutClick: () -> Unit,
    onBackClick: () -> Unit,
    navController: NavController

) {
    val user by viewModel.currentUser.collectAsState()
    val emailUserName by viewModel.emailUserName.collectAsState()
    val deliveryAddress by viewModel.deliveryAddress.collectAsState()

    var isEditing by remember { mutableStateOf(false) } // toggle editing
    var address by remember { mutableStateOf("") }

    val email = user?.email ?: ""



    val displayName = user?.displayName ?: emailUserName ?: "User"



    // Sync address from ViewModel
    LaunchedEffect(deliveryAddress) {
        address = deliveryAddress
    }

    val neutralTextFieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = Color.Gray,
        unfocusedBorderColor = Color.Gray,
        errorBorderColor = Color.Gray,
        focusedLabelColor = Color.DarkGray,
        unfocusedLabelColor = Color.DarkGray,
        focusedTextColor = Color.Black,
        unfocusedTextColor = Color.Black,
        cursorColor = Color.Transparent,
        focusedContainerColor = Color.Transparent,
        unfocusedContainerColor = Color.Transparent,
        disabledContainerColor = Color.Transparent
    )


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0x88FF0000))
    ) {
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

            IconButton(onClick = onBackClick) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 30.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                UserAvatar(
                    userName = displayName,
                    userPhotoUrl = user?.photoUrl?.toString(),
                    modifier = Modifier
                        .size(139.dp)
                        .clip(RoundedCornerShape(16.dp))
                )
            }

            Spacer(Modifier.height(32.dp))

            // Name field (always read-only)
            OutlinedTextField(
                value = displayName,
                onValueChange = {},
                label = { Text("Name") },
                singleLine = true,
                readOnly = true,
                modifier = Modifier.fillMaxWidth(),
                colors = neutralTextFieldColors
            )


            Spacer(Modifier.height(16.dp))

            // Email field (always read-only)
            OutlinedTextField(
                value = email,
                onValueChange = {},
                label = { Text("Email") },
                singleLine = true,
                readOnly = true,
                modifier = Modifier.fillMaxWidth(),
                colors = neutralTextFieldColors

            )

            Spacer(Modifier.height(16.dp))

            // Delivery Address field (editable only in edit mode)
            OutlinedTextField(
                value = deliveryAddress,
                onValueChange = { newAddress ->
                    if (isEditing) {
                        user?.uid?.let { uid ->
                            viewModel.updateDeliveryAddress(uid, newAddress)
                        }
                    }
                },
                label = { Text("Delivery Address") },
                singleLine = true,
                readOnly = !isEditing, // only editable in edit mode
                modifier = Modifier.fillMaxWidth(),
                colors = neutralTextFieldColors
            )

            Spacer(modifier = Modifier.height(24.dp))
            HorizontalDivider(thickness = 1.dp, color = Color.Gray)
            Spacer(modifier = Modifier.height(16.dp))

            // Info Rows
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp)
                    .clickable { /* Navigate to Payment */ },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Payment Details", style = MaterialTheme.typography.bodyLarge)
                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null)
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp)
                    .clickable { },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = { navController.navigate("orderHistory") },
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 8.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                ) {
                    Text("Order History", color = Color.White, fontWeight = FontWeight.Bold)
                }

            }

            // Buttons Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Edit / Save Button
                Button(
                    onClick = {
                        if (isEditing) {
                            // Save new address
                            user?.uid?.let { uid ->
                                viewModel.updateDeliveryAddress(uid, address)
                            }
                        }
                        isEditing = !isEditing
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(70.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = if (isEditing) "Save" else "Edit Profile",
                            color = Color.White
                        )
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

