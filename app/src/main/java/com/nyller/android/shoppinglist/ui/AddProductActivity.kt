package com.nyller.android.shoppinglist.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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
            binding.edtProductNameText.error = "Insira um nome para o produto!"
            binding.edtProductNameText.requestFocus()
            return
        }

        if (rbSelected == binding.rbLimpeza.id) category = "Limpeza"
        if (rbSelected == binding.rbAlimentos.id) category = "Alimentos"
        if (rbSelected == -1) {
            Toast.makeText(this, "Selecione a categoria do produto!", Toast.LENGTH_SHORT).show()
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
            category = category,
            state = false
        )

        val replyIntent = Intent()
        replyIntent.putExtra(EXTRA_REPLY, product)
        setResult(RESULT_OK, replyIntent)
        finish()

    }

}