package com.example.burgerapp.ui.presentation.chat_screen


import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class ChatViewModel(
    private val repository: ChatRepository = ChatRepository()
) : ViewModel() {

    val messages = mutableStateListOf<Message>()

    init {
        repository.observeMessages { newMessages ->
            messages.clear()
            messages.addAll(newMessages)
        }
    }

    fun sendMessage(text: String) {
        val userMessage = Message(
            text = text,
            senderId = "USER"
        )
        repository.sendMessage(userMessage)


    }
}
