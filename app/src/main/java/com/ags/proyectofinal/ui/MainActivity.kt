package com.ags.proyectofinal.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.ags.proyectofinal.R
import com.ags.proyectofinal.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var currentMenu : Int = 0

    // Firebase
    private lateinit var firebaseAuth: FirebaseAuth
    private var user: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        user = firebaseAuth.currentUser

        if (savedInstanceState == null){
            supportFragmentManager.beginTransaction()
                .replace(R.id.fgContainerView, ListaProductosFragment())
                .commit()
            currentMenu = R.id.menuInicio
        }

        binding.bottomNavigationView.setOnItemSelectedListener {menu ->
            when(menu.itemId){
                R.id.menuInicio -> {
                    setCurrentFragment(ListaProductosFragment())
                    currentMenu = R.id.menuInicio
                    true
                }

                R.id.menuPedido -> {
                    if (user != null) {
                        setCurrentFragment(ListaPedidosFragment())
                        currentMenu = R.id.menuPedido
                        true
                    }
                    else{
                        AlertDialog.Builder(this)
                            .setTitle(getString(R.string.iniciar))
                            .setMessage(R.string.requiereInicioSesion)
                            .setPositiveButton(getString(R.string.botonPositivo)){ _,_ ->
                                val intent = Intent(this, LoginActivity::class.java)
                                startActivity(intent)
                                finish()
                            }.setNegativeButton(getString(R.string.botonNegativo)){ dialog,_ ->
                                dialog.dismiss()
                            }
                            .create()
                            .show()
                        false
                    }
                }

                R.id.menuUsuario -> {
                    if (user != null) {
                        setCurrentFragment(ConfiguracionFragment())
                        currentMenu = R.id.menuUsuario
                        true
                    }
                    else{
                        AlertDialog.Builder(this)
                            .setTitle(getString(R.string.iniciar))
                            .setMessage(R.string.requiereInicioSesion)
                            .setPositiveButton(getString(R.string.botonPositivo)){ _,_ ->
                                val intent = Intent(this, LoginActivity::class.java)
                                startActivity(intent)
                                finish()
                            }.setNegativeButton(getString(R.string.botonNegativo)){ dialog,_ ->
                                dialog.dismiss()
                            }
                            .create()
                            .show()
                        false
                    }
                }
                else -> {
                    false

                }
            }
        }

    }

    private fun setCurrentFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fgContainerView,fragment)
            commit()
        }
    }
}