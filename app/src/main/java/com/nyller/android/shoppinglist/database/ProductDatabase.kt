package com.nyller.android.shoppinglist.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.nyller.android.shoppinglist.domain.Product
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [Product::class], version = 1)
abstract class ProductDatabase: RoomDatabase() {

    abstract fun productDao(): ProductDAO

    companion object {

        private const val DATABASE_NAME = "app-database"

        @Volatile
        private lateinit var INSTANCE: ProductDatabase

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): ProductDatabase {
            synchronized(ProductDatabase::class.java) {
                if (!::INSTANCE.isInitialized) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        ProductDatabase::class.java, DATABASE_NAME
                    ).addCallback(ProductDatabaseCallback(scope))
                        .build()
                }
            }
            return INSTANCE
        }

    }

    private class ProductDatabaseCallback(private val scope: CoroutineScope): RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE.let { productDatabase ->
                scope.launch {
                    populateDatabase(productDatabase.productDao())
                }
            }
        }

        suspend fun populateDatabase(productDao: ProductDAO) {
            productDao.deleteAll()
        }
    }

}