package com.example.burgerapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.burgerapp.data.Order
import com.example.burgerapp.repository.OrderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val repository: OrderRepository
) : ViewModel() {

    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders: StateFlow<List<Order>> = _orders.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun fetchUserOrders(userId: String) {
        _isLoading.value = true
        viewModelScope.launch {
            repository.getUserOrders(userId).collect { orderList ->
                _orders.value = orderList
                _isLoading.value = false
            }
        }
    }
}