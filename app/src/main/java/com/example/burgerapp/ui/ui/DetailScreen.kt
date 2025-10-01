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

import androidx.compose.material.icons.filled.Remove
import coil.compose.rememberAsyncImagePainter
import androidx.compose.foundation.Image

import androidx.compose.foundation.shape.RoundedCornerShape


import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.LinearProgressIndicator

import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import com.example.burgerapp.burger.Burger

@Composable
fun BurgerDetailScreen(
    burger: Burger,
    onBackClick: () -> Unit,
    onOrderClick: (Int) -> Unit,
    SpicyBar: @Composable (Float) -> Unit
) {
    var portion by remember { mutableIntStateOf(1) }
    var spiceLevel by remember { mutableFloatStateOf(0.7f) }

    Box(modifier = Modifier.fillMaxSize()) {

        // Main content above buttons
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Back Arrow
            IconButton(onClick = onBackClick) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
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

            // Name, rating, description
            Text(
                text = burger.name,
                fontSize = 25.sp,
                fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = "⭐ ${burger.rating}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(vertical = 4.dp)
            )
            Text(
                text = burger.description.ifBlank { "No description available." },
                fontSize = 16.sp,
                lineHeight = 28.sp,
                modifier = Modifier.padding(top = 6.dp, bottom = 12.dp)
            )

            Spacer(modifier = Modifier.height(7.dp))

            // Spiciness + Portion Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Spicy bar
                Column(
                    modifier = Modifier.width(168.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Spiciness",
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    SpicyBar(spiceLevel)
                }

                // Portion selector
                Column(
                    modifier = Modifier.width(121.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Portion",
                        fontSize = 14.sp,
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Medium,
                        fontFamily = androidx.compose.ui.text.font.FontFamily.SansSerif
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { if (portion > 1) portion-- },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD2042D)),
                            shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(0.dp),
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(Icons.Default.Remove, contentDescription = "Decrease", tint = Color.White)
                        }

                        Text(
                            "$portion",
                            fontSize = 14.sp,
                            fontWeight = androidx.compose.ui.text.font.FontWeight.Medium,
                            fontFamily = androidx.compose.ui.text.font.FontFamily.SansSerif
                        )

                        Button(
                            onClick = { portion++ },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD2042D)),
                            shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(0.dp),
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Increase", tint = Color.White)
                        }
                    }
                }
            }
        }

        // Fixed Bottom Buttons (always visible at bottom)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(horizontal = 16.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Price Button
            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD2042D)),
                modifier = Modifier
                    .width(100.dp)
                    .height(60.dp),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(
                    text = "$${String.format("%.2f", burger.price * portion)}",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                )
            }

            // Order Now Button
            Button(
                onClick = { onOrderClick(portion) },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD2042D)),
                modifier = Modifier
                    .width(230.dp)
                    .height(60.dp),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(
                    text = "Order Now",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                )
            }
        }
    }
}


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

    BurgerDetailScreen(
        burger = sampleBurger,
        onBackClick = {},
        onOrderClick = {},
        SpicyBar = { progress ->
            LinearProgressIndicator(
                progress = progress,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp),
                color = Color(0xFFD2042D),
                trackColor = Color.LightGray,
                strokeCap = ProgressIndicatorDefaults.LinearStrokeCap
            )
        }
    )
}
