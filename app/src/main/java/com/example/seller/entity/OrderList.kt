package com.example.seller.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class
OrderList(
    val orderList: Map<String, Order> = emptyMap()
) : Parcelable {
    constructor() : this(emptyMap())
}