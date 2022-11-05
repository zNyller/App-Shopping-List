package com.nyller.android.shoppinglist.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.nyller.android.shoppinglist.domain.Product

@Dao
interface ProductDAO {

    @Query("SELECT * FROM products")
    fun getProducts(): LiveData<List<Product>>

    @Insert(onConflict = REPLACE)
    suspend fun insert(product: Product)

    @Delete
    suspend fun delete(product: Product)

    @Query("DELETE FROM products")
    suspend fun deleteAll()

}