package com.nyller.android.shoppinglist.util

import com.nyller.android.shoppinglist.adapter.ProductAdapter
import com.nyller.android.shoppinglist.domain.Product

fun List<Product>.toListOfDataItem(): List<ProductAdapter.DataItem> {

    val grouping = this.groupBy { product ->
       product.category
    }

    val listDataItem = mutableListOf<ProductAdapter.DataItem>()
    grouping.forEach { mapEntry ->
        listDataItem.add(ProductAdapter.DataItem.Header(mapEntry.key))
        listDataItem.addAll(
            mapEntry.value.map { product ->
                ProductAdapter.DataItem.ProductItem(product)
            }
        )
    }

    return listDataItem

}