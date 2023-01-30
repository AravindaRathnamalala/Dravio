package com.araa.dravio.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.araa.dravio.R
import com.araa.dravio.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private  lateinit var  binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }
}