package com.example.burgerapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.burgerapp.data.Order
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor() : ViewModel() {

    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders: StateFlow<List<Order>> = _orders.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val database = FirebaseDatabase.getInstance().getReference("orders")

    fun fetchUserOrders(userId: String) {
        _isLoading.value = true

        database.child(userId).addValueEventListener(object : com.google.firebase.database.ValueEventListener {
            override fun onDataChange(snapshot: com.google.firebase.database.DataSnapshot) {
                val orderList = snapshot.children.mapNotNull { it.getValue(Order::class.java) }
                _orders.value = orderList.sortedByDescending { it.timestamp }
                _isLoading.value = false
            }

            override fun onCancelled(error: com.google.firebase.database.DatabaseError) {
                _isLoading.value = false
            }
        })
    }


}