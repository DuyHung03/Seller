package com.example.seller.network

import com.example.seller.entity.Category
import com.example.seller.entity.CategoryRequest
import com.example.seller.entity.Product
import com.example.seller.entity.ProductRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductService {
    @GET("products")
    suspend fun getProductsAsPage(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int,
    ): Response<List<Product>>

    @POST("products")
    suspend fun addNewProduct(
        @Body productBody: ProductRequest
    ): Response<Product>

    @PUT("products/{id}")
    suspend fun updateProduct(
        @Path("id") id: Int,
        @Body updateBody: ProductRequest
    ): Response<Product>

    @DELETE("products/{id}")
    suspend fun deleteProduct(
        @Path("id") id: Int
    ): Response<Boolean>

    @GET("categories")
    suspend fun getCategories(): Response<List<Category>>

    @PUT("categories/{id}")
    suspend fun updateCategory(
        @Path("id") id: Int,
        @Body updateBody: CategoryRequest
    ): Response<Category>

    @POST("categories")
    suspend fun addNewCategory(
        @Body categoryBody: CategoryRequest
    ): Response<Category>

    @DELETE("categories/{id}")
    suspend fun deleteCategory(
        @Path("id") id: Int
    ): Response<Boolean>

}