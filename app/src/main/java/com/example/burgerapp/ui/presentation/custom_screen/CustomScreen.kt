package com.example.burgerapp.ui.presentation.custom_screen

import androidx.compose.foundation.Image
import androidx.compose.ui.res.stringResource
import com.example.burgerapp.ui.theme.CherryRed

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.burgerapp.R
import com.example.burgerapp.data.Burger
import com.example.burgerapp.data.Topping
import com.example.burgerapp.ui.presentation.detail_screen.DraggableSpicySlider

import com.example.burgerapp.viewmodel.CustomViewModel


@Composable
fun CustomScreen(
    burger: Burger,
    navController: NavController,
    initialSpiceLevel: Float,
    initialPortion: Int,
    onBackClick: () -> Unit,
    viewModel: CustomViewModel = hiltViewModel()
) {
    var spiceLevel by remember { mutableFloatStateOf(initialSpiceLevel) }
    var portion by remember { mutableIntStateOf(initialPortion) }

    val options by viewModel.options.collectAsState()

    // TODO: take this from firebase
    // Names **must exactly match Firebase keys**
    val toppingsList = listOf(
        Topping("bacon", R.drawable.bacon),
        Topping("tomato", R.drawable.tomato),
        Topping("pickles", R.drawable.pickles),
        Topping("onions", R.drawable.onion)
    )
    val sidesList = listOf(
        Topping("fries", R.drawable.fries),
        Topping("coleslaw", R.drawable.caloslew),
        Topping("salad", R.drawable.salad),
        Topping("onionRings", R.drawable.onionrings)
    )

    val selectedToppings = viewModel.selectedToppings
    val selectedSides = viewModel.selectedSides

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // ----------------- BACK BUTTON & BURGER IMAGE -----------------
        Box(modifier = Modifier.fillMaxWidth()) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier.padding(start = 8.dp, top = 16.dp)

            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription =  stringResource(R.string.back)
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
                        text = stringResource(R.string.customize_burger),
                        fontFamily = FontFamily.Default,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 16.sp,
                        lineHeight = 16.sp * 1.8f
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = stringResource(R.string.ultimate_experience),
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
                            text = stringResource(R.string.spiciness),
                            fontWeight = FontWeight.Medium,
                            color = Black
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
                            text = stringResource(R.string.portion),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            fontFamily = FontFamily.SansSerif,
                            color = Black
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Button(
                                onClick = { if (portion > 1) portion-- },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = CherryRed
                                ),
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(0.dp),
                                modifier = Modifier.size(40.dp)
                            ) {
                                Icon(
                                    Icons.Default.Remove,
                                    contentDescription = stringResource(R.string.decrease),
                                    tint = White
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
                                    containerColor = CherryRed
                                ),
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(0.dp),
                                modifier = Modifier.size(40.dp)
                            ) {
                                Icon(
                                    Icons.Default.Add,
                                    contentDescription = stringResource(R.string.increase),

                                    tint = White
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ----------------- TOPPINGS -----------------
        Text(
            text = stringResource(R.string.toppings),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 16.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))

        // TODO: separate widget
        LazyRow(
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(toppingsList.size) { index ->
                val topping = toppingsList[index]
                val isSelected = topping.name in selectedToppings


                Surface(
                    modifier = Modifier
                        .width(84.dp)
                        .height(99.dp),
                    shape = RoundedCornerShape(16.dp),
                    color = if (isSelected) CherryRed else Black,
                    onClick = { viewModel.toggleTopping(topping.name) },
                    shadowElevation = 8.dp // increased
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                                .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                                .background(White),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(topping.imageRes),
                                contentDescription = topping.name,
                                modifier = Modifier.size(40.dp)
                            )
                        }
                        Text(
                            text = topping.name,
                            fontWeight = FontWeight.Medium,
                            fontSize = 12.sp,
                            color =White,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

// ----------------- SIDES -----------------
        Text(
            text = stringResource(R.string.sides),

            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 16.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(sidesList.size) { index ->
                val side = sidesList[index]
                val isSelected = side.name in selectedSides

                Box(
                    modifier = Modifier.shadow(8.dp, RoundedCornerShape(16.dp))
                ) {
                    Surface(
                        modifier = Modifier
                            .width(84.dp)
                            .height(99.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .clickable(
                                onClick = { viewModel.toggleSide(side.name) },
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ),
                        shape = RoundedCornerShape(16.dp),
                        color = if (isSelected) CherryRed else Black,
                        shadowElevation = 8.dp // increased
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(52.dp)
                                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                                    .background(White),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(side.imageRes),
                                    contentDescription = side.name,
                                    modifier = Modifier.size(40.dp)
                                )
                            }
                            Text(
                                text = side.name,
                                fontWeight = FontWeight.Medium,
                                fontSize = 12.sp,
                                color =White,
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                        }
                    }
                }
            }
        }


        Spacer(modifier = Modifier.height(16.dp))

        // ----------------- TOTAL + ORDER -----------------
        val totalPrice by remember(selectedToppings, selectedSides, portion, options) {
            derivedStateOf {
                viewModel.getFinalPrice(burger.price, portion)
            }
        }


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Total: $${String.format("%.2f", totalPrice)}",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Black
            )

            Button(
                onClick = {
                    navController.navigate(
                        "paymentScreen/${burger.burgerId}/$portion/$spiceLevel/$totalPrice"
                    )


                },
                colors = ButtonDefaults.buttonColors(containerColor = CherryRed),
                modifier = Modifier
                    .height(60.dp)
                    .width(230.dp),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(
                    text = stringResource(R.string.order_now),
                    color = White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}