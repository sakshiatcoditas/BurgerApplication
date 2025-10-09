package com.example.burgerapp.repository


import com.example.burgerapp.data.Option
import com.example.burgerapp.data.Options
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CustomRepository @Inject constructor() {

    private val database = Firebase.database
    private val toppingsRef = database.getReference("customizationOptions/toppings")
    private val sidesRef = database.getReference("customizationOptions/sides")

    // Fetch toppings from Firebase
    suspend fun fetchToppings(): Map<String, Option> {
        val snapshot = toppingsRef.get().await()
        return snapshot.children.mapNotNull { child ->
            val name = child.key
            val option = child.getValue<Option>()
            if (name != null && option != null) name to option else null
        }.toMap()
    }

    // Fetch sides from Firebase
    suspend fun fetchSides(): Map<String, Option> {
        val snapshot = sidesRef.get().await()
        return snapshot.children.mapNotNull { child ->
            val name = child.key
            val option = child.getValue<Option>()
            if (name != null && option != null) name to option else null
        }.toMap()
    }

    // Combine toppings & sides into Options object
    suspend fun fetchOptions(): Options {
        val toppings = fetchToppings()
        val sides = fetchSides()
        return Options(toppings = toppings, sides = sides)
    }
}
