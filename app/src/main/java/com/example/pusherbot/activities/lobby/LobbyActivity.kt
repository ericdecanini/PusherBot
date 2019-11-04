package com.example.pusherbot.activities.lobby

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.pusherbot.BR
import com.example.pusherbot.R
import com.example.pusherbot.activities.base.BaseActivity
import com.example.pusherbot.activities.main.MainActivity
import com.example.pusherbot.databinding.ActivityLobbyBinding
import kotlinx.android.synthetic.main.activity_lobby.*

class LobbyActivity : BaseActivity<ActivityLobbyBinding, LobbyViewModel>() {

    override fun getLayoutId(): Int {
        return R.layout.activity_lobby
    }
    override val bindingVariable: Int
        get() = BR.lobbyViewModel

    override val viewModel: LobbyViewModel
        get() = ViewModelProviders.of(this).get(LobbyViewModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val preferences = getSharedPreferences("MAIN_PREFERENCES", Context.MODE_PRIVATE)
        val roomId = preferences.getString("ROOM_ID", null)

        if (roomId != null) {
            launchChatroom(roomId)
            return
        }

        viewModel.initChatbotRepository(this)
        setListeners()
    }

    override fun initObservers() {
        super.initObservers()
        observeRoom()
    }

    private fun setListeners() {
        button_join.setOnClickListener { handleSendButton() }
    }

    private fun observeRoom() {
        viewModel.getRoom().observe(this, Observer { room ->
            if (room != null) {
                handleRoomJoined(room.id)
            }
        })
    }

    private fun handleSendButton() {
        viewModel.joinRoom(input_roomid.text.toString())
    }

    private fun handleRoomJoined(roomId: String) {
        val preferences = getSharedPreferences("MAIN_PREFERENCES", Context.MODE_PRIVATE)
        preferences.edit()
            .putString("ROOM_ID", roomId)
            .apply()

       launchChatroom(roomId)
    }

    private fun launchChatroom(roomId: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("ROOM_ID", roomId)
        startActivity(intent)
        finish()
    }

}
