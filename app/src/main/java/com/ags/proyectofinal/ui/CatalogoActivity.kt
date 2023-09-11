package com.ags.proyectofinal.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.ags.proyectofinal.R
import com.ags.proyectofinal.databinding.ActivityCatalogoBinding

class CatalogoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCatalogoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCatalogoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle = intent.getBundleExtra("EXTRA_BUNDLE")
        val type = intent.getStringExtra("KEY_INFO")

        if (type == "Login"){
            binding.tvInicio.text = getString(R.string.bienvenida,"de nuevo","estos son los datos con los que ingresaste:")
            binding.tvEmail.text = getString(R.string.datos,"Correo",bundle?.getString("EXTRA_EMAIL"))
            binding.tvPassword.text = getString(R.string.datos,"Contraseña",bundle?.getString("EXTRA_PASSWORD"))
            binding.tvName.visibility = View.GONE
            binding.tvLastName.visibility = View.GONE
            binding.tvSex.visibility= View.GONE
        }
        if (type == "Registro"){
            binding.tvInicio.text = getString(R.string.bienvenida,"a nuestra aplicación","te has registrado satisfactoriamente con los siguientes datos:")
            binding.tvName.text = getString(R.string.datos,"Nombre",bundle?.getString("EXTRA_NAME"))
            binding.tvLastName.text = getString(R.string.datos,"Apellido",bundle?.getString("EXTRA_LASTNAME"))
            binding.tvSex.text = getString(R.string.datos,"Sexo",bundle?.getString("EXTRA_SEX"))
            binding.tvEmail.text = getString(R.string.datos,"Correo",bundle?.getString("EXTRA_EMAIL"))
            binding.tvPassword.text = getString(R.string.datos,"Contraseña",bundle?.getString("EXTRA_PASSWORD"))
        }
        if(type == "Invitado"){
            binding.tvInicio.text = getString(R.string.bienvenida,"usuario invitado","recuerda que para poder hacer compras deberás iniciar sesión")
            binding.tvName.visibility = View.GONE
            binding.tvLastName.visibility = View.GONE
            binding.tvSex.visibility = View.GONE
            binding.tvEmail.visibility = View.GONE
            binding.tvPassword.visibility = View.GONE
        }

        binding.btMisPedidos.setOnClickListener {
            val intent = Intent(this, PedidoActivity::class.java)
            startActivity(intent)

        }


    }
}