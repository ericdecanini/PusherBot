package com.example.pusherbot.activities.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pusherbot.BR
import com.example.pusherbot.R
import com.example.pusherbot.activities.base.BaseActivity
import com.example.pusherbot.activities.lobby.LobbyActivity
import com.example.pusherbot.adapters.ChatAdapter
import com.example.pusherbot.databinding.ActivityMainBinding
import com.pusher.chatkit.messages.multipart.Message
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override val bindingVariable: Int
        get() = BR.mainViewModel

    override val viewModel: MainViewModel
        get() = ViewModelProviders.of(this).get(MainViewModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initRoom()
        setListeners()
    }

    private fun initRoom() {
        val roomId = intent.getStringExtra("ROOM_ID")
        if (roomId == null) {
            handleNullRoomId()
            return
        }

        supportActionBar?.title = "Room $roomId"

        viewModel.initChatbotRepository(this, roomId)
    }

    private fun setListeners() {
        chat_send_button.setOnClickListener { handleSendButton() }
    }

    override fun initObservers() {
        super.initObservers()
        observeMessages()
        observeRoom()
    }

private fun observeRoom() {
    viewModel.getRoom().observe(this, Observer { room ->
        if (room == null)
        handleLeaveRoom()
    })
}

private fun handleMessages(messages: List<Message>) {
    val chatAdapter = ChatAdapter(messages)
    chat_messages_list.layoutManager = LinearLayoutManager(this)
    chat_messages_list.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
    chat_messages_list.adapter = chatAdapter
    chat_messages_list.layoutManager?.scrollToPosition(messages.size - 1)
}

private fun observeMessages() {
    viewModel.getMessages().observe(this, Observer { messages ->
        handleMessages(messages)
    })
}

private fun handleSendButton() {
    sendMessage(chat_input.text.toString())
    chat_input.setText("")
}

private fun sendMessage(message: String) {
    viewModel.sendMessage(message)
}

private fun handleLeaveRoom() {
    val preferences = getSharedPreferences("MAIN_PREFERENCES", Context.MODE_PRIVATE)
    preferences.edit()
        .putString("ROOM_ID", null)
        .apply()

    startActivity(Intent(this, LobbyActivity::class.java))
    finish()
}

private fun handleNullRoomId() {
    startActivity(Intent(this, LobbyActivity::class.java))
    finish()
}

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.ic_sign_out -> {
                viewModel.signOut()
                true
            }
            R.id.ic_leave_room -> {
                viewModel.leaveRoom()
                true
            }
            else -> false
        }
    }

}
