package com.example.seller.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seller.entity.Message
import com.example.seller.entity.Order
import com.example.seller.repository.DataRepository
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DataViewModel @Inject constructor(
    private val dataRepository: DataRepository
) : ViewModel() {
    private val _ordersLiveData = MutableLiveData<List<Order>?>()
    val ordersLiveData: LiveData<List<Order>?> get() = _ordersLiveData
    fun getOrders() = viewModelScope.launch {
        val orderList = dataRepository.getAllOrders()
        _ordersLiveData.value = orderList
    }

    fun cancelOrder(
        order: Order,
        customerId: String
    ): String {
        var message = ""
        viewModelScope.launch {
            message = dataRepository.cancelOrder(order, customerId)
        }
        return message
    }

    fun acceptOrder(
        order: Order,
        customerId: String
    ): String {
        var message = ""
        viewModelScope.launch {
            message = dataRepository.acceptOrder(order, customerId)
        }
        return message
    }

    fun saveImageToStorage(uris: List<Uri>, callback: (List<Uri>) -> Unit) {
        viewModelScope.launch {
            val result = dataRepository.saveImageToStorage(uris)
            callback(result)
        }
    }

    fun signInEmail(email: String, password: String, callback: (FirebaseUser?) -> Unit) =
        viewModelScope.launch {
            val result = dataRepository.signInEmail(email, password)
            callback(result)
        }

    private val _chatUserLiveData = MutableLiveData<List<Message>>()
    val chatUserLiveData: LiveData<List<Message>> = _chatUserLiveData
    fun getChatUserList() {
        try {
            dataRepository.getChatUserList(_chatUserLiveData)
        } catch (e: Exception) {
            Log.d("TAG", "getChatUserList: $e")
        }
    }

    private val _messagesLiveData = MutableLiveData<List<Message>>()
    val messagesLiveData: LiveData<List<Message>> = _messagesLiveData
    fun getMessageByUser(userId: String) {
        try {
            dataRepository.getMessageByUser(userId, _messagesLiveData)
        } catch (e: Exception) {
            Log.d("TAG", "getChatUserList: $e")
        }
    }

    fun sentMessage(userId: String, message: Message) = viewModelScope.launch {
        dataRepository.sendMessage(userId, message)
    }

}