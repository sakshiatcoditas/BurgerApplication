package com.example.burgerapp.ui.ui
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.burgerapp.R
import com.example.burgerapp.burger.Burger

@Composable
fun CustomScreen(
    initialSpiceLevel: Float,   // comes from previous screen
    initialPortion: Int,        // comes from previous screen
    onBackClick: () -> Unit
) {
    // local state initialized from passed values
    var spiceLevel by remember { mutableFloatStateOf(initialSpiceLevel) }
    var portion by remember { mutableIntStateOf(initialPortion) }

    Box(modifier = Modifier.fillMaxWidth()) {
        // Back arrow
        IconButton(
            onClick = onBackClick,
            modifier = Modifier.padding(start = 16.dp, top = 16.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back"
            )
        }

        // Row with image on left, everything else on right
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 100.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Left image
            Image(
                painter = painterResource(R.drawable.customscreen),
                contentDescription = "Burger Image",
                modifier = Modifier
                    .width(217.dp)
                    .height(297.dp)
                    .offset(x = (-20).dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Right column (Text + Slider + Portion)
            Column(
                modifier = Modifier
                    .width(162.dp)
                    .offset(x = (-8).dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
                // Texts
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

                // Spicy slider section
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

                // Portion selector section
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
    }
}


@Preview(showBackground = true)
@Composable
fun CustomScreenPreview() {
    CustomScreen(
        initialSpiceLevel = 0.7f, // same default as DetailScreen
        initialPortion = 1,       // same default as DetailScreen
        onBackClick = {}
    )
}


