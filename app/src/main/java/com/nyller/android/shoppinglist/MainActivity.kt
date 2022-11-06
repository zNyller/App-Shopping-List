package com.nyller.android.shoppinglist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.view.MenuProvider
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.nyller.android.shoppinglist.adapter.ProductAdapter
import com.nyller.android.shoppinglist.application.MyApplication
import com.nyller.android.shoppinglist.databinding.ActivityMainBinding
import com.nyller.android.shoppinglist.domain.Product
import com.nyller.android.shoppinglist.ui.AddProductActivity
import com.nyller.android.shoppinglist.ui.MainViewModel
import com.nyller.android.shoppinglist.ui.MainViewModelFactory

class MainActivity : AppCompatActivity() {

    private val binding : ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val mViewModel : MainViewModel by viewModels {
        MainViewModelFactory((application as MyApplication).repository)
    }

    private val getResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val newProduct = result.data?.getSerializableExtra(AddProductActivity.EXTRA_REPLY) as Product
            mViewModel.insert(newProduct)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupActionBar()

        val adapter = ProductAdapter(ProductAdapter.ProductClickListener { product ->
            mViewModel.onProductClicked(product)
            Toast.makeText(this, "Produto: ${product.name}", Toast.LENGTH_SHORT).show()
        })

        binding.recyclerView.adapter = adapter
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))

        mViewModel.allProducts.observe(this) { products ->
            products?.let { adapter.addHeaderAndSubmitList(it) }
        }

        mViewModel.onProductDetail.observe(this) { product ->
            product?.let {
                mViewModel.onProductOpened()
            }
        }

    }

    override fun onStart() {
        super.onStart()

        binding.fabAdd.setOnClickListener {
            val intent = Intent(this, AddProductActivity::class.java)
            getResult.launch(intent)
        }

    }

    private fun setupActionBar() {

        binding.mainToolbar.inflateMenu(R.menu.main_menu)
        binding.mainToolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menu_clear -> {
                    mViewModel.deleteAll()
                    Toast.makeText(this, "Lista de compras excluída!", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }

    }

}