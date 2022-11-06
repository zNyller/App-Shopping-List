package com.nyller.android.shoppinglist.domain

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "products")
data class Product (
    @PrimaryKey(autoGenerate = true) var id : Int = 0,
    @ColumnInfo(name = "name") var name : String,
    @ColumnInfo(name = "category") var category : String
    ) : Serializable