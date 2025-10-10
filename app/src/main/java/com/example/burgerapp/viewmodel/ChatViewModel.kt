package com.example.burgerapp.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

import com.example.burgerapp.data.ChatMessage


@HiltViewModel
class ChatViewModel @Inject constructor() : ViewModel() {
    private val _messages = mutableStateListOf<ChatMessage>()
    val messages: List<ChatMessage> get() = _messages

    fun sendMessage(text: String) {
        if (text.isNotBlank()) {
            _messages.add(ChatMessage(text, isUser = true))
            _messages.add(ChatMessage("Demo reply!", isUser = false))
        }
    }
}