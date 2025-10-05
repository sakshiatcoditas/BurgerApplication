package com.example.burgerapp.ui.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.burgerapp.burger.Burger
import com.example.burgerapp.viewmodel.FavoriteViewModel

@Composable
fun FavoriteScreen(
    favoriteViewModel: FavoriteViewModel
) {
    val favoriteBurgers by favoriteViewModel.favorites.collectAsState()

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(top = 10.dp, bottom = 10.dp)
    ) {
        items(favoriteBurgers) { burger ->
            BurgerCard(
                burger = burger,
                onFavoriteClick = { favoriteViewModel.removeFavorite(burger) }
            )
        }
    }
}
