package com.app.meupedido

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.isDigitsOnly
import com.app.meupedido.databinding.ActivityRegistrationBinding

class RegistrationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        saveRegister()

    }

    private fun saveRegister() {
        binding.btnAdd.setOnClickListener {
            val numberOrder = binding.txtNumberOrder.text.toString().trim()
            if (numberOrder.isEmpty()
                || numberOrder.length < 7
                || validateNumberOrder(numberOrder)) {
                Toast.makeText(this, "Número do pedido inválido", Toast.LENGTH_SHORT).show()
            } else {
                    val intent = Intent()
                    intent.putExtra("keyName", numberOrder)
                    setResult(Activity.RESULT_OK, intent)
                    finish()
            }

        }
    }

    private fun validateNumberOrder(numberOrder: String): Boolean {
        val idStore = numberOrder.substring(0,3)
        val number = numberOrder.substring(3,7)
        return !(!idStore.isDigitsOnly() && number.isDigitsOnly())
    }

    override fun onDestroy() {
        super.onDestroy()
        finish()
    }
}