package com.example.burgerapp.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

// Price option (used for toppings & sides)
data class Option(
    val price: Double = 0.0
)

// Full set of options
data class Options(
    val toppings: Map<String, Option> = emptyMap(),
    val sides: Map<String, Option> = emptyMap()
)

@HiltViewModel
class CustomViewModel @Inject constructor() : ViewModel() {

    // Selections (live reactive lists)
    val selectedToppings = mutableStateListOf<String>()
    val selectedSides = mutableStateListOf<String>()

    // Firebase-loaded options with prices
    private val _options = MutableStateFlow(Options())
    val options: StateFlow<Options> = _options.asStateFlow()

    private val database = FirebaseDatabase.getInstance()
    private val toppingsRef = database.getReference("customizationOptions/toppings")
    private val sidesRef = database.getReference("customizationOptions/sides")

    init {
        fetchOptionsFromFirebase()
    }

    private fun fetchOptionsFromFirebase() {
        toppingsRef.get().addOnSuccessListener { snapshot ->
            val toppingsMap = snapshot.children.mapNotNull { child ->
                val name = child.key
                val option = child.getValue(Option::class.java)
                if (name != null && option != null) name to option else null
            }.toMap()

            sidesRef.get().addOnSuccessListener { sidesSnapshot ->
                val sidesMap = sidesSnapshot.children.mapNotNull { child ->
                    val name = child.key
                    val option = child.getValue(Option::class.java)
                    if (name != null && option != null) name to option else null
                }.toMap()

                _options.value = Options(toppings = toppingsMap, sides = sidesMap)
            }
        }
    }


    // Toggle selection
    fun toggleTopping(name: String) {
        if (selectedToppings.contains(name)) selectedToppings.remove(name)
        else selectedToppings.add(name)
    }

    fun toggleSide(name: String) {
        if (selectedSides.contains(name)) selectedSides.remove(name)
        else selectedSides.add(name)
    }

    // Compute total price
    fun getFinalPrice(basePrice: Double, portion: Int): Double {
        val toppingsPrice = selectedToppings.sumOf { topping ->
            _options.value.toppings[topping]?.price ?: 0.0
        }
        val sidesPrice = selectedSides.sumOf { side ->
            _options.value.sides[side]?.price ?: 0.0
        }
        return (basePrice + toppingsPrice + sidesPrice) * portion
    }
}
