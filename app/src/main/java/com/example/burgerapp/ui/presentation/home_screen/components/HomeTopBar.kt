package com.example.burgerapp.ui.presentation.home_screen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.burgerapp.R
import com.example.burgerapp.ui.theme.LobsterFont
import com.example.burgerapp.ui.theme.Typography

@Composable
fun HomeTopBar(
    navController: NavHostController,
    userPhotoUrl: String?,
    userName: String?,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = stringResource(id = R.string.app_name),
                fontFamily = LobsterFont,
                fontWeight = FontWeight.W400,
                fontSize = 45.sp,
                lineHeight = 61.sp
            )
            Text(
                text = stringResource(id = R.string.order_your_favourite_food),
                style = Typography.bodyLarge,
                fontWeight = FontWeight.W500,
                fontSize = 18.sp,
                color = Gray
            )
        }

        UserAvatar(
            userName = userName,
            userPhotoUrl = userPhotoUrl,

            ) {
            navController.navigate("Profile")
        }
    }
}
