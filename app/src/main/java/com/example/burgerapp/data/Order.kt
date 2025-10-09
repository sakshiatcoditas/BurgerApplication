package com.example.burgerapp.data


data class Order(
    val burgerName: String = "",
    val totalPrice: Double = 0.0,
    val toppings: List<String> = emptyList(),
    val sides: List<String> = emptyList(),
    val quantity: Int = 1,
    val timestamp: Long = 0L
)

