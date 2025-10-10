package com.example.burgerapp.ui.presentation.home_screen.components

import androidx.compose.material3.NavigationBar
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Red

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.navigation.NavHostController

import com.example.burgerapp.R

import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.burgerapp.ui.theme.CherryRed
import com.example.burgerapp.ui.theme.Gray
import com.example.burgerapp.ui.theme.searchbarcolor

@Composable
fun HomeSearchBar(
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    onFilterClick: () -> Unit,
    isSearchNotFound: Boolean
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
    ) {
        TextField(
            value = searchText,
            onValueChange = onSearchTextChange,
            placeholder = {
                Text(
                    text = if (isSearchNotFound) {
                        stringResource(id = R.string.search_not_found)
                    } else {
                        stringResource(id = R.string.search_food)
                    },
                    color = if (isSearchNotFound) Red else Gray
                )
            },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = stringResource(id = R.string.search_food)) },
            singleLine = true,
            modifier = Modifier
                .weight(1f)
                .height(52.dp),
            shape = RoundedCornerShape(26.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = searchbarcolor,
                unfocusedContainerColor = searchbarcolor,
                disabledContainerColor = searchbarcolor,
                focusedIndicatorColor = Transparent,
                unfocusedIndicatorColor = Transparent,
                disabledIndicatorColor = Transparent
            )
        )

        Box(
            modifier = Modifier
                .size(52.dp)
                .background(CherryRed, RoundedCornerShape(16.dp))
                .clickable { onFilterClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.filter_icon),
                contentDescription = stringResource(id = R.string.filter),
                tint = White,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
