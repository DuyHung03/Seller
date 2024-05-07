package com.example.seller.view.activity

import android.os.Bundle
import android.util.Log
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.seller.R
import com.example.seller.adapter.ChatAdapter
import com.example.seller.databinding.ActivityChatBinding
import com.example.seller.entity.Message
import com.example.seller.viewmodel.DataViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.AndroidEntryPoint
import java.util.UUID
import javax.inject.Inject

@AndroidEntryPoint
class ChatActivity : AppCompatActivity() {
    @Inject
    lateinit var firebaseAuth: FirebaseAuth
    private lateinit var currentUser: FirebaseUser
    private lateinit var binding: ActivityChatBinding
    private lateinit var chatUser: Message
    private val dataViewModel by viewModels<DataViewModel>()
    private lateinit var chatAdapter: ChatAdapter

    // Registers a photo picker activity launcher in single-select mode.
    private val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickMultipleVisualMedia(10)) { uris ->
            // Callback is invoked after the user selects a media item or closes the
            // photo picker.
            if (uris != null) {
                for (uri in uris) {
                    dataViewModel.sentMessage(
                        chatUser.senderId,
                        Message(
                            UUID.randomUUID().toString(),
                            currentUser.uid,
                            currentUser.email!!,
                            currentUser.displayName,
                            currentUser.photoUrl.toString(),
                            chatUser.senderId,
                            null,
                            uri.toString(),
                            System.currentTimeMillis()
                        )
                    )
                }
            } else {
                Log.d("TAG", "No media selected")
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        currentUser = firebaseAuth.currentUser!!

        //get user chat from intent
        chatUser = intent.getParcelableExtra("chat_user")!!

        //set avatar
        chatUser.senderPhoto?.let {
            Glide.with(this)
                .load(chatUser.senderPhoto)
                .override(100, 100)
                .into(binding.avatar)
        }

        //set user's name
        binding.recipientName.text = chatUser.senderName ?: chatUser.senderEmail

        //close button
        binding.closeBtn.setOnClickListener {
            this.finish()
        }

        //setup recyclerView
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.stackFromEnd = true
        chatAdapter = ChatAdapter(firebaseAuth.currentUser!!.uid)
        binding.recyclerView.layoutManager = linearLayoutManager
        binding.recyclerView.adapter = chatAdapter

        //get message from firebase
        dataViewModel.getMessageByUser(chatUser.senderId)

        //observe message change
        dataViewModel.messagesLiveData.observe(this) {
            chatAdapter.setMessages(it)
        }

        //handle sent message
        binding.sentButton.setOnClickListener {
            val messageText = binding.edtMessage.text.toString()
            if (messageText.isNotEmpty()) {
                val message = Message(
                    UUID.randomUUID().toString(),
                    currentUser.uid,
                    currentUser.email!!,
                    currentUser.displayName,
                    currentUser.photoUrl.toString(),
                    chatUser.senderId,
                    messageText,
                    null,
                    System.currentTimeMillis()
                )
                dataViewModel.sentMessage(
                    chatUser.senderId,
                    message
                )
            }

            binding.edtMessage.text.clear()
        }

        //handle message photo
        binding.addPhoto.setOnClickListener {
// Launch the photo picker and let the user choose images and videos.
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo))
        }
    }

}
