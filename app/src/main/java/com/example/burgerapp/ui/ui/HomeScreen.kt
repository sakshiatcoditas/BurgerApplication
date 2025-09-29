package com.example.burgerapp.ui.ui


import androidx.compose.ui.res.painterResource
import com.example.burgerapp.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.burgerapp.burger.Burger
import com.example.burgerapp.ui.theme.CherryRed
import com.example.burgerapp.ui.theme.LobsterFont
import com.example.burgerapp.ui.theme.Typography
import com.example.burgerapp.viewmodel.FavoriteViewModel

// --- HomeScreen ---
@Composable
fun HomeScreen(
    burgers: List<Burger>,
    categories: List<String>,
    navController: NavHostController,
    userEmail: String, // logged-in user email
    //instead of all of these parameteres pass the viewmodel that has all of these
    // all the logic part never in the UI
    //All the strings remove the hardcoding please
) {
    var searchText by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(categories.firstOrNull() ?: "All") }
    var selectedBottomItem by remember { mutableStateOf("Home") }

    val filteredBurgers = burgers.filter { burger ->
        val typeNormalized = burger.type.lowercase()
        val categoryNormalized = selectedCategory.replace("-", "").lowercase()
        (selectedCategory == "All" || typeNormalized == categoryNormalized) &&
                (searchText.isBlank() || burger.name.contains(searchText, ignoreCase = true))
    }

    Scaffold(
        bottomBar = {
            val navBarItemColors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                unselectedIconColor = Color.White.copy(alpha = 0.7f),
                selectedTextColor = Color.White,
                unselectedTextColor = Color.White.copy(alpha = 0.7f),
                indicatorColor = Color.Transparent // removes gray indicator background
            )

            NavigationBar(
                containerColor = Color(0xFFEF2A39), // 🔴 Red background
                modifier = Modifier.height(56.dp)   // compact nav bar height
            ) {
                val navBarItemColors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White,
                    unselectedIconColor = Color.White.copy(alpha = 0.7f),
                    selectedTextColor = Color.White,
                    unselectedTextColor = Color.White.copy(alpha = 0.7f),
                    indicatorColor = Color.Transparent
                )

                NavigationBarItem(
                    selected = selectedBottomItem == "Home",
                    onClick = { selectedBottomItem = "Home" },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home", modifier = Modifier.size(22.dp)) },
                    label = { Text("Home", fontSize = 12.sp) },
                    colors = navBarItemColors
                )
                NavigationBarItem(
                    selected = selectedBottomItem == "Favorites",
                    onClick = { selectedBottomItem = "Favorites" },
                    icon = { Icon(Icons.Default.Favorite, contentDescription = "Favorites", modifier = Modifier.size(22.dp)) },
                    label = { Text("Favorites", fontSize = 12.sp) },
                    colors = navBarItemColors
                )
                NavigationBarItem(
                    selected = selectedBottomItem == "Profile",
                    onClick = {
                        selectedBottomItem = "Profile"
                        navController.navigate("Profile") { launchSingleTop = true }
                    },
                    icon = { Icon(Icons.Default.Person, contentDescription = "Profile", modifier = Modifier.size(22.dp)) },
                    label = { Text("Profile", fontSize = 12.sp) },
                    colors = navBarItemColors
                )
            }

        }
    )
    { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            HomeTopBar(searchText = searchText, onSearchChange = { searchText = it })
            Spacer(modifier = Modifier.height(8.dp))
            CategoryChips(categories = categories, selectedCategory = selectedCategory) { selectedCategory = it }
            Spacer(modifier = Modifier.height(8.dp))

            if (selectedBottomItem == "Home") {

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(
                        top = 10.dp , // add chips height + spacing
                        bottom = 10.dp // space for bottom nav
                    )
                ) {
                    items(filteredBurgers) { burger ->
                        BurgerCard(burger = burger, userEmail = userEmail)
                    }
                }


            } else if (selectedBottomItem == "Favorites") {
                FavoriteScreen(userEmail = userEmail)
            }
        }
    }
}

// --- Top Bar ---
@Composable
fun HomeTopBar(searchText: String, onSearchChange: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                // --- App Name ---
                Text(
                    text = "Foodgo",
                    fontFamily = LobsterFont,          // Lobster font
                    fontWeight = FontWeight.W400,
                    fontSize = 45.sp,                  // 45px
                    lineHeight = 45.sp * 1.35f,        // 135% line height
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(4.dp)) // small spacing

                // --- Subtitle ---
                Text(
                    text = "Order your favourite food!",
                    style = Typography.bodyLarge, // Poppins Medium
                    fontWeight = FontWeight.W500,
                    fontSize = 18.sp,               // 18px
                    lineHeight = 18.sp,             // 100% line height
                    letterSpacing = 0.sp,           // 0%
                    color = Color(0xFF6A6A6A)       // gray #6A6A6A
                )
            }

            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "Profile",
                modifier = Modifier.size(32.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))


        // 🔍 Rounded Search Bar with Filter Icon
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Search Bar
            TextField(
                value = searchText,
                onValueChange = onSearchChange,
                placeholder = { Text("Search food") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search"
                    )
                },
                singleLine = true,
                modifier = Modifier
                    .weight(1f) // takes all available width except filter icon
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

            Spacer(modifier = Modifier.width(12.dp))

            // Filter Icon Box
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .background(Color(0xFFEF2A39), shape = RoundedCornerShape(16.dp))
                    .clickable { /* TODO: handle filter click */ },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Menu, // your filter icon
                    contentDescription = "Filter",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
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


// --- BurgerCard ---
@Composable
fun BurgerCard(
    burger: Burger,
    userEmail: String,
    favoriteViewModel: FavoriteViewModel = hiltViewModel()
) {
    val favorites by favoriteViewModel.favorites.collectAsState()
    val isFavorite = favorites[userEmail]?.any { it.burgerId == burger.burgerId } ?: burger.isFavorite

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
            // Image
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

            // Name, Type, Rating
            Text(burger.name, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.Black)
            Text(burger.type, color = Color.Gray)
            Text("⭐ ${burger.rating}", fontSize = 14.sp, color=Color.Black)
            Spacer(modifier = Modifier.height(4.dp))

            // Price + Favorite Icon Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("₹${burger.price}", fontWeight = FontWeight.Bold, color = Color.Black)

                IconToggleButton(
                    checked = isFavorite,
                    onCheckedChange = {
                        favoriteViewModel.toggleFavorite(userEmail, burger)
                    }
                ) {
                    Icon(
                        painter = painterResource(
                            id = if (isFavorite) R.drawable.heart else R.drawable.heart
                        ), // Use a different drawable for outline if you have one
                        contentDescription = "Favorite",
                        tint = if (isFavorite) Color(0xFFEF2A39) else Color.Gray,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }
    }
}


// --- FavoriteScreen ---

// --- Preview ---
@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    val dummyBurgers = listOf(
        Burger("1", "Cheese Burger", "Delicious cheesy burger", "", 120.0, 4.5, "Veg"),
        Burger("2", "Chicken Burger", "Juicy chicken patty", "", 150.0, 4.7, "Non-Veg")
    )
    val dummyCategories = listOf("All", "Veg", "Non-Veg")
    val dummyNavController = rememberNavController()

    HomeScreen(
        burgers = dummyBurgers,
        categories = dummyCategories,
        navController = dummyNavController,
        userEmail = "preview@example.com"
    )
}
