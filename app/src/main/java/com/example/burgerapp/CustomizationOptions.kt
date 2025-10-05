package com.example.burgerapp


data class CustomizationOption(
    val price: Double = 0.0
)

data class CustomizationOptions(
    val toppings: Map<String, CustomizationOption> = emptyMap(),
    val sides: Map<String, CustomizationOption> = emptyMap()
)
