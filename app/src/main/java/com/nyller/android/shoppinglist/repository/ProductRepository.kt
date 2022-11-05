package com.nyller.android.shoppinglist.repository

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.nyller.android.shoppinglist.database.ProductDAO
import com.nyller.android.shoppinglist.domain.Product

class ProductRepository (private val productDAO: ProductDAO) {

    val allProducts: LiveData<List<Product>> = productDAO.getProducts()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(product: Product) {
        productDAO.insert(product)
    }

    suspend fun delete(product: Product) {
        productDAO.delete(product)
    }

    suspend fun deleteAll() {
        productDAO.deleteAll()
    }

}