package com.example.seller.repository

import android.util.Log
import com.example.seller.entity.Category
import com.example.seller.entity.Product
import com.example.seller.network.ProductService
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val productService: ProductService
) {
    suspend fun getProducts(offset: Int, limit: Int): List<Product>? {
        var list: List<Product>? = mutableListOf()
        return try {
            val res = productService.getProductsAsPage(offset, limit)
            if (res.isSuccessful) {
                list = res.body()
            } else {
                Log.d("TAG", "getProducts: Response body is null")
            }
            list
        } catch (e: Exception) {
            Log.d("TAG", "Error loading products: $e")
            null
        } catch (e: IOException) {
            Log.d("TAG", "Error loading products: $e")
            null
        } catch (e: HttpException) {
            Log.d("TAG", "Error loading products: $e")
            null
        }
    }

    suspend fun getCategories(): List<Category>? {
        var list: List<Category>? = mutableListOf()
        return try {
            val res = productService.getCategories()
            if (res.isSuccessful) {
                list = res.body()
            } else {
                Log.d("TAG", "getCategories: Response body is null")
            }
            list
        } catch (e: Exception) {
            Log.d("TAG", "Error loading getCategories: $e")
            null
        } catch (e: IOException) {
            Log.d("TAG", "Error loading getCategories: $e")
            null
        } catch (e: HttpException) {
            Log.d("TAG", "Error loading getCategories: $e")
            null
        }
    }
}