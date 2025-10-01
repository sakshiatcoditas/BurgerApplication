package com.example.burgerapp.ui.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.burgerapp.burger.Burger
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import kotlin.math.roundToInt

// 🔹 Custom Draggable Slider
// 🔹 Custom Draggable Slider
@Composable
fun DraggableSpicySlider(
    spiceLevel: Float,
    onSpiceChange: (Float) -> Unit
) {
    val sliderWidth = 150.dp
    val sliderHeight = 8.dp      // Track height
    val thumbSize = 25.dp        // Thumb height/width

    val animatedOffset by animateFloatAsState(
        targetValue = spiceLevel,
        label = "thumbAnim"
    )

    val density = LocalDensity.current

    Box(
        modifier = Modifier
            .width(sliderWidth)
            .height(thumbSize)
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    val sensitivity = 4.0f
                    val newOffset = (animatedOffset + dragAmount.x / size.width * sensitivity)
                        .coerceIn(0f, 1f)
                    onSpiceChange(newOffset)
                }
            }
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
        ) {
            val trackHeightPx = with(density) { sliderHeight.toPx() }
            val trackY = (size.height - trackHeightPx) / 2  // center track vertically

            // Track
            drawRoundRect(
                color = Color.LightGray,
                cornerRadius = CornerRadius(trackHeightPx / 2, trackHeightPx / 2),
                topLeft = androidx.compose.ui.geometry.Offset(0f, trackY),
                size = androidx.compose.ui.geometry.Size(width = size.width, height = trackHeightPx)
            )

            // Fill
            drawRoundRect(
                color = Color(0xFFD2042D),
                cornerRadius = CornerRadius(trackHeightPx / 2, trackHeightPx / 2),
                topLeft = androidx.compose.ui.geometry.Offset(0f, trackY),
                size = androidx.compose.ui.geometry.Size(width = size.width * animatedOffset, height = trackHeightPx)
            )
        }

        // Thumb
        // Thumb
        Box(
            modifier = Modifier
                .offset {
                    val maxOffsetPx = with(density) { (sliderWidth - 15.dp).toPx() } // width
                    val xPx = (animatedOffset * maxOffsetPx).coerceIn(0f, maxOffsetPx)
                    IntOffset(xPx.roundToInt(), 0)
                }
                .width(15.dp)  // narrower width
                .height(25.dp) // keep same height
                .background(Color(0xFFD2042D), RoundedCornerShape(4.dp))
                .align(Alignment.CenterStart)
                .shadow(2.dp, RoundedCornerShape(4.dp))
        )

    }

    // Labels
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text("🌶 Spicy", fontSize = 12.sp, color = Color.Red)
        Text("😋 Medium", fontSize = 12.sp, color = Color(0xFFFFC107))
        Text("🙂 Mild", fontSize = 12.sp, color = Color(0xFF4CAF50))
    }
}

// 🔹 Burger Detail Screen
@Composable
fun BurgerDetailScreen(
    burger: Burger,
    onBackClick: () -> Unit,
    onOrderClick: (Int) -> Unit
) {
    var portion by remember { mutableIntStateOf(1) }
    var spiceLevel by remember { mutableFloatStateOf(0.7f) }

    Box(modifier = Modifier.fillMaxSize()) {

        // Main content
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            IconButton(onClick = onBackClick) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Image(
                painter = rememberAsyncImagePainter(burger.imageUrl),
                contentDescription = burger.name,
                modifier = Modifier
                    .width(350.dp)
                    .height(300.dp)
                    .align(Alignment.CenterHorizontally),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = burger.name,
                fontSize = 25.sp,
                fontWeight = FontWeight.SemiBold,
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
                // ✅ Draggable spicy slider
                Column(
                    modifier = Modifier.width(168.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Spiciness",
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    DraggableSpicySlider(
                        spiceLevel = spiceLevel,
                        onSpiceChange = { newLevel ->
                            spiceLevel = newLevel
                        }
                    )
                }

                // Portion selector
                Column(
                    modifier = Modifier.width(121.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Portion",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = FontFamily.SansSerif
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { if (portion > 1) portion-- },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD2042D)),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(0.dp),
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(Icons.Default.Remove, contentDescription = "Decrease", tint = Color.White)
                        }

                        Text(
                            "$portion",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            fontFamily = FontFamily.SansSerif
                        )

                        Button(
                            onClick = { portion++ },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD2042D)),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(0.dp),
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Increase", tint = Color.White)
                        }
                    }
                }
            }
        }

        // Fixed Bottom Buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(horizontal = 16.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD2042D)),
                modifier = Modifier
                    .width(130.dp)
                    .height(60.dp),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(
                    text = "$${String.format("%.2f", burger.price * portion)}",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Button(
                onClick = { onOrderClick(portion) },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF000000)),
                modifier = Modifier
                    .width(230.dp)
                    .height(60.dp),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(
                    text = "Order Now",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

// 🔹 Preview
@Preview(showBackground = true)
@Composable
fun PreviewBurgerDetailScreen() {
    BurgerDetailScreen(
        burger = Burger(
            name = "Cheese Burger",
            description = "Juicy grilled beef patty with cheese, lettuce, and tomato.",
            imageUrl = "",
            price = 8.99,
            rating = 4.5
        ),
        onBackClick = {},
        onOrderClick = {}
    )
}
