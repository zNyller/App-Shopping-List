package com.nyller.android.shoppinglist.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.nyller.android.shoppinglist.domain.Product

@Database(entities = [Product::class], version = 1)
abstract class ProductDatabase: RoomDatabase() {

    abstract fun productDao(): ProductDAO

    companion object {

        private const val DATABASE_NAME = "app-database"

        @Volatile
        private lateinit var INSTANCE: ProductDatabase

        fun getDatabase(context: Context): ProductDatabase {
            synchronized(ProductDatabase::class.java) {
                if (!::INSTANCE.isInitialized) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        ProductDatabase::class.java, DATABASE_NAME
                    ).build()
                }
            }
            return INSTANCE
        }

    }

}