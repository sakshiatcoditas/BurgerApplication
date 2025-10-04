package com.example.burgerapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.burgerapp.burger.Burger
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

    fun loadBurger(burgerId: String) {
        viewModelScope.launch {
            repository.getBurgers()
                .map { list -> list.find { it.burgerId == burgerId } }
                .distinctUntilChanged()
                .collect { _burger.value = it }
        }
    }
}
