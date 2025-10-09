package com.example.burgerapp.ui.presentation.payment_screen



import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val helper: PaymentPreferencesHelper
) : ViewModel() {

    private val _selectedCard = MutableStateFlow(0)
    val selectedCard: StateFlow<Int> get() = _selectedCard

    init {
        viewModelScope.launch {
            helper.selectedCard.collect { index ->
                _selectedCard.value = index
            }
        }
    }

    fun selectCard(index: Int) {
        _selectedCard.value = index
        viewModelScope.launch {
            helper.saveSelectedCard(index)
        }
    }
}
