package com.example.burgerapp.ui.presentation.detail_screen

import androidx.compose.ui.res.stringResource
import com.example.burgerapp.R
import android.annotation.SuppressLint
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.example.burgerapp.data.Burger
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.example.burgerapp.ui.theme.CherryRed
import com.example.burgerapp.ui.theme.Gray
import com.example.burgerapp.ui.theme.LightGray
import com.example.burgerapp.ui.theme.green
import com.example.burgerapp.ui.theme.yellow
import com.example.burgerapp.viewmodel.DetailViewModel
import kotlin.math.roundToInt



@Composable
fun DraggableSpicySlider(
    spiceLevel: Float,
    onSpiceChange: (Float) -> Unit
) {
    val sliderWidth = 150.dp
    val sliderHeight = 8.dp
    val thumbSize = 25.dp

    val animatedOffset by animateFloatAsState(
        targetValue = spiceLevel,
        label = "thumbAnimation"
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
                color = LightGray,
                cornerRadius = CornerRadius(trackHeightPx / 2, trackHeightPx / 2),
                topLeft = Offset(0f, trackY),
                size = Size(width = size.width, height = trackHeightPx)
            )

            // Fill
            drawRoundRect(
                color = CherryRed,
                cornerRadius = CornerRadius(trackHeightPx / 2, trackHeightPx / 2),
                topLeft = Offset(0f, trackY),
                size = Size(width = size.width * animatedOffset, height = trackHeightPx)
            )
        }


        // Thumb
        Box(
            modifier = Modifier
                .offset {
                    val maxOffsetPx = with(density) { (sliderWidth - 15.dp).toPx() }
                    val xPx = (animatedOffset * maxOffsetPx).coerceIn(0f, maxOffsetPx)
                    IntOffset(xPx.roundToInt(), 0)
                }
                .width(15.dp)
                .height(25.dp)
                .clip(RoundedCornerShape(4.dp))  // <-- ensures color stays inside the rounded shape
                .background(CherryRed)
                .align(Alignment.CenterStart)

        )

    }

    // Labels
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(stringResource(R.string.mild), fontSize = 12.sp, color = green)
        Text(stringResource(R.string.medium), fontSize = 12.sp, color = yellow)
        Text(stringResource(R.string.spicy), fontSize = 12.sp, color = Red)


    }
}

//  Burger Detail Screen
@SuppressLint("DefaultLocale")
@Composable
fun BurgerDetailScreen(
    burger: Burger, // keep as non-null
    onBackClick: () -> Unit,
    navController: NavHostController,
    detailViewModel: DetailViewModel = hiltViewModel()
) {
    // Read state directly from ViewModel
    val portion by remember { derivedStateOf { detailViewModel.portion } }
    val spiceLevel by remember { derivedStateOf { detailViewModel.spiceLevel } }

    Box(modifier = Modifier.fillMaxSize()) {

        // Main content
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Back button
            IconButton(onClick = onBackClick) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.back))
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Burger Image
            SubcomposeAsyncImage(
                model = burger.imageUrl,
                contentDescription = burger.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(350.dp)
                    .height(280.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                when (painter.state) {
                    is AsyncImagePainter.State.Loading -> {
                        // Show loader while image loads
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(LightGray.copy(alpha = 0.3f)),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                color = CherryRed,
                                strokeWidth = 3.dp,
                                modifier = Modifier.size(36.dp)
                            )
                        }
                    }
                    is AsyncImagePainter.State.Error -> {
                        // Show fallback if image fails
                        Icon(
                            imageVector = Icons.Default.BrokenImage,
                            contentDescription = "Image error",
                            tint =Gray,
                            modifier = Modifier.size(64.dp)
                        )
                    }
                    else -> {
                        // Show the actual image when loaded
                        SubcomposeAsyncImageContent()
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Burger info
            Text(
                text = burger.name,
                fontSize = 25.sp,
                fontWeight = FontWeight.SemiBold,
                color = Black,
                modifier = Modifier.fillMaxWidth()
            )
            Text(// TODO: use icon for star and use common widget for this.
                text = "⭐ ${burger.rating}",
                style = MaterialTheme.typography.bodyMedium,
                color =Black,
                modifier = Modifier.padding(vertical = 4.dp)
            )
            Text(
                text = burger.description.ifBlank {  stringResource(R.string.no_description) },
                fontSize = 16.sp,
                lineHeight = 28.sp,
                color = Gray,
                modifier = Modifier.padding(top = 6.dp, bottom = 12.dp)
            )

            Spacer(modifier = Modifier.height(7.dp))

            // Spiciness + Portion Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Draggable spicy slider
                Column(
                    modifier = Modifier.width(168.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = stringResource(R.string.spiciness_level),
                        fontWeight = FontWeight.Medium,
                        color = Black
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    DraggableSpicySlider(
                        spiceLevel = spiceLevel,
                        onSpiceChange = { newLevel ->
                            detailViewModel.spiceLevel = newLevel
                        }
                    )
                }

                // Portion selector
                Column(
                    modifier = Modifier.width(121.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        stringResource(R.string.portion),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = FontFamily.SansSerif,
                        color = Black
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { if (detailViewModel.portion > 1) detailViewModel.portion-- },
                            colors = ButtonDefaults.buttonColors(containerColor = CherryRed),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(0.dp),
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(Icons.Default.Remove, contentDescription = stringResource(R.string.decrease), tint =White)
                        }

                        Text(
                            "${detailViewModel.portion}",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            fontFamily = FontFamily.SansSerif
                        )

                        Button(
                            onClick = { detailViewModel.portion++ },
                            colors = ButtonDefaults.buttonColors(containerColor = CherryRed),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(0.dp),
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = stringResource(R.string.increase), tint = White)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }

        // Bottom Buttons
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
                colors = ButtonDefaults.buttonColors(containerColor = CherryRed),
                modifier = Modifier
                    .width(130.dp)
                    .height(60.dp),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(
                    text = "$${String.format("%.2f", burger.price * portion)}",
                    //.format(transform value to a specific pattern)
                    //.2f means 2 decimal places to be the value converted
                    color = White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Order Now Button
            Button(
                onClick = {
                    navController.navigate(
                        "customScreen/${burger.burgerId}/$portion/$spiceLevel"
                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor =Black),
                modifier = Modifier
                    .width(230.dp)
                    .height(60.dp),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(
                    stringResource(R.string.order_now),
                    color = White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

