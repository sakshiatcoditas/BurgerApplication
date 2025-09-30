package com.example.burgerapp.ui.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.burgerapp.viewmodel.FavoriteViewModel
import kotlin.collections.emptyList
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.grid.items
@Composable
fun FavoriteScreen(
    userEmail: String,
    favoriteViewModel: FavoriteViewModel = hiltViewModel()
) {
    val favorites by favoriteViewModel.favorites.collectAsState()
    val userFavorites = favorites[userEmail] ?: emptyList()

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(userFavorites) { burger ->
            BurgerCard(burger = burger, userEmail = userEmail)
        }
    }
}

