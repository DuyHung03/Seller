package com.example.seller.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seller.entity.Category
import com.example.seller.entity.Product
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

}