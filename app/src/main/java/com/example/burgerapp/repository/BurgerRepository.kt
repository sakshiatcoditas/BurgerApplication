package com.example.burgerapp.repository

import com.example.burgerapp.CustomizationOptions
import com.example.burgerapp.data.Burger

import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import javax.inject.Singleton
import com.example.burgerapp.CustomizationOption

@Singleton
class BurgerRepository @Inject constructor(
    private val database: FirebaseDatabase
) {

    fun getBurgers(): Flow<List<Burger>> = callbackFlow {
        val ref = database.reference.child("burgers")
        val listener = object : com.google.firebase.database.ValueEventListener {
            override fun onDataChange(snapshot: com.google.firebase.database.DataSnapshot) {
                val burgers = snapshot.children.mapNotNull { it.getValue(Burger::class.java) }
                trySend(burgers)
            }

            override fun onCancelled(error: com.google.firebase.database.DatabaseError) {
                close(error.toException())
            }
        }
        ref.addValueEventListener(listener)
        awaitClose { ref.removeEventListener(listener) }
    }

    fun getCustomizationOptions(): Flow<CustomizationOptions> = callbackFlow {
        val ref = database.reference.child("customizationOptions")
        val listener = object : com.google.firebase.database.ValueEventListener {
            override fun onDataChange(snapshot: com.google.firebase.database.DataSnapshot) {
                val toppings = snapshot.child("toppings").children.associate {
                    it.key!! to it.getValue(CustomizationOption::class.java)!!
                }
                val sides = snapshot.child("sides").children.associate {
                    it.key!! to it.getValue(CustomizationOption::class.java)!!
                }
                trySend(CustomizationOptions(toppings, sides))
            }

            override fun onCancelled(error: com.google.firebase.database.DatabaseError) {
                close(error.toException())
            }
        }
        ref.addValueEventListener(listener)
        awaitClose { ref.removeEventListener(listener) }
    }
}
