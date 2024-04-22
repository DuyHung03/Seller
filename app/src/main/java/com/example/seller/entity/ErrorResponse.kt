package com.example.seller.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ErrorResponse(
    val error: String,
    val message: List<String>,
    val statusCode: Int
) : Parcelable