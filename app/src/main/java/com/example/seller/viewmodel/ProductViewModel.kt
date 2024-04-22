package com.example.seller.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seller.entity.Category
import com.example.seller.entity.CategoryRequest
import com.example.seller.entity.Product
import com.example.seller.entity.ProductRequest
import com.example.seller.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _productsLiveData = MutableLiveData<List<Product>?>()
    val productsLiveData: LiveData<List<Product>?> get() = _productsLiveData
    fun getProducts(offset: Int, limit: Int) = viewModelScope.launch {
        _productsLiveData.value = productRepository.getProducts(offset, limit)
    }


    private val _categoriesLiveData = MutableLiveData<List<Category>?>()
    val categoriesLiveData: LiveData<List<Category>?> get() = _categoriesLiveData
    fun getCategories() = viewModelScope.launch {
        _categoriesLiveData.value = productRepository.getCategories()
    }

    fun updateProduct(id: Int, productRequest: ProductRequest, callback: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val res = productRepository.updateProduct(id, productRequest)
                callback(res)
            } catch (e: Exception) {
                callback("Error: ${e.message}")
            }
        }
    }

    fun deleteProduct(id: Int, callback: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val res = productRepository.deleteProduct(id)
                callback(res)
            } catch (e: Exception) {
                callback("Error: ${e.message}")
            }
        }
    }

    fun addNewProduct(productRequest: ProductRequest, callback: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val res = productRepository.addNewProduct(productRequest)
                callback(res)
            } catch (e: Exception) {
                callback("Error: ${e.message}")
            }
        }
    }

    fun updateCategory(id: Int, categoryRequest: CategoryRequest, callback: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val res = productRepository.updateCategory(id, categoryRequest)
                callback(res)
            } catch (e: Exception) {
                callback("Error: ${e.message}")
            }
        }
    }

    fun deleteCategory(id: Int, callback: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val res = productRepository.deleteCategory(id)
                callback(res)
            } catch (e: Exception) {
                callback("Error: ${e.message}")
            }
        }
    }

    fun addNewCategory(categoryRequest: CategoryRequest, callback: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val res = productRepository.addNewCategory(categoryRequest)
                callback(res)
            } catch (e: Exception) {
                callback("Error: ${e.message}")
            }
        }
    }

}