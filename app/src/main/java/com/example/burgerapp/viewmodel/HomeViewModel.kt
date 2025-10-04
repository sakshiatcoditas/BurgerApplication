package com.example.burgerapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.burgerapp.HomeUiState
import com.example.burgerapp.burger.Burger
import com.example.burgerapp.repository.BurgerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: BurgerRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    // Filtered burgers exposed for UI
    val filteredBurgers: StateFlow<List<Burger>> = uiState.map { state ->
        var list = state.burgers

        if (state.selectedCategory != "All") {
            list = list.filter { it.type.equals(state.selectedCategory, ignoreCase = true) }
        }

        if (state.searchText.isNotBlank()) {
            list = list.filter { it.name.contains(state.searchText, ignoreCase = true) }
        }

        list = when (state.filterOption) {
            "Price" -> list.sortedBy { it.price }
            "Rating" -> list.sortedByDescending { it.rating }
            else -> list
        }

        // Mark favorites
        list.map { it.copy(isFavorite = state.favorites.any { fav -> fav.burgerId == it.burgerId }) }
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    init {
        viewModelScope.launch {
            repository.getBurgers().collectLatest { burgerList ->
                _uiState.update { it.copy(burgers = burgerList) }
            }
        }
        loadCurrentUser()
    }

    // --- UI Events ---
    fun onSearchTextChange(text: String) {
        _uiState.update { it.copy(searchText = text) }
    }

    fun onCategorySelected(category: String) {
        _uiState.update { it.copy(selectedCategory = category) }
    }

    fun onFilterSelected(filter: String) {
        _uiState.update { it.copy(filterOption = filter) }
    }

    fun toggleFavorite(burger: Burger) {
        // Optional: implement actual favorite logic here (Firebase or local)
        val isCurrentlyFavorite = _uiState.value.favorites.any { it.burgerId == burger.burgerId }
        val updatedFavorites = if (isCurrentlyFavorite) {
            _uiState.value.favorites.filter { it.burgerId != burger.burgerId }
        } else {
            _uiState.value.favorites + burger
        }

        _uiState.update { it.copy(favorites = updatedFavorites) }
    }

    // Optional: set user info from repository
    fun setUserInfo(email: String?, photoUrl: String?) {
        _uiState.update { it.copy(userEmail = email, userPhotoUrl = photoUrl) }
    }

    private fun loadCurrentUser() {
        val user = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser
        _uiState.update {
            it.copy(
                userEmail = user?.displayName ?: user?.email ?: "U",
                userPhotoUrl = user?.photoUrl?.toString()
            )
        }
    }
}
