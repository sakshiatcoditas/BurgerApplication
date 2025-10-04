package com.example.burgerapp.ui.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.burgerapp.R
import com.example.burgerapp.ui.theme.CherryRed

// Topping/Side data class
data class Topping(
    val name: String,
    val imageRes: Int
)

@Composable
fun CustomScreen(
    initialSpiceLevel: Float,
    initialPortion: Int,
    onBackClick: () -> Unit
) {
    var spiceLevel by remember { mutableFloatStateOf(initialSpiceLevel) }
    var portion by remember { mutableIntStateOf(initialPortion) }

    // ----------------- TOPPINGS LIST -----------------
    val toppingsList = listOf(
        Topping("Bacon", R.drawable.bacon),
        Topping("Tomato", R.drawable.tomato),
        Topping("Pickles", R.drawable.pickles),
        Topping("Onion", R.drawable.onion)
    )

    val selectedToppings = remember { mutableStateListOf<String>() }

    // ----------------- SIDES LIST -----------------
    val sidesList = listOf(
        Topping("Fries", R.drawable.fries),
        Topping("Coleslaw", R.drawable.caloslew),
        Topping("Salad", R.drawable.salad),
        Topping("OnionRing", R.drawable.onionrings)
    )
    val selectedSides = remember { mutableStateListOf<String>() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // ----------------- BACK BUTTON & BURGER IMAGE -----------------
        Box(modifier = Modifier.fillMaxWidth()) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier.padding(start = 16.dp, top = 16.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 60.dp),
                verticalAlignment = Alignment.Top
            ) {
                Image(
                    painter = painterResource(R.drawable.customscreen),
                    contentDescription = "Burger Image",
                    modifier = Modifier
                        .width(217.dp)
                        .height(297.dp)
                        .offset(x = (-20).dp)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(
                    modifier = Modifier
                        .width(162.dp)
                        .offset(x = (-8).dp),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "Customize Your Burger to Your Tastes",
                        fontFamily = FontFamily.Default,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 16.sp,
                        lineHeight = 16.sp * 1.8f
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Ultimate Experience",
                        fontFamily = FontFamily.Default,
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp,
                        lineHeight = 14.sp * 1.8f
                    )

                    Spacer(modifier = Modifier.height(35.dp))

                    // ----------------- SPICINESS SLIDER -----------------
                    Column(
                        modifier = Modifier.width(162.dp),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Spiciness",
                            fontWeight = FontWeight.Medium,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        DraggableSpicySlider(
                            spiceLevel = spiceLevel,
                            onSpiceChange = { newLevel -> spiceLevel = newLevel }
                        )
                    }

                    Spacer(modifier = Modifier.height(33.dp))

                    // ----------------- PORTION SELECTOR -----------------
                    Column(
                        modifier = Modifier.width(162.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "Portion",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            fontFamily = FontFamily.SansSerif,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Button(
                                onClick = { if (portion > 1) portion-- },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(
                                        0xFFD2042D
                                    )
                                ),
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(0.dp),
                                modifier = Modifier.size(40.dp)
                            ) {
                                Icon(
                                    Icons.Default.Remove,
                                    contentDescription = "Decrease",
                                    tint = Color.White
                                )
                            }

                            Text(
                                "$portion",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                fontFamily = FontFamily.SansSerif
                            )

                            Button(
                                onClick = { portion++ },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(
                                        0xFFD2042D
                                    )
                                ),
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(0.dp),
                                modifier = Modifier.size(40.dp)
                            ) {
                                Icon(
                                    Icons.Default.Add,
                                    contentDescription = "Increase",
                                    tint = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ----------------- TOPPINGS SECTION -----------------
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Toppings",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(toppingsList.size) { index ->
                val topping = toppingsList[index]
                val isSelected = topping.name in selectedToppings

                Surface(
                    modifier = Modifier
                        .width(84.dp)
                        .height(99.dp)
                        .clickable {
                            if (isSelected) selectedToppings.remove(topping.name)
                            else selectedToppings.add(topping.name)
                        },
                    shape = RoundedCornerShape(16.dp),
                    color = if (isSelected) Color(0xFFD2042D) else Color.Black,
                    shadowElevation = 4.dp
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        // WHITE BOX with topping image
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(61.dp)
                                .background(
                                    color = Color.White,
                                    shape = RoundedCornerShape(
                                        topStart = 15.dp,
                                        topEnd = 15.dp,
                                        bottomStart = 8.dp,
                                        bottomEnd = 8.dp
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(topping.imageRes),
                                contentDescription = topping.name,
                                modifier = Modifier.size(40.dp)
                            )
                        }

                        // Topping name + plus
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = topping.name,
                                fontWeight = FontWeight.Medium,
                                fontSize = 12.sp,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Box(
                                modifier = Modifier
                                    .size(16.dp)
                                    .clickable {
                                        if (isSelected) selectedToppings.remove(topping.name)
                                        else selectedToppings.add(topping.name)
                                    }
                            ) {
                                Image(
                                    painter = painterResource(R.drawable.redplus),
                                    contentDescription = "Outer Vector",
                                    modifier = Modifier.fillMaxSize()
                                )
                                Image(
                                    painter = painterResource(R.drawable.whiteplus),
                                    contentDescription = "Inner Vector",
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(3.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ----------------- SIDES SECTION -----------------
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Sides",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(sidesList.size) { index ->
                val side = sidesList[index]
                val isSelected = side.name in selectedSides

                Surface(
                    modifier = Modifier
                        .width(84.dp)
                        .height(99.dp)
                        .clickable {
                            if (isSelected) selectedSides.remove(side.name)
                            else selectedSides.add(side.name)
                        },
                    shape = RoundedCornerShape(16.dp),
                    color = if (isSelected) Color(0xFFD2042D) else Color.Black,
                    shadowElevation = 4.dp
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        // WHITE BOX with side image
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(61.dp)
                                .background(
                                    color = Color.White,
                                    shape = RoundedCornerShape(
                                        topStart = 15.dp,
                                        topEnd = 15.dp,
                                        bottomStart = 8.dp,
                                        bottomEnd = 8.dp
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(side.imageRes),
                                contentDescription = side.name,
                                modifier = Modifier.size(40.dp)
                            )
                        }

                        // Side name + plus
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = side.name,
                                fontWeight = FontWeight.Medium,
                                fontSize = 12.sp,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Box(
                                modifier = Modifier
                                    .size(16.dp)
                                    .clickable {
                                        if (isSelected) selectedSides.remove(side.name)
                                        else selectedSides.add(side.name)
                                    }
                            ) {
                                Image(
                                    painter = painterResource(R.drawable.redplus),
                                    contentDescription = "Outer Vector",
                                    modifier = Modifier.fillMaxSize()
                                )
                                Image(
                                    painter = painterResource(R.drawable.whiteplus),
                                    contentDescription = "Inner Vector",
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(3.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

// ----------------- TOTAL + ORDER BUTTON -----------------
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Total amount text
            Text(
                text = "Total: ₹200",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            // Order Now button
            Button(
                onClick = {
                    // TODO: Navigate to Payment screen
                },
                colors = ButtonDefaults.buttonColors(containerColor = CherryRed),
                modifier = Modifier
                    .height(60.dp)
                    .width(230.dp),
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

        Spacer(modifier = Modifier.height(16.dp))

    }
}

@Preview(showBackground = true)
@Composable
fun CustomScreenPreview() {
    CustomScreen(
        initialSpiceLevel = 0.7f,
        initialPortion = 1,
        onBackClick = {}
    )
}
