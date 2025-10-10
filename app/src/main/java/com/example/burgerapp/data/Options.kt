package com.example.burgerapp.data

data class Options(
    val toppings: Map<String, Option> = emptyMap(),
    val sides: Map<String, Option> = emptyMap()
)
