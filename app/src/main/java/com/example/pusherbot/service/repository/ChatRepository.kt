package com.example.pusherbot.service.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.pusherbot.ApiConstants
import com.example.pusherbot.Helper
import com.pusher.chatkit.AndroidChatkitDependencies
import com.pusher.chatkit.ChatManager
import com.pusher.chatkit.ChatkitTokenProvider
import com.pusher.chatkit.CurrentUser
import com.pusher.chatkit.messages.multipart.Message
import com.pusher.chatkit.rooms.Room
import com.pusher.chatkit.rooms.RoomListeners
import com.pusher.chatkit.subscription.ChatkitSubscription
import com.pusher.client.Pusher
import com.pusher.util.Result
import elements.Error

class ChatRepository(context: Context, val roomId: String?) {

    constructor(context: Context): this(context, null)

    var room = MutableLiveData<Room>()
    var chatkitUser = MutableLiveData<CurrentUser>()
    var messages = MutableLiveData<List<Message>>(emptyList())

    private val chatManager = ChatManager(
        instanceLocator = ApiConstants.CHATKIT_INSTANCE_LOCATOR,
        userId = "john",
        dependencies = AndroidChatkitDependencies(
            tokenProvider = ChatkitTokenProvider(
                endpoint = "${ApiConstants.CHATKIT_BASE_URL}/token",
                userId = "neo"
            ),
            context = context
        ))

    init {
        connectChatkit()
    }

    private fun connectChatkit() {
        chatManager.connect { result ->
            when (result) {
                is Result.Success -> {
                    this.chatkitUser.postValue(result.value)
                    if (roomId != null) {
                        subscribeToRoom(result.value)
                    }
                }

                is Result.Failure -> {
                    // Failure
                    Log.e("TAG", "Error connecting to chatkit: ${result.error.reason}")
                    result.error.printStackTrace()
                }
            }
        }
    }

    private fun subscribeToRoom(user: CurrentUser) {
        user.subscribeToRoomMultipart(
            this.roomId!!,
            RoomListeners(onMultipartMessage = { message ->
                this.messages.postValue(Helper.addListElement(this.messages.value!!, message))
            }),
            20
        ) {}
    }

    fun sendMessage(message: String) {
        this.chatkitUser.value?.sendSimpleMessage(
            this.roomId!!,
            message
        ) { result ->
            when (result) {
                is Result.Failure -> handleError(result.error)
            }
        }
    }

    fun attemptJoinRoom(roomId: String) {
        this.chatkitUser.value?.getJoinableRooms { result ->
            when (result) {
                is Result.Success -> {
                    for (joinableRoom in result.value)
                        if (joinableRoom.id == roomId) {
                            joinRoom(roomId)
                            return@getJoinableRooms
                        }

                    // If room doesn't exist, create it instead
                    createRoom(roomId)
                }

                is Result.Failure -> handleError(result.error)
            }
        }
    }

    fun joinRoom(roomId: String) {
        this.chatkitUser.value?.joinRoom(
            roomId
        ) { result ->
            when (result) {
                is Result.Success -> this.room.postValue(result.value)
                is Result.Failure -> handleError(result.error)
            }
        }
    }

    fun createRoom(roomId: String) {
        this.chatkitUser.value?.createRoom(
            roomId,
            roomId,
            null,
            false,
            null
        ) { result ->
            when (result) {
                is Result.Success -> joinRoom(roomId)
                is Result.Failure -> handleError(result.error)
            }
        }
    }

    fun leaveRoom() {
        this.chatkitUser.value?.leaveRoom(
            roomId!!
        ) { result ->
            when (result) {
                is Result.Success -> room.postValue(null)
                is Result.Failure -> handleError(result.error)
            }
        }
    }

    private fun handleError(error: Error) {
        Log.e("TAG", "Error encountered while using Chatkit: ${error.reason}")
        error.printStackTrace()
    }

    fun terminate() {
        chatManager.close{}
    }


}