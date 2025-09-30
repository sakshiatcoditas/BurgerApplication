package com.example.burgerapp.ui.ui

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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.burgerapp.R
import com.example.burgerapp.burger.Burger
import com.example.burgerapp.ui.theme.LobsterFont
import com.example.burgerapp.ui.theme.Typography
import com.example.burgerapp.viewmodel.FavoriteViewModel
import androidx.compose.runtime.collectAsState

// --- HomeScreen ---
@Composable
fun HomeScreen(
    burgers: List<Burger>,
    categories: List<String>,
    navController: NavHostController,
    userPhotoUrl: String?,
    userEmail: String,
    favoriteViewModel: FavoriteViewModel = hiltViewModel()
) {
    var searchText by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(categories.firstOrNull() ?: "All") }
    var selectedBottomItem by remember { mutableStateOf("Home") }

    var filterOption by remember { mutableStateOf<String?>(null) }
    var showFilterDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            HomeTopBar(
                navController = navController,
                userEmail = userEmail,
                userPhotoUrl = userPhotoUrl
            )
        },
        bottomBar = {
            val navBarItemColors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                unselectedIconColor = Color.White.copy(alpha = 0.7f),
                selectedTextColor = Color.White,
                unselectedTextColor = Color.White.copy(alpha = 0.7f),
                indicatorColor = Color.Transparent
            )

            NavigationBar(
                containerColor = Color(0xFFEF2A39),
                modifier = Modifier.height(56.dp)
            ) {
                NavigationBarItem(
                    selected = selectedBottomItem == "Home",
                    onClick = { selectedBottomItem = "Home" },
                    icon = { Icon(painter = androidx.compose.ui.res.painterResource(R.drawable.home_icon),
                        contentDescription = "Home",
                        modifier = Modifier.size(22.dp)) },
                    label = { Text("Home", fontSize = 12.sp) },
                    colors = navBarItemColors
                )

                NavigationBarItem(
                    selected = selectedBottomItem == "Favorites",
                    onClick = { selectedBottomItem = "Favorites" },
                    icon = { Icon(painter = androidx.compose.ui.res.painterResource(R.drawable.heart),
                        contentDescription = "Favorite",
                        modifier = Modifier.size(22.dp)) },
                    label = { Text("Favorites", fontSize = 12.sp) },
                    colors = navBarItemColors
                )

                NavigationBarItem(
                    selected = selectedBottomItem == "Profile",
                    onClick = {
                        selectedBottomItem = "Profile"
                        navController.navigate("Profile") { launchSingleTop = true }
                    },
                    icon = { Icon(painter = androidx.compose.ui.res.painterResource(R.drawable.user_icon),
                        contentDescription = "Profile",
                        modifier = Modifier.size(22.dp)) },
                    label = { Text("Profile", fontSize = 12.sp) },
                    colors = navBarItemColors
                )

                NavigationBarItem(
                    selected = selectedBottomItem == "Chat",
                    onClick = {
                        selectedBottomItem = "Chat"
                        navController.navigate("Chat") { launchSingleTop = true }
                    },
                    icon = { Icon(painter = androidx.compose.ui.res.painterResource(R.drawable.chat_icon),
                        contentDescription = "Chat",
                        modifier = Modifier.size(22.dp)) },
                    label = { Text("Chat", fontSize = 12.sp) },
                    colors = navBarItemColors
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // --- Search Bar + Filter Icon ---
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
            ) {
                TextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    placeholder = { Text("Search food") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                    singleLine = true,
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp),
                    shape = RoundedCornerShape(26.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFFF3F4F6),
                        unfocusedContainerColor = Color(0xFFF3F4F6),
                        disabledContainerColor = Color(0xFFF3F4F6),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    )
                )

                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .background(Color(0xFFEF2A39), RoundedCornerShape(16.dp))
                        .clickable { showFilterDialog = true },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = androidx.compose.ui.res.painterResource(id = R.drawable.filter_icon),
                        contentDescription = "Filter",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // --- Category Chips ---
            CategoryChips(categories = categories, selectedCategory = selectedCategory) { selectedCategory = it }

            Spacer(modifier = Modifier.height(8.dp))

            // --- Filter Dialog ---
            if (showFilterDialog) {
                AlertDialog(
                    onDismissRequest = { showFilterDialog = false },
                    title = { Text("Filter by") },
                    text = {
                        Column {
                            listOf("Price", "Rating").forEach { option ->
                                Text(
                                    text = option,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            filterOption = option
                                            showFilterDialog = false
                                        }
                                        .padding(8.dp),
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    },
                    confirmButton = {}
                )
            }

            // --- Filtered Burgers ---
            var filteredBurgers = burgers.filter { burger ->
                val typeNormalized = burger.type.lowercase()
                val categoryNormalized = selectedCategory.replace("-", "").lowercase()
                (selectedCategory == "All" || typeNormalized == categoryNormalized) &&
                        (searchText.isBlank() || burger.name.contains(searchText, ignoreCase = true))
            }

            filteredBurgers = when (filterOption) {
                "Price" -> filteredBurgers.sortedBy { it.price }
                "Rating" -> filteredBurgers.sortedByDescending { it.rating }
                else -> filteredBurgers
            }

            // --- Main content ---
            if (selectedBottomItem == "Home") {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(top = 10.dp, bottom = 10.dp)
                ) {
                    items(filteredBurgers) { burger ->
                        BurgerCard(
                            burger = burger.copy(
                                isFavorite = favoriteViewModel.favorites.collectAsState().value.any { it.burgerId == burger.burgerId }
                            ),
                            onFavoriteClick = { favoriteViewModel.toggleFavorite(burger) }
                        )
                    }
                }
            } else if (selectedBottomItem == "Favorites") {
                FavoriteScreen(favoriteViewModel = favoriteViewModel)
            }
        }
    }
}

