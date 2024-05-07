package com.example.seller.adapter

import android.app.ActivityOptions
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.seller.R
import com.example.seller.entity.Message
import com.example.seller.view.activity.ChatActivity
import de.hdodenhof.circleimageview.CircleImageView

class ChatUserAdapter :
    RecyclerView.Adapter<ChatUserAdapter.ViewHolder>() {

    private val chatUserList = mutableListOf<Message>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.chat_user, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chatUser = chatUserList[position]
        holder.bind(chatUser)
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, ChatActivity::class.java)
            intent.putExtra("chat_user", chatUser)
            startActivity(
                holder.itemView.context,
                intent,
                ActivityOptions.makeCustomAnimation(
                    holder.itemView.context,
                    R.anim.slide_in_right,
                    R.anim.slide_out_left
                )
                    .toBundle()
            )
        }

    }

    override fun getItemCount(): Int {
        return chatUserList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val avatar: CircleImageView = itemView.findViewById(R.id.avatar)
        private val recipientName: TextView = itemView.findViewById(R.id.recipientName)
        private val lastMessage: TextView = itemView.findViewById(R.id.lastMessage)

        fun bind(chatUser: Message) {
            Glide.with(itemView.context)
                .load(chatUser.senderPhoto)
                .override(100, 100)
                .into(avatar)

            recipientName.text = chatUser.senderName ?: chatUser.senderEmail
            lastMessage.text = chatUser.message ?: "Sent a photo."
        }
    }


    fun setChatUsers(newChatUserList: List<Message>) {
        chatUserList.clear()
        chatUserList.addAll(newChatUserList)
        notifyDataSetChanged()
    }
}

