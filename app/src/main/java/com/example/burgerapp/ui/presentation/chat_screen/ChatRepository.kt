package com.example.burgerapp.ui.presentation.chat_screen



import androidx.compose.runtime.mutableStateListOf

import com.google.firebase.database.*

class ChatRepository {

    private val database = FirebaseDatabase.getInstance().getReference("chats/global_chat/messages")

    fun sendMessage(message: Message) {
        val key = database.push().key ?: return
        database.child(key).setValue(message)
    }

    fun observeMessages(onMessagesReceived: (List<Message>) -> Unit) {
        database.orderByChild("timestamp")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val messages = snapshot.children.mapNotNull { it.getValue(Message::class.java) }
                    onMessagesReceived(messages)
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }
}
