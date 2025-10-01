package com.example.burgerapp.ui.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Remove
import coil.compose.rememberAsyncImagePainter
import androidx.compose.foundation.Image
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import com.example.burgerapp.burger.Burger

@Composable
fun BurgerDetailScreen(
    burger: Burger,
    onBackClick: () -> Unit,
    onOrderClick: (Int) -> Unit,
    SpicyBar: @Composable (Float) -> Unit // your imported spicy bar Composable
) {
    var portion by remember { mutableStateOf(1) }
    var spiceLevel by remember { mutableFloatStateOf(0.7f) } // default spice level 0..1

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

        // Back Arrow
        IconButton(onClick = onBackClick) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Burger Image
        Image(
            painter = rememberAsyncImagePainter(burger.imageUrl),
            contentDescription = burger.name,
            modifier = Modifier
                .width(350.dp)
                .height(355.dp)
                .align(Alignment.CenterHorizontally),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Burger Name
        Text(
            text = burger.name,
            fontSize = 25.sp,
            fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold,
            modifier = Modifier.fillMaxWidth()
        )

        // Rating
        Text(
            text = "⭐ ${burger.rating}",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(vertical = 4.dp)
        )

        // Description
        Text(
            text = burger.description.ifBlank { "No description available." },
            fontSize = 16.sp,
            lineHeight = 28.sp,
            modifier = Modifier.padding(top = 6.dp, bottom = 12.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Spiciness + Portion Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            // Spicy bar (left)
            Column(
                modifier = Modifier.width(168.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "Spiciness", fontWeight = androidx.compose.ui.text.font.FontWeight.Medium)
                Spacer(modifier = Modifier.height(8.dp))
                // Your custom spicy bar Composable
                SpicyBar(spiceLevel)
            }

            // Portion (right)
            Column(
                modifier = Modifier.width(121.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Portion", fontSize = 18.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Medium)
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Decrement button
                    Button(
                        onClick = { if (portion > 1) portion-- },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD2042D)),
                        shape = CircleShape,
                        contentPadding = PaddingValues(0.dp),
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(Icons.Default.Remove, contentDescription = "Decrease", tint = Color.White)
                    }

                    Text("$portion", fontSize = 18.sp)

                    // Increment button
                    Button(
                        onClick = { portion++ },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD2042D)),
                        shape = CircleShape,
                        contentPadding = PaddingValues(0.dp),
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Increase", tint = Color.White)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Price + Order Now
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "$${String.format("%.2f", burger.price * portion)}",
                fontSize = 22.sp,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
            )

            Button(
                onClick = { onOrderClick(portion) },
                modifier = Modifier.height(50.dp)
            ) {
                Text("Order Now")
            }
        }
    }
}

// Preview with sample burger
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun BurgerDetailScreenPreview() {
    val sampleBurger = Burger(
        burgerId = "burger_01",
        name = "Spicy Chicken Burger",
        price = 6.49,
        rating = 4.3,
        description = "A perfectly grilled chicken patty infused with our signature spicy sauce, topped with fresh lettuce, juicy tomatoes, and melted cheese.",
        imageUrl = "https://raw.githubusercontent.com/sakshiatcoditas/BurgerImagesForApp/main/nonveg1.png"
    )

    // For preview, we pass a simple slider-looking placeholder for SpicyBar
    BurgerDetailScreen(
        burger = sampleBurger,
        onBackClick = {},
        onOrderClick = {},
        SpicyBar = { progress ->
            LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                                .fillMaxWidth()
                                .height(10.dp),
            color = Color(0xFFD2042D),
            trackColor = Color.LightGray,
            strokeCap = ProgressIndicatorDefaults.LinearStrokeCap,
            )
        }
    )
}
