package com.example.burgerapp.burger

data class Burger(
    val burgerId: String = "",
    val name: String = "",
    val description: String = "",
    val imageUrl: String = "",
    val price: Double = 0.0,
    val rating: Double = 0.0,
    val type: String = "" ,
    val isFavorite: Boolean = false// "veg" or "nonveg"
)