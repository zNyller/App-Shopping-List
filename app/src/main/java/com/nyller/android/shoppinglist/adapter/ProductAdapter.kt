package com.nyller.android.shoppinglist.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nyller.android.shoppinglist.databinding.HeaderBinding
import com.nyller.android.shoppinglist.databinding.ItemProductBinding
import com.nyller.android.shoppinglist.domain.Product
import com.nyller.android.shoppinglist.util.toListOfDataItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private val ITEM_VIEW_TYPE_HEADER = 0
private val ITEM_VIEW_TYPE_ITEM = 1

private val adapterScope = CoroutineScope(Dispatchers.Default)

class ProductAdapter(
    val clickListener: ProductClickListener
) : ListAdapter<ProductAdapter.DataItem, RecyclerView.ViewHolder>(DiffCallback()) {

    class HeaderViewHolder(val binding : HeaderBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(item: DataItem.Header) {
            with(binding) {
                header = item
            }
        }

        companion object {
            fun from(parent: ViewGroup): HeaderViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding: HeaderBinding = HeaderBinding.inflate(layoutInflater, parent, false)
                return HeaderViewHolder(binding)
            }
        }
    }

    class ProductViewHolder private constructor(val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            product: Product,
            clickListener: ProductClickListener
        ) {
            binding.product = product
            binding.clickListener = clickListener
        }

        companion object {
            fun from(parent: ViewGroup): ProductViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemProductBinding.inflate(layoutInflater, parent, false)

                return ProductViewHolder(binding)
            }
        }

    }

    override fun getItemViewType(position: Int): Int {
        return when(getItem(position)) {
            is DataItem.ProductItem -> ITEM_VIEW_TYPE_ITEM
            is DataItem.Header -> ITEM_VIEW_TYPE_HEADER
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            ITEM_VIEW_TYPE_ITEM -> ProductViewHolder.from(parent)
            ITEM_VIEW_TYPE_HEADER -> HeaderViewHolder.from(parent)
            else -> throw ClassCastException("Viewtype desconhecido! $viewType")
        }
    }

    fun addHeaderAndSubmitList(list: List<Product>?) {
        adapterScope.launch {
            val listDataItem = list?.toListOfDataItem()
            withContext(Dispatchers.Main) {
                submitList(listDataItem)
            }

        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is ProductViewHolder -> {
                val item = getItem(position) as DataItem.ProductItem
                holder.bind(
                    item.product,
                    clickListener
                )
            }
            is HeaderViewHolder -> {
                val item = getItem(position) as DataItem.Header
                holder.bind(item)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<DataItem>() {
        override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
            return oldItem.id == newItem.id
        }
    }

    sealed class DataItem {

        abstract val id : Int

        data class ProductItem(val product: Product) : DataItem() {
            override val id = product.id
        }

        data class Header(val title: String?): DataItem() {
            override val id = Int.MIN_VALUE
        }
    }

    class ProductClickListener(val clickListener: (productId: Product) -> Unit) {
        fun onClick(product: Product) = clickListener(product)
    }


}