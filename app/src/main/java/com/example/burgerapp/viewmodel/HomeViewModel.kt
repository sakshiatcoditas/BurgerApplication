package com.example.burgerapp.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.burgerapp.burger.Burger

import com.example.burgerapp.repository.BurgerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: BurgerRepository
) : ViewModel() {

    private val _burgers = MutableStateFlow<List<Burger>>(emptyList())
    val burgers: StateFlow<List<Burger>> get() = _burgers

    init {
        viewModelScope.launch {
            repository.getBurgers().collectLatest { burgerList ->
                _burgers.value = burgerList
            }
        }
    }
}
