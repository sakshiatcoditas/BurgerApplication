package com.example.burgerapp.ui.ui

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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.burgerapp.burger.Burger
import coil.compose.AsyncImage

// --- HomeScreen ---



@Composable
fun HomeScreen(
    burgers: List<Burger>,
    categories: List<String>,
    navController: NavHostController // <-- added navController here
) {
    var searchText by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(categories.firstOrNull() ?: "All") }
    var selectedBottomItem by remember { mutableStateOf("Home") }

    // Filtered burgers
    val filteredBurgers = burgers.filter { burger ->
        (selectedCategory == "All" || burger.type.equals(selectedCategory, ignoreCase = true)) &&
                (searchText.isBlank() || burger.name.contains(searchText, ignoreCase = true))
    }

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = selectedBottomItem == "Home",
                    onClick = { selectedBottomItem = "Home" },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Home") }
                )
                NavigationBarItem(
                    selected = selectedBottomItem == "Favorites",
                    onClick = { selectedBottomItem = "Favorites" },
                    icon = { Icon(Icons.Default.Favorite, contentDescription = "Favorites") },
                    label = { Text("Favorites") }
                )
                NavigationBarItem(
                    selected = selectedBottomItem == "Profile",
                    onClick = {
                        selectedBottomItem = "Profile"
                        navController.navigate("Profile") { launchSingleTop = true } // <-- navigate to Profile
                    },
                    icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                    label = { Text("Profile") }
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Top Bar
            HomeTopBar(searchText = searchText, onSearchChange = { searchText = it })

            Spacer(modifier = Modifier.height(8.dp))

            // Chips Row
            CategoryChips(categories = categories, selectedCategory = selectedCategory) {
                selectedCategory = it
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Burger Grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filteredBurgers) { burger ->
                    BurgerCard(burger = burger)
                }
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
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Foodgo",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "Profile",
                modifier = Modifier.size(32.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = searchText,
                onValueChange = onSearchChange,
                placeholder = { Text("Search food") },
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(Color.Red, shape = RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Filter",
                    tint = Color.White
                )
            }
        }
    }
}

// --- Chips Row ---
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
            .height(50.dp)
            .horizontalScroll(scrollState)
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        categories.forEach { category ->
            Box(
                modifier = Modifier
                    .height(50.dp)
                    .padding(horizontal = 4.dp)
                    .border(
                        width = if (category == selectedCategory) 0.dp else 1.dp,
                        brush = SolidColor(Color.LightGray),
                        shape = RoundedCornerShape(20.dp)
                    )
                    .background(
                        color = if (category == selectedCategory) Color(0xFFEF2A39) else Color(0xFFF5F5F5),
                        shape = RoundedCornerShape(20.dp)
                    )
                    .clickable { onCategorySelected(category) }
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = category,
                    color = if (category == selectedCategory) Color.White else Color.Black,
                    fontSize = 16.sp
                )
            }
        }
    }
}

// --- BurgerCard ---
@Composable
fun BurgerCard(burger: Burger) {
    var isFavorite by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Box {
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
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(burger.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(burger.type, color = Color.Gray)
                Text("⭐ ${burger.rating}", fontSize = 14.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text("₹${burger.price}", fontWeight = FontWeight.Bold, color = Color(0xFFEF2A39))
            }

            IconToggleButton(
                checked = isFavorite,
                onCheckedChange = { isFavorite = it },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .size(48.dp)
            ) {
                Icon(
                    imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = "Favorite",
                    tint = if (isFavorite) Color(0xFFEF2A39) else Color.Gray,
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}

// --- Preview ---
@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    val dummyBurgers = listOf(
        Burger("1", "Cheese Burger", "Delicious cheesy burger", "", 120.0, 4.5, "Veg"),
        Burger("2", "Chicken Burger", "Juicy chicken patty", "", 150.0, 4.7, "Non-Veg"),
        Burger("3", "Paneer Burger", "Paneer with masala", "", 130.0, 4.3, "Veg"),
        Burger("4", "Double Patty Burger", "Big and heavy", "", 200.0, 4.8, "Non-Veg")
    )
    val dummyCategories = listOf("All", "Veg", "Non-Veg", "Combos", "Classic")

    // Just for preview, passing dummy NavController
    // You can remove this when using real NavController in your app
}
