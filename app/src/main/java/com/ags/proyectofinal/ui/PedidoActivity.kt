package com.ags.proyectofinal.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ags.proyectofinal.databinding.ActivityPedidoBinding

class PedidoActivity : AppCompatActivity() {

    private lateinit var binding : ActivityPedidoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPedidoBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}