package com.example.seller.repository

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.seller.entity.Message
import com.example.seller.entity.Order
import com.example.seller.entity.OrderList
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject

class DataRepository @Inject constructor(
    private val db: FirebaseFirestore,
    private val storage: FirebaseStorage,
    private val auth: FirebaseAuth,
    private val realtimeDatabase: FirebaseDatabase
) {

    suspend fun signInEmail(email: String, password: String): FirebaseUser? {
        return try {
            val authResult = auth.signInWithEmailAndPassword(email, password).await()
            if (authResult.user != null) {
                authResult.user
            } else {
                Log.d("TAG", "signInEmail: Cannot found account")
                null
            }
        } catch (e: FirebaseAuthException) {
            // Handle exception or log the error
            Log.d("TAG", "signInEmail: ${e.message}")
            null
        }
    }

    private val orderCollection = db.collection("order")
    private val storageRef = storage.reference
    suspend fun getAllOrders(): List<Order>? {
        val list = mutableListOf<Order>()
        return try {
            val querySnapshot = orderCollection.get().await()
            for (doc in querySnapshot) {
                val orderList = doc.toObject(OrderList::class.java)
                list.addAll(orderList.orderList.values)
            }
            list
        } catch (e: Exception) {
            null
        }
    }

    suspend fun cancelOrder(
        order: Order,
        customerId: String,
    ): String {
        val cancelOrderDoc = orderCollection.document(customerId)
        return try {
            val snapshot = cancelOrderDoc.get().await()
            if (snapshot.exists()) {
                val existingItem = snapshot.toObject(OrderList::class.java)
                existingItem?.let {
                    existingItem.orderList[order.orderId]?.cancelled = true
                    existingItem.orderList[order.orderId]?.reasonCancel =
                        "The order was canceled by the seller"
                    cancelOrderDoc.set(existingItem).await()
                }
            }
            "Cancel order successfully"
        } catch (e: Exception) {
            "Failed to cancel order: ${e.message}"
        }
    }

    suspend fun acceptOrder(
        order: Order,
        customerId: String,
    ): String {
        val orderDoc = orderCollection.document(customerId)
        return try {
            val snapshot = orderDoc.get().await()
            if (snapshot.exists()) {
                val existingItem = snapshot.toObject(OrderList::class.java)
                existingItem?.let {
                    existingItem.orderList[order.orderId]?.confirmed = true
                    orderDoc.set(existingItem).await()
                }
            }
            "Accept order successfully"
        } catch (e: Exception) {
            "Failed to accept order: ${e.message}"
        }
    }

    suspend fun saveImageToStorage(uris: List<Uri>): List<Uri> {
        val list: MutableList<Uri> = mutableListOf()
        try {
            for (uri in uris) {
                val imageRef = storageRef.child("images/" + UUID.randomUUID().toString())
                imageRef.putFile(uri).await()
                val downloadUrl = imageRef.downloadUrl.await()
                Log.e("TAG", downloadUrl.toString())
                list.add(downloadUrl)
            }
        } catch (e: Exception) {
            Log.e("TAG", "saveImageToStorage: Exception", e)
        }
        return list
    }

    fun getChatUserList(liveData: MutableLiveData<List<Message>>) {
        val chatUserRef = realtimeDatabase.getReference("chatUser")
        chatUserRef.orderByChild("timestamp")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val chatUserList: List<Message> = snapshot.children.map {
                        it.getValue(Message::class.java)!!.copy(id = snapshot.key!!)
                    }
                    liveData.postValue(chatUserList)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("TAG", "onCancelled: $error")
                }

            })
    }

    fun getMessageByUser(userId: String, liveData: MutableLiveData<List<Message>>) {
        val messageRef = realtimeDatabase.getReference("messages/$userId")
        messageRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val messages: List<Message> = snapshot.children.map {
                    it.getValue(Message::class.java)!!.copy(id = snapshot.key!!)
                }
                liveData.postValue(messages)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("TAG", "onCancelled: $error")
            }

        })
    }

    suspend fun sendMessage(userId: String, message: Message) {
        val messageRef = realtimeDatabase.getReference("messages/$userId")
        withContext(Dispatchers.IO) {
            try {

                if (!message.imageUrl.isNullOrEmpty() && message.message.isNullOrEmpty()) {
                    // Upload image and get image URL
                    val imageUrl = uploadImage(Uri.parse(message.imageUrl))
                    // Set the image URL in the message
                    message.imageUrl = imageUrl.toString()
                }

                val messageId =
                    messageRef.push().key ?: throw Exception("Failed to generate message ID")

                messageRef.child(messageId).setValue(message)

            } catch (e: Exception) {
                Log.d("TAG", "sendMessage: $e")
                throw Exception(e)
            }
        }
    }

    private suspend fun uploadImage(uri: Uri): Uri {
        val storageRef = storage.reference
        val imagesRef = storageRef.child("chatImages/${UUID.randomUUID()}")
        imagesRef.putFile(uri).await()

        return imagesRef.downloadUrl.await()
    }

}

