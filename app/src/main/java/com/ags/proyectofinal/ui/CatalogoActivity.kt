package com.ags.proyectofinal.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.ags.proyectofinal.R
import com.ags.proyectofinal.application.ProyectoFinalApp
import com.ags.proyectofinal.data.remote.ProductoRepository
import com.ags.proyectofinal.data.remote.model.ProductoDto
import com.ags.proyectofinal.databinding.ActivityCatalogoBinding
import com.ags.proyectofinal.util.Constants
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class CatalogoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCatalogoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCatalogoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null){
            supportFragmentManager.beginTransaction()
                .replace(R.id.fgContainerView, ListaProductosFragment())
                .commit()
        }

        /*val bundle = intent.getBundleExtra("EXTRA_BUNDLE")
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
        }*/




        binding.btMisPedidos.setOnClickListener {
            val intent = Intent(this, PedidoActivity::class.java)
            startActivity(intent)

        }


    }
}