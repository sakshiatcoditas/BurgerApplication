package com.example.burgerapp.ui.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.burgerapp.viewmodel.FavoriteViewModel

@Composable
fun FavoriteScreen(
    favoriteViewModel: FavoriteViewModel = hiltViewModel()
) {
    val favorites by favoriteViewModel.favorites.collectAsState()

    if (favorites.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("No favorites yet", style = MaterialTheme.typography.bodyLarge)
        }
    } else {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(top = 10.dp, bottom = 10.dp)
        ) {
            items(favorites) { burger ->
                BurgerCard(
                    burger = burger.copy(isFavorite = true),
                    onFavoriteClick = { favoriteViewModel.toggleFavorite(burger) }
                )
            }
        }
    }
}
