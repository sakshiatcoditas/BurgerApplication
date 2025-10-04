package com.example.burgerapp.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.burgerapp.burger.Burger
import com.example.burgerapp.repository.BurgerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: BurgerRepository
) : ViewModel() {

    fun getBurger(burgerId: String): Flow<Burger?> {
        return repository.getBurgers().map { list -> list.find { it.burgerId == burgerId } }
            .stateIn(viewModelScope, SharingStarted.Lazily, null)
    }
}
