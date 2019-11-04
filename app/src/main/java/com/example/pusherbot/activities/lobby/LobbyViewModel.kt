package com.example.pusherbot.activities.lobby

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.pusherbot.activities.base.BaseViewModel
import com.example.pusherbot.service.repository.ChatRepository
import com.pusher.chatkit.rooms.Room

class LobbyViewModel: BaseViewModel() {

    private lateinit var chatRepository: ChatRepository

    fun initChatbotRepository(context: Context) {
        // TODO: Implement safety
        chatRepository = ChatRepository(context, user.firebaseUser.value!!.uid)
    }

    fun joinRoom(roomId: String) {
        chatRepository.attemptJoinRoom(roomId)
    }

    fun getRoom(): LiveData<Room> {
        return chatRepository.room
    }

    override fun onCleared() {
        super.onCleared()
        if (this::chatRepository.isInitialized)
            chatRepository.terminate()
    }

}