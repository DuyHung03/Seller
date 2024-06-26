package com.example.seller.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Category(
    val creationAt: String = "", // 2024-01-07T22:28:40.000Z
    val id: Int = 0, // 1
    val image: String = "", // https://w0.peakpx.com/wallpaper/870/294/HD-wallpaper-anime-kamen-rider.jpg
    val name: String = "", // Clothes
    val updatedAt: String = "", // 2024-01-08T00:27:26.000Z
) : Parcelable {
    override fun toString(): String = name
}