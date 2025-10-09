package com.example.burgerapp

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.burgerapp.data.Order

@Composable
fun OrderHistoryCard(order: Order) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(order.burgerName, fontWeight = FontWeight.Bold, fontSize = 16.sp)
           // Text("Portion: ${order.portion}")
            Text("Total: $${String.format("%.2f", order.totalPrice)}")
            if (order.toppings.isNotEmpty()) Text("Toppings: ${order.toppings.joinToString()}")
            if (order.sides.isNotEmpty()) Text("Sides: ${order.sides.joinToString()}")
        }
    }
}
