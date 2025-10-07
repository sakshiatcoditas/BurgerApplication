package com.example.burgerapp.ui.presentation.chat_screen

import androidx.compose.ui.res.stringResource
import com.example.burgerapp.R

import kotlin.collections.isNotEmpty
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Scaffold

import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.burgerapp.ui.theme.*
import com.example.burgerapp.viewmodel.ChatViewModel
import androidx.hilt.navigation.compose.hiltViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    navController: NavHostController,
    viewModel: ChatViewModel = hiltViewModel()
    // Hilt injects the ViewModel
) {
    var messageText by remember { mutableStateOf("") }
    val messages = viewModel.messages  // messages come from ViewModel
    val listState = rememberLazyListState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.chat_title), color = WhiteText) },

                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.navigate("Home") {
                                popUpTo("Home") { inclusive = true }
                                launchSingleTop = true
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back_button),
                            tint = WhiteText
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = CherryRed
                )
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(LightGray)
            ) {
                // Messages list
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp)
                ) {
                    items(messages) { msg ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = if (msg.isUser) Arrangement.End else Arrangement.Start
                        ) {
                            Text(
                                text = msg.text,
                                modifier = Modifier
                                    .background(
                                        color = if (msg.isUser) CherryRed else PurpleGrey80,
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    .padding(12.dp),
                                color = Black,
                                fontSize = 16.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                }

                // Auto-scroll to latest message
                LaunchedEffect(messages.size) {
                    if (messages.isNotEmpty()) {
                        listState.animateScrollToItem(messages.size - 1)
                    }
                }

                // Input row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextField(
                        value = messageText,
                        onValueChange = { messageText = it },
                        placeholder = {
                            Text(
                                stringResource(R.string.type_message),
                                color = PurpleGrey40
                            )
                        },
                        modifier = Modifier
                            .weight(1f)
                            .background(White, RoundedCornerShape(8.dp)),
                        textStyle = LocalTextStyle.current.copy(color =Black),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            viewModel.sendMessage(messageText)
                            messageText = ""
                        },
                        modifier = Modifier
                            .background(Red, shape = RoundedCornerShape(8.dp))
                    ) {
                        Text(stringResource(R.string.send)
                            , color = White)
                    }
                }
            }
        }
    )
}
