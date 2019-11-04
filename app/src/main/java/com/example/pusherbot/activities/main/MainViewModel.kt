package com.example.pusherbot.activities.main

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.pusherbot.activities.base.BaseViewModel
import com.example.pusherbot.service.repository.ChatRepository
import com.pusher.chatkit.CurrentUser
import com.pusher.chatkit.messages.multipart.Message
import com.pusher.chatkit.rooms.Room

class MainViewModel: BaseViewModel() {

    private lateinit var chatRepository: ChatRepository

    fun initChatbotRepository(context: Context, roomId: String) {
        chatRepository = ChatRepository(context, roomId)
    }

    fun getRoom(): LiveData<Room> {
        return chatRepository.room
    }

    fun getMessages(): LiveData<List<Message>> {
        return chatRepository.messages
    }

    fun sendMessage(message: String) {
        chatRepository.sendMessage(message)
    }

    fun leaveRoom() {
        chatRepository.leaveRoom()
    }

    override fun onCleared() {
        super.onCleared()
        if (this::chatRepository.isInitialized)
            chatRepository.terminate()
    }
}