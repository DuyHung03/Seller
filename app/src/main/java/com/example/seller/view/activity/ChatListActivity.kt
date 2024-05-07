package com.example.seller.view.activity

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.seller.R
import com.example.seller.adapter.ChatUserAdapter
import com.example.seller.databinding.ActivityChatListBinding
import com.example.seller.viewmodel.DataViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatListBinding
    private lateinit var chatUserAdapter: ChatUserAdapter
    private val dataViewModel by viewModels<DataViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_list)
        binding = ActivityChatListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //set up recyclerView
        chatUserAdapter = ChatUserAdapter()
        binding.recyclerView.adapter = chatUserAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        dataViewModel.getChatUserList()

        dataViewModel.chatUserLiveData.observe(this) {
            chatUserAdapter.setChatUsers(it)
            binding.progressBar.visibility = View.GONE
        }

        binding.closeBtn.setOnClickListener {
            this.finish()
        }

    }
}