package com.example.seller.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CartItem(
    val productId: String = "",
    val product: Product,
    var quantity: Int
) : Parcelable {
    // Add a no-argument constructor for Firestore deserialization
    constructor() : this("", Product(), 0)
}