package com.example.burgerapp.ui.presentation.payment_screen

import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.foundation.Image
import androidx.compose.ui.res.stringResource
import com.example.burgerapp.ui.theme.CherryRed

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider

import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.font.FontWeight.Companion.SemiBold
import androidx.compose.ui.unit.dp

import com.example.burgerapp.R
import com.example.burgerapp.ui.theme.BlackText
import com.example.burgerapp.ui.theme.Gray
import com.example.burgerapp.ui.theme.LightGray


@Composable
fun PaymentScreen(
    burgerId: String,
    portion: Int,
    spiceLevel: Float,
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Back button
        IconButton(onClick = onBackClick) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.back))
        }

        Spacer(modifier = Modifier.height(16.dp))
//        Text(text = "Burger ID: $burgerId")
//        Text(text = "Portion: $portion")
//        Text(text = "Spice Level: $spiceLevel")

        Text(
            modifier = Modifier.fillMaxWidth()
                .padding(start = 14.dp),
            text = "Order Summary",
            style = MaterialTheme.typography.titleLarge,
            color = BlackText,
            fontWeight = SemiBold,


            )

        Spacer(modifier=Modifier.height(14.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp,end=16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Order Total",
                color = Gray
            )

            Text(
                text = "$100",
                color = Gray
            )
        }
        Spacer(modifier=Modifier.height(12.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp,end=16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Taxes",
                color = Gray
            )

            Text(
                text = "$12",
                color = Gray
            )
        }

        Spacer(modifier=Modifier.height(12.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp,end=16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Delivery Fees",
                color = Gray
            )

            Text(
                text = "$50",
                color = Gray
            )
        }

        Spacer(modifier=Modifier.height(12.dp))

        HorizontalDivider(Modifier.padding(start = 24.dp,end=16.dp), DividerDefaults.Thickness, color= Gray)

        Spacer(modifier = Modifier.height(26.dp))

        Row(modifier = Modifier.fillMaxWidth()
            .padding(start = 24.dp,end=16.dp),
            horizontalArrangement = Arrangement.SpaceBetween) {


            Text(
                text = "Total:",
                color = BlackText,
                fontWeight = Bold
            )

            Text(
                text = "$1000",
                color = BlackText, fontWeight = Bold
            )
        }
        Spacer(modifier = Modifier.height(14.dp))

        Row(modifier = Modifier.fillMaxWidth().padding(start=24.dp,end=16.dp),
            horizontalArrangement = Arrangement.SpaceBetween){

            Text(text="Estimated delivery time:",
                color=BlackText,
                fontWeight = Bold)

            Text(text="20mins",
                color=BlackText,
                fontWeight = Bold)
        }

        Spacer(modifier = Modifier.height(34.dp))

        Column( modifier = Modifier
            .fillMaxWidth()
            .padding(start = 14.dp)) {

            Text(text="Payment methods",
                color=BlackText,
                fontWeight = SemiBold,
                style = MaterialTheme.typography.titleLarge)

        }
    }
}
