package com.app.meupedido

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.app.meupedido.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var prefs: SharedPreferences
    private var autoArchive = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setPrefs()
    }

    private fun setPrefs() {
        prefs = this.applicationContext.getSharedPreferences("my_orders_prefs", 0)
        autoArchive = prefs.getBoolean("auto_archive_unit", false)
        binding.swAutoArchive.isChecked = autoArchive

        binding.swAutoArchive.setOnCheckedChangeListener { _, isChecked ->
            val editor = prefs.edit()
            if (isChecked)
                editor?.apply() { putBoolean("auto_archive_unit", true).apply() }
            else editor?.apply() { putBoolean("auto_archive_unit", false).apply() }
        }
    }
}