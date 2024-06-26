package com.example.seller.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class Cost(
    val productsCost: Double,
    val shippingCost: Double,
    val total: Double
) : Parcelable {
    constructor() : this(0.0, 0.0, 0.0)
}