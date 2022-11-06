package com.nyller.android.shoppinglist.application

import android.app.Application
import com.nyller.android.shoppinglist.database.ProductDatabase
import com.nyller.android.shoppinglist.repository.ProductRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class MyApplication: Application() {

    val applicationScope = CoroutineScope(SupervisorJob())

    val database by lazy { ProductDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { ProductRepository(database.productDao()) }

}