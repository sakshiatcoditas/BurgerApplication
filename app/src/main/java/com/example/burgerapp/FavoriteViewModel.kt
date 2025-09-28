package com.example.burgerapp

import androidx.lifecycle.ViewModel
import com.example.burgerapp.burger.Burger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor() : ViewModel() {

    private val _favorites = MutableStateFlow<Map<String, List<Burger>>>(emptyMap())
    val favorites: StateFlow<Map<String, List<Burger>>> = _favorites

    // Toggle favorite
    fun toggleFavorite(userEmail: String, burger: Burger) {
        val currentList = _favorites.value[userEmail] ?: emptyList()
        val isAlreadyFavorite = currentList.any { it.burgerId == burger.burgerId }

        val updatedList = if (isAlreadyFavorite) {
            currentList.filter { it.burgerId != burger.burgerId }
        } else {
            currentList + burger.copy(isFavorite = true)
        }

        _favorites.value = _favorites.value.toMutableMap().apply {
            put(userEmail, updatedList)
        }
    }
}