// --- BurgerCard ---
@Composable
fun BurgerCard(
    burger: Burger,
    onFavoriteClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            AsyncImage(
                model = burger.imageUrl.ifEmpty { "https://via.placeholder.com/150" },
                contentDescription = burger.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(16.dp))
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(burger.name, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.Black)
            Text(burger.type, color = Color.Gray)
            Text("⭐ ${burger.rating}", fontSize = 14.sp, color = Color.Black)
            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("$${burger.price}", fontWeight = FontWeight.Bold, color = Color.Black)

                IconButton(
                    onClick = onFavoriteClick
                ) {
                    Icon(
                        imageVector = if (burger.isFavorite) Icons.Filled.Favorite else Icons.Outlined.Favorite,
                        contentDescription = "Favorite",
                        tint = if (burger.isFavorite) Color(0xFFEF2A39) else Color.Gray,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun HomeTopBar(
    navController: NavHostController,
    userEmail: String?,
    userPhotoUrl: String?
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = "Foodgo",
                fontFamily = LobsterFont,
                fontWeight = FontWeight.W400,
                fontSize = 45.sp,
                lineHeight = 61.sp
            )
            Text(
                text = "Order your favourite food!",
                style = Typography.bodyLarge,
                fontWeight = FontWeight.W500,
                fontSize = 18.sp,
                color = Color(0xFF6A6A6A)
            )
        }

        UserAvatar(
            userEmail = userEmail,
            userPhotoUrl = userPhotoUrl,

            ) {
            navController.navigate("Profile")
        }
    }
}


@Composable
fun UserAvatar(
    userEmail: String?,           // user's email
    userPhotoUrl: String?,        // user's Google profile photo URL
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primary)
            .shadow(6.dp, CircleShape)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        if (!userPhotoUrl.isNullOrEmpty()) {
            // Show Google profile photo
            AsyncImage(
                model = userPhotoUrl,
                contentDescription = "Profile",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else if (!userEmail.isNullOrEmpty()) {
            // Show initial from email
            val initial = userEmail.trim().substringBefore("@").firstOrNull()?.uppercase() ?: "U"
            Text(
                text = initial,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        } else {
            // Default placeholder
            Text(
                text = "U",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        }
    }
}



// --- Category Chips ---
@Composable
fun CategoryChips(
    categories: List<String>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit
) {
    val scrollState = rememberScrollState()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(scrollState)
            .padding(horizontal = 8.dp, vertical = 8.dp), // top & bottom spacing
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        categories.forEach { category ->
            Surface(
                modifier = Modifier.height(45.dp), // standard chip height
                color = if (category == selectedCategory) Color(0xFFEF2A39) else Color(0xFFF3F4F6),
                shape = RoundedCornerShape(18.dp), // pill shape
                shadowElevation = 2.dp // subtle elevation for depth
            ) {
                Box(
                    modifier = Modifier
                        .clickable { onCategorySelected(category) }
                        .padding(horizontal = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = category,
                        color = if (category == selectedCategory) Color.White else Color.Black,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}
