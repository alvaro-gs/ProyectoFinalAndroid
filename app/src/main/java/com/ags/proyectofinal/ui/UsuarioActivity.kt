package com.ags.proyectofinal.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ags.proyectofinal.R
import com.ags.proyectofinal.databinding.ActivityUsuarioBinding

class UsuarioActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUsuarioBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUsuarioBinding.inflate(layoutInflater)

        setContentView(binding.root)

        if (savedInstanceState == null){
            supportFragmentManager.beginTransaction()
                .replace(R.id.fgContainerView, ConfiguracionFragment())
                .commit()
        }


        binding.bottomNavigationView.setOnItemReselectedListener{menu ->
            when(menu.itemId){


            }
        }
    }
}