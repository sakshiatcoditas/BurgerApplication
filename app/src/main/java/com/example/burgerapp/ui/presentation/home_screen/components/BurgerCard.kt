package com.example.burgerapp.ui.presentation.home_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.example.burgerapp.R
import com.example.burgerapp.data.Burger
import com.example.burgerapp.ui.theme.CherryRed
import com.example.burgerapp.ui.theme.LightGray
import kotlin.text.ifEmpty

@Composable
fun BurgerCard(
    burger: Burger,
    onFavoriteClick: () -> Unit,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp) // fixed card height
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            //  Show loader while image is loading
            SubcomposeAsyncImage(
                model = burger.imageUrl.ifEmpty { "https://via.placeholder.com/150" },
                contentDescription = burger.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(RoundedCornerShape(16.dp))
            ) {
                when (painter.state) {
                    is AsyncImagePainter.State.Loading -> {
                        // Loader while image is loading
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(LightGray.copy(alpha = 0.3f)),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                color = CherryRed,
                                strokeWidth = 3.dp,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }
                    is AsyncImagePainter.State.Error -> {
                        // Fallback if image fails
                        Icon(
                            imageVector = Icons.Default.BrokenImage,
                            contentDescription = "Error",
                            tint = Gray,
                            modifier = Modifier.size(48.dp)
                        )
                    }
                    else -> {
                        SubcomposeAsyncImageContent()
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(burger.name, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Black)
            Text(burger.type, color = Gray)

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = "Rating Star",
                    tint = Color.Yellow
                )
                Spacer(modifier = Modifier.width(4.dp)) // space between icon and text
                Text(
                    text = "${burger.rating}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("$${burger.price}", fontWeight = FontWeight.Bold, color = Black)

                IconButton(onClick = onFavoriteClick) {
                    Icon(
                        imageVector = if (burger.isFavorite) Icons.Filled.Favorite else Icons.Outlined.Favorite,
                        contentDescription = stringResource(R.string.favorites),
                        tint = if (burger.isFavorite) CherryRed else Gray,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }
    }
}

