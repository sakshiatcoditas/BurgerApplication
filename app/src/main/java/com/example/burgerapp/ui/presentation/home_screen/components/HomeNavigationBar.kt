package com.example.burgerapp.ui.presentation.home_screen.components

import androidx.compose.material3.NavigationBar
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.navigation.NavHostController

import com.example.burgerapp.R

import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource

import com.example.burgerapp.ui.theme.CherryRed


@Composable
fun HomeBottomNavigationBar(
    selectedItem: String,
    onItemSelected: (String) -> Unit,
    navController: NavHostController
) {
    val navBarItemColors = NavigationBarItemDefaults.colors(
        selectedIconColor = White,
        unselectedIconColor = White.copy(alpha = 0.7f),
        selectedTextColor = White,
        unselectedTextColor = White.copy(alpha = 0.7f),
        indicatorColor = Transparent
    )

    NavigationBar(
        containerColor = CherryRed,
        modifier = Modifier.height(56.dp)
    ) {
        NavigationBarItem(
            selected = selectedItem == "Home",
            onClick = { onItemSelected("Home") },
            icon = { Icon(painter = painterResource(R.drawable.home_icon), contentDescription = stringResource(R.string.home), modifier = Modifier.size(22.dp)) },
            label = { Text(stringResource(R.string.home), fontSize = 12.sp) },
            colors = navBarItemColors
        )

        NavigationBarItem(
            selected = selectedItem == "Favorites",
            onClick = { onItemSelected("Favorites") },
            icon = { Icon(painter = painterResource(R.drawable.heart), contentDescription = stringResource(R.string.favorites), modifier = Modifier.size(22.dp)) },
            label = { Text(stringResource(R.string.favorites), fontSize = 12.sp) },
            colors = navBarItemColors
        )

        NavigationBarItem(
            selected = selectedItem == "Chat",
            onClick = {
                onItemSelected("Chat")
                navController.navigate("Chat") { launchSingleTop = true }
            },
            icon = { Icon(painter = painterResource(R.drawable.chat_icon), contentDescription = stringResource(R.string.chat), modifier = Modifier.size(22.dp)) },
            label = { Text(stringResource(R.string.chat), fontSize = 12.sp) },
            colors = navBarItemColors
        )

        NavigationBarItem(
            selected = selectedItem == "Profile",
            onClick = {
                onItemSelected("Profile")
                navController.navigate("Profile") { launchSingleTop = true }
            },
            icon = { Icon(painter = painterResource(R.drawable.user_icon), contentDescription = stringResource(R.string.profile), modifier = Modifier.size(22.dp)) },
            label = { Text(stringResource(R.string.profile), fontSize = 12.sp) },
            colors = navBarItemColors
        )
    }
}
