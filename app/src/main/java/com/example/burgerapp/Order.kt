package com.example.burgerapp

data class Order(
    val orderId: String = "",
    val userId: String = "",
    val burgerId: String = "",
    val spiceLevel: Float = 0.7f,
    val portion: Int = 1,
    val toppings: List<String> = emptyList(),
    val sides: List<String> = emptyList()
)

