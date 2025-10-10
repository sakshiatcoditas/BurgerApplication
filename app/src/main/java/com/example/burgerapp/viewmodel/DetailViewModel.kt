package com.example.burgerapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.burgerapp.data.Burger
import com.example.burgerapp.repository.BurgerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: BurgerRepository
) : ViewModel() {

    private val _burger = MutableStateFlow<Burger?>(null)
    val burger: StateFlow<Burger?> = _burger.asStateFlow()

    // Persist portion & spiceLevel
    var portion by mutableIntStateOf(1)
    var spiceLevel by mutableFloatStateOf(0.5f)

    fun loadBurger(burgerId: String) {
        viewModelScope.launch {
            repository.getBurgers()
                .map { list -> list.find { it.burgerId == burgerId } }
                .distinctUntilChanged()
                .collect { _burger.value = it }
        }
    }
}
