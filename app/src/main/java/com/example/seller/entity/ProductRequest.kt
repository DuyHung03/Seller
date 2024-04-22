package com.example.seller.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductRequest(
    val title: String,
    val price: Int,
    val description: String,
    val categoryId: Int,
    val images: List<String>,
) : Parcelable