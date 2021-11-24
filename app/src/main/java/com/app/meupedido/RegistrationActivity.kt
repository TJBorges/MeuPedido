package com.app.meupedido

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.app.meupedido.databinding.ActivityRegistrationBinding

class RegistrationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        saveRegister()

        binding
    }

    private fun saveRegister() {
        binding.btnAdd.setOnClickListener {
            val numberOrder = binding.txtNumberOrder.text.toString().trim()
            if (numberOrder.isNullOrEmpty()) {
                Toast.makeText(this, "Número do pedido inválido", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent()
                intent.putExtra("keyName", numberOrder)
                setResult(Activity.RESULT_OK, intent)
                finish()
            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        finish()
    }
}