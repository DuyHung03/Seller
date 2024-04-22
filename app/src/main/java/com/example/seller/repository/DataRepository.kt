package com.example.seller.repository

import android.net.Uri
import android.util.Log
import com.example.seller.entity.Order
import com.example.seller.entity.OrderList
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject

class DataRepository @Inject constructor(
    private val db: FirebaseFirestore,
    private val storage: FirebaseStorage
) {

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
                val uploadTask = imageRef.putFile(uri).await()
                val downloadUrl = imageRef.downloadUrl.await()
                Log.e("TAG", downloadUrl.toString())
                list.add(downloadUrl)
            }
        } catch (e: Exception) {
            Log.e("TAG", "saveImageToStorage: Exception", e)
        }
        return list
    }



}