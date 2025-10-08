package com.example.burgerapp.ui.presentation.chat_screen

data class Message(
    val id: String = "",
    val text: String = "",
    val senderId: String = "",
    val timestamp: Long = System.currentTimeMillis()
) {
    val isUser: Boolean
        get() = senderId != "BOT"
}
