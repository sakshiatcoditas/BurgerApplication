package com.example.burgerapp.ui.presentation.home_screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.burgerapp.R
import com.example.burgerapp.viewmodel.HomeViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.res.stringResource
import com.example.burgerapp.ui.presentation.favorite_screen.FavoriteScreen
import com.example.burgerapp.ui.presentation.home_screen.components.*
import com.example.burgerapp.viewmodel.FavoriteViewModel

@Composable
fun HomeScreen(
    navController: NavHostController,
    homeViewModel: HomeViewModel = hiltViewModel(),
    favoriteViewModel: FavoriteViewModel = hiltViewModel()

) {
    val uiState by homeViewModel.uiState.collectAsState()
    val filteredBurgers by homeViewModel.filteredBurgers.collectAsState()
    val isOnline by homeViewModel.isOnline.collectAsState()
    var selectedBottomItem by remember { mutableStateOf("Home") }
    var showFilterDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            HomeTopBar(
                navController = navController,
                userName = uiState.userName ?: "",   // <-- pass userName
                userPhotoUrl = uiState.userPhotoUrl
            )
        },
        bottomBar = {
            HomeBottomNavigationBar(
                selectedItem = selectedBottomItem,
                onItemSelected = { selectedBottomItem = it },
                navController = navController
            )
        }
    )
    { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            Spacer(modifier = Modifier.height(8.dp))

            // Search Bar + Filter
            HomeSearchBar(
                searchText = uiState.searchText,
                onSearchTextChange = { homeViewModel.onSearchTextChange(it) },
                onFilterClick = { showFilterDialog = true },
                isSearchNotFound = filteredBurgers.isEmpty() && uiState.searchText.isNotEmpty()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Category Chips
            CategoryChips(
                categories = uiState.categories,
                selectedCategory = uiState.selectedCategory
            ) { homeViewModel.onCategorySelected(it) }

            Spacer(modifier = Modifier.height(8.dp))

            // Filter Dialog
            if (showFilterDialog) {
                AlertDialog(
                    onDismissRequest = { showFilterDialog = false },
                    title = { Text(stringResource(id = R.string.filter_by)) },
                    text = {
                        Column {
                            listOf("Price", "Rating").forEach { option ->
                                Text(
                                    text = option,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            homeViewModel.onFilterSelected(option)
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


            // Main content
            if (selectedBottomItem == "Home") {
                if (!isOnline) {
                    // No internet placeholder
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            stringResource(id = R.string.no_internet_connection),
                            color = Red,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                } else if (filteredBurgers.isEmpty() && uiState.searchText.isNotEmpty()) {
                    // Search not found placeholder
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            stringResource(id = R.string.search_not_found),
                            color = Red,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                } else if (filteredBurgers.isEmpty()) {
                    // Shimmer/loader placeholder
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(top = 10.dp, bottom = 10.dp)
                    ) {
                        items(6) { ShimmerBurgerCard() }
                    }
                } else {
                    // Show actual burgers
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
                                burger = burger,
                                onFavoriteClick = { homeViewModel.toggleFavorite(burger) },
                                onClick = { navController.navigate("burgerDetail/${burger.burgerId}") }
                            )
                        }
                    }
                }
            } else if (selectedBottomItem == "Favorites") {
                FavoriteScreen(favoriteViewModel = favoriteViewModel)
            }
        }
    }
}
