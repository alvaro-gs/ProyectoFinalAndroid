package com.ags.proyectofinal

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.ags.proyectofinal.databinding.FragmentLoginBinding

class LoginFragment : Fragment(R.layout.fragment_login) {

    private lateinit var binding: FragmentLoginBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLoginBinding.bind(view)

        binding.btIniciar.setOnClickListener {
            var error = false
            val bundle = Bundle().apply {
                if(binding.etEmail.text.isEmpty()) {
                    binding.etEmail.error = getString(R.string.error)
                    error = true
                }
                else{
                    putString("EXTRA_EMAIL", binding.etEmail.text.toString())}

                if(binding.etPassword.text.isEmpty()){
                    binding.etPassword.error = getString(R.string.error)
                    error = true
                }
                else{
                    putString("EXTRA_PASSWORD",binding.etPassword.text.toString())
                }
            }
            if(!error) {
                val intent = Intent(requireContext(), CatalogoActivity::class.java).apply {
                    putExtra("KEY_INFO", "Login")
                    putExtra("EXTRA_BUNDLE", bundle)
                }
                startActivity(intent)
            }
        }

        binding.tvRegistro.setOnClickListener {
          parentFragmentManager.beginTransaction()
              .replace(R.id.fgContainerView, RegisterFragment.newInstance())
              .addToBackStack("RegisterFragment")
              .commit()
        }

        binding.btCatalogo.setOnClickListener {
            val intent = Intent(requireContext(),CatalogoActivity::class.java).apply {
                putExtra("KEY_INFO","Invitado")
            }
            startActivity(intent)
        }


    }

    companion object {
        fun newInstance() = LoginFragment()
    }
}