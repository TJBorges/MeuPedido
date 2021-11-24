package com.app.meupedido

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.app.meupedido.databinding.ActivityArchiveBinding

class ArchiveActivity : AppCompatActivity() {

    private lateinit var binding: ActivityArchiveBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityArchiveBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
    override fun onDestroy() {
        super.onDestroy()
    }
}