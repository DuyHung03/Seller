package com.example.seller.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seller.entity.Order
import com.example.seller.repository.DataRepository
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
        Log.d("TAG", "getAllOrders: ${orderList?.get(0)?.orderId}")
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

}