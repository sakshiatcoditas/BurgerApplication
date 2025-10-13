package com.example.burgerapp.repository

import com.example.burgerapp.data.Burger
import com.google.firebase.database.ktx.getValue
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BurgerRepository @Inject constructor() {

    private val database = Firebase.database

    fun getBurgers(): Flow<List<Burger>> = callbackFlow {
        val ref = database.getReference("burgers")
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val burgers = snapshot.children.mapNotNull { it.getValue<Burger>() }
                trySend(burgers).isSuccess
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }

        ref.addValueEventListener(listener)
        awaitClose { ref.removeEventListener(listener) }
    }
}
