package com.example.pusherbot.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pusherbot.R
import com.pusher.chatkit.messages.multipart.Message
import com.pusher.chatkit.messages.multipart.Payload
import kotlinx.android.synthetic.main.list_item_chat.view.*

class ChatAdapter(private val messages: List<Message>): RecyclerView.Adapter<ChatAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_chat, parent, false))
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val message = this.messages[position]

        holder.sender.text = message.sender.name
        when(val data = message.parts[0].payload) {
            is Payload.Inline -> holder.message.text = data.content
        }
    }


    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val sender = view.chat_sender
        val message = view.chat_message
    }

}