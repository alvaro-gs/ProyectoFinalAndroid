package com.ags.proyectofinal.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import com.ags.proyectofinal.R
import com.ags.proyectofinal.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment(R.layout.fragment_register) {

    private lateinit var binding: FragmentRegisterBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRegisterBinding.bind(view)


        binding.btSend.setOnClickListener {
            var error = false
            val bundle = Bundle().apply {

                if(binding.etName.text.isEmpty()) {
                    binding.etName.error = getString(R.string.error)
                    error = true
                }
                else{
                    putString("EXTRA_NAME", binding.etName.text.toString())}

                if(binding.etLastName.text.isEmpty()){
                    binding.etLastName.error = getString(R.string.error)
                    error = true
                }
                else{
                    putString("EXTRA_LASTNAME",binding.etLastName.text.toString())
                }

                when(binding.rgSex.checkedRadioButtonId){
                    -1 -> {
                        Toast.makeText(requireContext(),getString(R.string.noSex),Toast.LENGTH_LONG).show()
                        error = true
                    }
                    R.id.rbMale -> putString("EXTRA_SEX",getString(R.string.male))
                    R.id.rbFemale -> putString("EXTRA_SEX",getString(R.string.female))
                    R.id.rbOther -> putString("EXTRA_SEX",getString(R.string.other))
                    else -> ""
                }

                if(binding.etEmail.text.isEmpty()){
                    binding.etEmail.error = getString(R.string.error)
                    error = true
                }
                else{
                    putString("EXTRA_EMAIL",binding.etEmail.text.toString())
                }

                if(binding.etPassword.text.isEmpty()){
                    binding.etPassword.error = getString(R.string.error)
                    error = true
                }
                else{
                    putString("EXTRA_PASSWORD",binding.etPassword.text.toString())
                }

            }
            if(!error) {
                val intent = Intent(requireContext(), MainActivity::class.java).apply {
                    putExtra("KEY_INFO", "Registro")
                    putExtra("EXTRA_BUNDLE", bundle)
                }
                startActivity(intent)
            }


        }

    }


    companion object {

        @JvmStatic
        fun newInstance() = RegisterFragment()
    }
}