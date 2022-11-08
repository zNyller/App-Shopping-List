package com.nyller.android.shoppinglist.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nyller.android.shoppinglist.domain.Product
import com.nyller.android.shoppinglist.repository.ProductRepository
import kotlinx.coroutines.launch

class MainViewModel(private val repository: ProductRepository): ViewModel() {

    val allProducts: LiveData<List<Product>> = repository.allProducts

    private val _onProductDetail = MutableLiveData<Product?>()
    val onProductDetail: LiveData<Product?>
        get() = _onProductDetail

    fun onProductClicked(id: Product) {
        _onProductDetail.value = id
    }

    fun onProductOpened() {
        _onProductDetail.value = null
    }

    fun insert(product: Product) = viewModelScope.launch {
        repository.insert(product)
    }

    fun delete(product: Product) = viewModelScope.launch {
        repository.delete(product)
    }

    fun deleteAll() = viewModelScope.launch {
        repository.deleteAll()
    }

}