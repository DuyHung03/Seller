package com.example.seller.utils

import android.content.Context

class SharedPreferences {
    companion object {
        fun saveResponseToCache(context: Context, responseJson: String, key: String) {
            val sharedPreferences = context.getSharedPreferences("MyCache", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString(key, responseJson)
            editor.apply()
        }

        fun getCachedResponse(context: Context, key: String): String? {
            val sharedPreferences = context.getSharedPreferences("MyCache", Context.MODE_PRIVATE)
            return sharedPreferences.getString(key, null)
        }
    }
}
