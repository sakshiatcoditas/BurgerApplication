package com.example.burgerapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.burgerapp.burger.Burger
import com.example.burgerapp.repository.FavoriteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val repository: FavoriteRepository
) : ViewModel() {

    val favorites: StateFlow<List<Burger>> =
        repository.getFavorites().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun removeFavorite(burger: Burger) {
        repository.toggleFavorite(burger) // toggles in Firebase
    }
}
