package com.example.burgerapp.repository

import com.example.burgerapp.data.Burger
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoriteRepository @Inject constructor(
    private val auth: FirebaseAuth
) {

    private val database = Firebase.database

    private fun getUserFavoritesRef(): DatabaseReference {
        val uid = auth.currentUser?.uid ?: throw Exception("User not logged in")
        return database.getReference("users/$uid/favorites")
    }

    // Fetch favorites in real-time
    fun getFavorites(): Flow<List<Burger>> = callbackFlow {
        val ref = getUserFavoritesRef()
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val favorites = snapshot.children.mapNotNull { it.getValue<Burger>() }
                trySend(favorites).isSuccess
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

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}
