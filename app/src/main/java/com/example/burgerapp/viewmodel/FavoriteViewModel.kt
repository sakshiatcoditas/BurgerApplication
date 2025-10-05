package com.example.burgerapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.burgerapp.burger.Burger
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val database: FirebaseDatabase
) : ViewModel() {

    private val _favorites = MutableStateFlow<List<Burger>>(emptyList())
    val favorites: StateFlow<List<Burger>> get() = _favorites

    private var userRef: DatabaseReference? = null
    private var favoritesListener: ValueEventListener? = null

    init {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            userRef = database.reference.child("users").child(currentUser.uid).child("favorites")
            observeFavorites()
        }
    }

    private fun observeFavorites() {
        favoritesListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val favList = snapshot.children.mapNotNull { it.getValue(Burger::class.java) }
                _favorites.value = favList
            }

            override fun onCancelled(error: DatabaseError) {
                // Optional: handle errors
            }
        }
        userRef?.addValueEventListener(favoritesListener!!)
    }

    fun toggleFavorite(burger: Burger) {
        val uid = auth.currentUser?.uid ?: return
        val favRef = database.reference.child("users").child(uid).child("favorites").child(burger.burgerId)

        viewModelScope.launch {
            if (_favorites.value.any { it.burgerId == burger.burgerId }) {
                // Remove from favorites
                favRef.removeValue()
            } else {
                // Add to favorites
                favRef.setValue(burger)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        favoritesListener?.let { userRef?.removeEventListener(it) }
    }
}