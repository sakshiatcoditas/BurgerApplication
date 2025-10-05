package com.example.burgerapp.repository

import com.example.burgerapp.Order
import com.google.firebase.database.FirebaseDatabase
import javax.inject.Inject

class OrderRepository @Inject constructor() {

    private val database = FirebaseDatabase.getInstance().reference

    suspend fun placeOrder(order: Order): Result<Unit> {
        return try {
            val userOrderRef = database.child("orders")
                .child(order.userId)
                .push() // generate unique order id
            userOrderRef.setValue(order)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
