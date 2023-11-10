package com.ags.proyectofinal.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.ags.proyectofinal.R
import com.ags.proyectofinal.data.util.OpcionMenu
import com.ags.proyectofinal.databinding.FragmentConfiguracionBinding
import com.google.firebase.auth.FirebaseAuth


class ConfiguracionFragment : Fragment() {

    private var _binding : FragmentConfiguracionBinding? = null
    private val binding get() = _binding!!

    private lateinit var firebaseAuth: FirebaseAuth

    private var menuOpciones : List<OpcionMenu> = emptyList()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentConfiguracionBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()
        menuOpciones = arrayListOf(
            OpcionMenu(getString(R.string.configuración),R.drawable.ic_settings),
            OpcionMenu(getString(R.string.domiciliosEntrega),R.drawable.ic_delivery_dining),
            OpcionMenu(getString(R.string.cerrarSesion),R.drawable.ic_logout))


       binding.rvMenu.apply {
           layoutManager = LinearLayoutManager(requireContext())
           adapter = MenuOpcionesAdapter(menuOpciones){
                   opcion -> onOpcionClicked(opcion)
           }

       }
    }

    private fun onOpcionClicked(opcion: Int){
        when(opcion){
            0 -> {

            }
            1 -> {

            }

            2 -> {
                firebaseAuth?.signOut()
                startActivity(Intent(requireContext(),LoginActivity::class.java))
                this.requireActivity().finish()
                Toast.makeText(requireContext(), getString(R.string.hastaLuego), Toast.LENGTH_SHORT).show()
            }
        }
    }
}