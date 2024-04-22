package com.example.seller.repository

import android.annotation.SuppressLint
import android.util.Log
import com.example.seller.entity.Category
import com.example.seller.entity.CategoryRequest
import com.example.seller.entity.Product
import com.example.seller.entity.ProductRequest
import com.example.seller.network.ProductService
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@SuppressLint("LogNotTimber")
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

    suspend fun updateProduct(id: Int, updateProduct: ProductRequest): String {
        return try {
            val res = productService.updateProduct(id, updateProduct)
            if (res.isSuccessful) {
                "Success"
            } else {
                Log.d("TAG", "updateProduct: Unsuccessful response: ${res.message()}")
                "Failed: ${res.message()}"
            }
        } catch (e: IOException) {
            Log.e("TAG", "updateProduct: Network error", e)
            "Network error: ${e.message}"
        } catch (e: Exception) {
            Log.e("TAG", "updateProduct: Unexpected error", e)
            "Error: ${e.message}"
        }
    }

    suspend fun deleteProduct(id: Int): String {
        return try {
            val res = productService.deleteProduct(id)
            if (res.isSuccessful) {
                "Success"
            } else {
                Log.d("TAG", "delete: Unsuccessful response: ${res.message()}")
                "Failed: ${res.message()}"
            }
        } catch (e: IOException) {
            Log.e("TAG", "delete: Network error", e)
            "Network error: ${e.message}"
        } catch (e: Exception) {
            Log.e("TAG", "delete: Unexpected error", e)
            "Error: ${e.message}"
        }
    }

    suspend fun addNewProduct(productRequest: ProductRequest): String {
        return try {
            val res = productService.addNewProduct(productRequest)
            if (res.isSuccessful) {
                "Success"
            } else {
                Log.d("TAG", "add: Unsuccessful response: ${res.message()}")
                "Failed: ${res.message()}"
            }
        } catch (e: IOException) {
            Log.e("TAG", "add: Network error", e)
            "Network error: ${e.message}"
        } catch (e: Exception) {
            Log.e("TAG", "add: Unexpected error", e)
            "Error: ${e.message}"
        }
    }

    suspend fun updateCategory(id: Int, updateCategoryRequest: CategoryRequest): String {
        return try {
            val res = productService.updateCategory(id, updateCategoryRequest)
            if (res.isSuccessful) {
                "Success"
            } else {
                Log.d(
                    "TAG",
                    "updateCategory: Unsuccessful response: ${res.code()} ${res.message()}"
                )
                "Failed: ${res.message()}"
            }
        } catch (e: IOException) {
            Log.e("TAG", "updateCategory: Network error", e)
            "Network error: ${e.message}"
        } catch (e: Exception) {
            Log.e("TAG", "updateCategory: Unexpected error", e)
            "Error: ${e.message}"
        }
    }

    suspend fun deleteCategory(id: Int): String {
        return try {
            val res = productService.deleteCategory(id)
            if (res.isSuccessful) {
                "Success"
            } else {
                Log.d("TAG", "delete: Unsuccessful response: ${res.message()}")
                "Failed: ${res.message()}"
            }
        } catch (e: IOException) {
            Log.e("TAG", "delete: Network error", e)
            "Network error: ${e.message}"
        } catch (e: Exception) {
            Log.e("TAG", "delete: Unexpected error", e)
            "Error: ${e.message}"
        }
    }

    suspend fun addNewCategory(categoryRequest: CategoryRequest): String {
        return try {
            val res = productService.addNewCategory(categoryRequest)
            if (res.isSuccessful) {
                "Success"
            } else {
                Log.d("TAG", "add: Unsuccessful response: ${res.message()}")
                "Failed: ${res.message()}"
            }
        } catch (e: IOException) {
            Log.e("TAG", "add: Network error", e)
            "Network error: ${e.message}"
        } catch (e: Exception) {
            Log.e("TAG", "add: Unexpected error", e)
            "Error: ${e.message}"
        }
    }
}