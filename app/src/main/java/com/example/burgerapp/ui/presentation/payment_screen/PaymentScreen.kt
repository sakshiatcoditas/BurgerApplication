package com.example.burgerapp.ui.presentation.payment_screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.ui.res.stringResource

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.Black
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.font.FontWeight.Companion.SemiBold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

import com.example.burgerapp.R
import com.example.burgerapp.ui.theme.BlackText
import com.example.burgerapp.ui.theme.CherryRed
import com.example.burgerapp.ui.theme.Gray
import com.example.burgerapp.ui.theme.LightGray
import com.example.burgerapp.ui.theme.WhiteText
import com.example.burgerapp.ui.theme.faintgrey
import com.example.burgerapp.ui.theme.lightergray
import com.example.burgerapp.viewmodel.CustomViewModel


@Composable
fun PaymentScreen(
    burgerId: String,
    portion: Int,
    spiceLevel: Float,
    onBackClick: () -> Unit,
    paymentViewModel: PaymentViewModel = hiltViewModel(),
    customViewModel: CustomViewModel = hiltViewModel(),
    totalPrice: Float,
    onPaymentSuccess: () -> Unit
) {
    val selectedCardIndex by paymentViewModel.selectedCard.collectAsState()
    val deliveryFee by paymentViewModel.deliveryFee.collectAsState()
    val taxes by paymentViewModel.taxes.collectAsState()

    // Check if Firebase data has loaded
    val isLoading = deliveryFee == 0f && taxes == 0f

    if (isLoading) {
        // Show a loading indicator while fetching fees and taxes
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        // Calculate total once data is loaded
        val totalWithExtras = totalPrice + (totalPrice * taxes) + deliveryFee

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            IconButton(onClick = onBackClick) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.back)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Order Summary
            Text(
                modifier = Modifier.padding(start = 14.dp),
                text = "Order Summary",
                style = MaterialTheme.typography.titleLarge,
                color = BlackText,
                fontWeight = SemiBold
            )

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Order Total", color = Gray)
                Text("$${String.format("%.2f", totalPrice)}", color = BlackText, fontWeight = Bold)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Taxes", color = Gray)
                Text("$${String.format("%.2f", totalPrice * taxes)}", color = Gray)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Delivery Fees", color = Gray)
                Text("$${String.format("%.2f", deliveryFee)}", color = Gray)
            }

            Spacer(modifier = Modifier.height(12.dp))

            HorizontalDivider(
                Modifier.padding(horizontal = 24.dp),
                DividerDefaults.Thickness,
                color = Gray
            )

            Spacer(modifier = Modifier.height(26.dp))

            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Total:", color = BlackText, fontWeight = Bold)
                Text("$${String.format("%.2f", totalWithExtras)}", color = BlackText, fontWeight = Bold)
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Estimated delivery time:", color = BlackText, fontWeight = Bold)
                Text("20mins", color = BlackText, fontWeight = Bold)
            }

            Spacer(modifier = Modifier.height(34.dp))

            Column(modifier = Modifier.padding(start = 14.dp)) {
                Text(
                    text = "Payment methods",
                    color = BlackText,
                    fontWeight = SemiBold,
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.height(34.dp))

                // --- MasterCard ---
                PaymentCard(
                    cardName = "Credit card",
                    cardNumber = "5105 **** **** 0505",
                    cardImage = R.drawable.mastercard,
                    index = 0,
                    selectedIndex = selectedCardIndex,
                    onCardSelected = { paymentViewModel.selectCard(it) }
                )

                // --- Visa ---
                PaymentCard(
                    cardName = "Debit card",
                    cardNumber = "5105 **** **** 0505",
                    cardImage = R.drawable.visa,
                    index = 1,
                    selectedIndex = selectedCardIndex,
                    onCardSelected = { paymentViewModel.selectCard(it) }
                )

                Spacer(modifier = Modifier.height(40.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Total Price", color = Color.Black, fontSize = 14.sp)
                        Text("$${String.format("%.2f", totalWithExtras)}", color = Color.Black, fontWeight = Bold, fontSize = 16.sp)
                    }

                    Button(
                        onClick = {  onPaymentSuccess() },
                        modifier = Modifier
                            .size(width = 209.dp, height = 70.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                    ) {
                        Text("Pay Now", color = Color.White, fontWeight = Bold, fontSize = 16.sp)
                    }
                }

            }
        }
    }
}
@Composable
fun PaymentCard(
    cardName: String,
    cardNumber: String,
    cardImage: Int,
    index: Int,
    selectedIndex: Int,
    onCardSelected: (Int) -> Unit
) {
    val isSelected = selectedIndex == index
    val backgroundColor = if (isSelected) BlackText else lightergray
    val borderColor = if (isSelected) WhiteText else backgroundColor

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(bottom = 16.dp)
            .clickable { onCardSelected(index) },
        color = backgroundColor,
        shape = RoundedCornerShape(16.dp),
        shadowElevation = if (isSelected) 8.dp else 4.dp,
        border = BorderStroke(2.dp, borderColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = cardImage),
                contentDescription = cardName,
                modifier = Modifier.size(width = 70.dp, height = 42.38.dp)
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp)
            ) {
                Text(cardName, color = WhiteText, fontWeight = SemiBold)
                Text(cardNumber, color = faintgrey)
            }

            RadioButton(
                selected = isSelected,
                onClick = { onCardSelected(index) },
                colors = RadioButtonDefaults.colors(
                    selectedColor = WhiteText,
                    unselectedColor = BlackText
                )
            )
        }
    }
}
