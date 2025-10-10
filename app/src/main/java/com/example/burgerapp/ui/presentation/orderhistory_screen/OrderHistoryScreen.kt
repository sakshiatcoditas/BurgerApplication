package com.example.burgerapp.ui.presentation.orderhistory_screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.burgerapp.data.Order
import com.example.burgerapp.viewmodel.OrderViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun OrderHistoryScreen(
    viewModel: OrderViewModel = hiltViewModel(),
    userId: String,
    onBackClick: () -> Unit
) {
    val orders by viewModel.orders.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchUserOrders(userId)
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
            Text(
                text = "Order History",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        when {
            isLoading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            orders.isEmpty() -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No orders yet!", color = Color.Gray)
                }
            }
            else -> {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(orders) { order ->
                        OrderCard(order)
                    }
                }
            }
        }
    }


}

@Composable
fun OrderCard(order: Order) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(order.burgerName,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp)

            Spacer(modifier = Modifier.height(4.dp))

            Text("Total: ₹${order.totalPrice}",
                fontWeight = FontWeight.Medium)

            Text("Quantity: ${order.portion}")

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Ordered on: ${
                    SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(order.timestamp)
                }",
                color = Color.Gray,
                fontSize = 12.sp
            )
        }
    }


}