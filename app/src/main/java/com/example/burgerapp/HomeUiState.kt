package com.example.burgerapp

import com.example.burgerapp.data.Burger

data class HomeUiState(

    val burgers: List<Burger> = emptyList(),

    val categories: List<String> = listOf("All", "Veg", "NonVeg", "Classic","Combos") ,
    val searchText: String = "",
    val userName: String? = null,
    val selectedCategory: String = "All",
    val filterOption: String? = null,
    val favorites: List<Burger> = emptyList(),
    val userEmail: String? = null,
    val userPhotoUrl: String? = null
)
