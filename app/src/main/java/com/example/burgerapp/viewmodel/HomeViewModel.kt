package com.example.burgerapp.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.burgerapp.HomeUiState
import com.example.burgerapp.data.Burger
import com.example.burgerapp.repository.BurgerRepository
import com.example.burgerapp.repository.FavoriteRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val burgerRepository: BurgerRepository,
    private val favoriteRepository: FavoriteRepository,
    private val app: Application // added to monitor network
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    // Filtered burgers for UI
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

    // --- Network connectivity state ---
    private val _isOnline = MutableStateFlow(true)
    val isOnline: StateFlow<Boolean> = _isOnline

    init {
        viewModelScope.launch {
            burgerRepository.getBurgers().collectLatest { burgerList ->
                _uiState.update { it.copy(burgers = burgerList) }
            }
        }

        viewModelScope.launch {
            favoriteRepository.getFavorites().collect { favList ->
                _uiState.update { it.copy(favorites = favList) }
            }
        }

        loadCurrentUser()
        monitorNetwork()
    }



    // --- Toggle favorite ---
    fun toggleFavorite(burger: Burger) {
        favoriteRepository.toggleFavorite(burger) // Firebase updated
    }

    // --- Search, filter, category ---
    fun onSearchTextChange(text: String) {
        _uiState.update { it.copy(searchText = text) }
    }

    fun onCategorySelected(category: String) {
        _uiState.update { it.copy(selectedCategory = category) }
    }

    fun onFilterSelected(filter: String) {
        _uiState.update { it.copy(filterOption = filter) }
    }

    // --- Load current user ---
    private fun loadCurrentUser() {
        val auth = FirebaseAuth.getInstance()
        val user = auth.currentUser ?: return

        val dbRef = FirebaseDatabase.getInstance().reference.child("users").child(user.uid)
        dbRef.get().addOnSuccessListener { snapshot ->
            val nameFromDB = snapshot.child("name").getValue(String::class.java)
            val email = user.email
            val photoUrl = user.photoUrl?.toString()

            _uiState.update { state ->
                state.copy(
                    userEmail = email,
                    userPhotoUrl = photoUrl,
                    userName = if (!nameFromDB.isNullOrBlank()) {
                        nameFromDB
                    } else {
                        email?.substringBefore("@") ?: "U"
                    }
                )
            }
        }
    }



    // --- Network monitoring ---
    private fun monitorNetwork() {
        val connectivityManager =
            app.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // Register network callback for modern Android versions
            connectivityManager.registerDefaultNetworkCallback(object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    _isOnline.value = true
                }

                override fun onLost(network: Network) {
                    _isOnline.value = false
                }
            })
        } else {
            // For older versions, check connectivity using NetworkCapabilities
            val network = connectivityManager.activeNetwork
            val capabilities = connectivityManager.getNetworkCapabilities(network)
            _isOnline.value = capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
        }
    }

}