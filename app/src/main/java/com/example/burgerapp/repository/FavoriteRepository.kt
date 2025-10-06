package com.example.burgerapp.repository

import com.example.burgerapp.data.Burger
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoriteRepository @Inject constructor(
    private val database: FirebaseDatabase,
    private val auth: FirebaseAuth
) {

    private fun getUserFavoritesRef(): DatabaseReference {
        val uid = auth.currentUser?.uid ?: throw Exception("User not logged in")
        return database.reference.child("users").child(uid).child("favorites")
    }

    // Fetch favorites in real-time
    fun getFavorites(): Flow<List<Burger>> = callbackFlow {
        val ref = getUserFavoritesRef()
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val favorites = snapshot.children.mapNotNull { it.getValue(Burger::class.java) }
                trySend(favorites)
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }
        ref.addValueEventListener(listener)
        awaitClose { ref.removeEventListener(listener) }
    }

    // Toggle favorite in DB
    fun toggleFavorite(burger: Burger) {
        val ref = getUserFavoritesRef().child(burger.burgerId)
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    ref.removeValue() // Remove from favorites
                } else {
                    ref.setValue(burger) // Add to favorites
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }
}
