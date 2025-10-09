package com.example.burgerapp.ui.presentation.payment_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val helper: PaymentPreferencesHelper
) : ViewModel() {


    private val _selectedCard = MutableStateFlow(0)
    val selectedCard: StateFlow<Int> get() = _selectedCard

    // --- new payment info state ---
    private val _deliveryFee = MutableStateFlow(0f)
    val deliveryFee: StateFlow<Float> get() = _deliveryFee

    private val _taxes = MutableStateFlow(0f)
    val taxes: StateFlow<Float> get() = _taxes

    init {
        // Load both card selection and Firebase data
        viewModelScope.launch {
            helper.selectedCard.collect { index ->
                _selectedCard.value = index
            }
        }

        fetchPaymentInfoFromFirebase()
    }

    fun selectCard(index: Int) {
        _selectedCard.value = index
        viewModelScope.launch {
            helper.saveSelectedCard(index)
        }
    }

    // new function to fetch payment info from Firebase
    private fun fetchPaymentInfoFromFirebase() {
        viewModelScope.launch {
            try {
                val dbRef = FirebaseDatabase.getInstance().getReference("paymentInfo")
                val snapshot = dbRef.get().await()

                val delivery = snapshot.child("deliveryFee").getValue(Float::class.java) ?: 0f
                val tax = snapshot.child("taxes").getValue(Float::class.java) ?: 0f

                _deliveryFee.value = delivery
                _taxes.value = tax

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    fun saveOrderToFirebase(
        burgerName: String,
        totalPrice: Float,
        toppings: List<String>,
        spiceLevel: Float,
        portion: Int
    ) {
        viewModelScope.launch {
            try {
                val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return@launch
                val ordersRef = FirebaseDatabase.getInstance()
                    .getReference("orders")
                    .child(userId)

                val orderId = ordersRef.push().key ?: return@launch

                val orderData = mapOf(
                    "orderId" to orderId,
                    "burgerName" to burgerName,
                    "toppings" to toppings,
                    "spiceLevel" to spiceLevel,
                    "portion" to portion,
                    "totalPrice" to totalPrice,
                    "timestamp" to System.currentTimeMillis()
                )

                ordersRef.child(orderId).setValue(orderData).await()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }




}