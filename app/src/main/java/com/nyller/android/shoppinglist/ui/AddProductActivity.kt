package com.nyller.android.shoppinglist.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.nyller.android.shoppinglist.databinding.ActivityAddProductBinding
import com.nyller.android.shoppinglist.domain.Product

class AddProductActivity : AppCompatActivity() {

    private val binding: ActivityAddProductBinding by lazy {
        ActivityAddProductBinding.inflate(layoutInflater)
    }

    private var category = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnAdd.setOnClickListener { verifyFields() }

    }

    private fun verifyFields() {

        val rbSelected = binding.radioGroup.checkedRadioButtonId

        if (binding.edtProductNameText.text.isNullOrEmpty()) {
            Toast.makeText(this, "Insira um nome para o produto!", Toast.LENGTH_SHORT).show()
            binding.edtProductNameText.requestFocus()
            return
        }

        if (rbSelected == binding.rbLimpeza.id) category = "Limpeza"
        if (rbSelected == binding.rbAlimentos.id) category = "Alimentos"
        if (rbSelected == -1) {
            Toast.makeText(this, "Selecione uma categoria para o produto!", Toast.LENGTH_SHORT).show()
            return
        }

        saveNewProduct()

    }

    companion object {
        const val EXTRA_REPLY = "dados"
    }

    private fun saveNewProduct() {
        val product = Product(
            name = binding.edtProductNameText.text.toString(),
            category = category
        )

        val replyIntent = Intent()
        replyIntent.putExtra(EXTRA_REPLY, product)
        setResult(RESULT_OK, replyIntent)
        finish()

    }

}