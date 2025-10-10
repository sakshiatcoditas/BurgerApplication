package com.example.burgerapp.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.burgerapp.data.Option
import com.example.burgerapp.data.Options
import com.example.burgerapp.repository.CustomRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CustomViewModel @Inject constructor(
    private val repository: CustomRepository
) : ViewModel() {

    // Selected toppings and sides
    val selectedToppings = mutableStateListOf<String>()
    val selectedSides = mutableStateListOf<String>()

    // Firebase-loaded options
    private val _options = MutableStateFlow(Options())
    val options: StateFlow<Options> = _options.asStateFlow()

    init {
        fetchOptions()
    }

    // Fetch options from repository
    private fun fetchOptions() {
        viewModelScope.launch {
            try {
                _options.value = repository.fetchOptions()
            } catch (e: Exception) {
                // Handle error (e.g., log or set empty options)
                _options.value = Options()
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

    // Compute final price
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
