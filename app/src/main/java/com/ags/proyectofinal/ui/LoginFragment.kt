package com.ags.proyectofinal.ui

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.ags.proyectofinal.R
import com.ags.proyectofinal.databinding.FragmentLoginBinding
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser

class LoginFragment : Fragment(R.layout.fragment_login) {

    private lateinit var binding: FragmentLoginBinding

    //Para Firebase
    private lateinit var firebaseAuth: FirebaseAuth
    private var user: FirebaseUser? = null

    // Cajas de texto
    private var email = ""
    private var contrasenia = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLoginBinding.bind(view)
        firebaseAuth = FirebaseAuth.getInstance()
        user = firebaseAuth.currentUser
        if (user != null){
            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
            this.requireActivity().finish()
        }


        binding.progressBar.visibility = View.GONE

        binding.btIniciar.setOnClickListener {
            /*var error = false
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
                val intent = Intent(requireContext(), MainActivity::class.java).apply {
                    putExtra("KEY_INFO", "Login")
                    putExtra("EXTRA_BUNDLE", bundle)
                }
                startActivity(intent)
            }*/

            if (!validaCampos()) return@setOnClickListener
            binding.progressBar.visibility = View.VISIBLE
            autenticaUsuario(email,contrasenia)
        }

        binding.btRegistro.setOnClickListener {
          /*parentFragmentManager.beginTransaction()
              .replace(R.id.fgContainerView, RegisterFragment.newInstance())
              .addToBackStack("RegisterFragment")
              .commit()*/
            if(!validaCampos()) return@setOnClickListener

            binding.progressBar.visibility = View.VISIBLE

            //Registrando al usuario
            firebaseAuth.createUserWithEmailAndPassword(email, contrasenia).addOnCompleteListener { authResult->
                if(authResult.isSuccessful){
                    binding.progressBar.visibility = View.GONE
                    //Enviar correo para verificación de email
                    var user_fb = firebaseAuth.currentUser
                    user_fb?.sendEmailVerification()?.addOnSuccessListener {
                        Toast.makeText(requireContext(), getString(R.string.correoEnviado), Toast.LENGTH_SHORT).show()
                    }?.addOnFailureListener {
                        Toast.makeText(requireContext(), getString(R.string.correoNoEnviado), Toast.LENGTH_SHORT).show()
                    }

                    Toast.makeText(requireContext(), getString(R.string.usuarioCreado), Toast.LENGTH_SHORT).show()

                    val intent = Intent(requireContext(), MainActivity::class.java)
                    startActivity(intent)
                    this.requireActivity().finish()


                }else{
                    binding.progressBar.visibility = View.GONE
                    manejaErrores(authResult)
                }
            }
        }

        binding.btCatalogo.setOnClickListener {
            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
            this.requireActivity().finish()
        }

        binding.tvOlvidoContrasenia.setOnClickListener{
            val resetMail = EditText(it.context)
            resetMail.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS

            AlertDialog.Builder(it.context)
                .setTitle(getString(R.string.restablecerContraseña))
                .setMessage(R.string.correoEnlaceRestablecerContraseña)
                .setView(resetMail)
                .setPositiveButton(getString(R.string.enviar)) { _, _ ->
                    val mail = resetMail.text.toString()
                    if (mail.isNotEmpty()) {
                        firebaseAuth.sendPasswordResetEmail(mail).addOnSuccessListener {
                            Toast.makeText(
                                requireContext(),
                                getString(R.string.enlaceRestablecerContraseñaExitoso),
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }.addOnFailureListener {
                            Toast.makeText(
                                requireContext(),
                                getString(R.string.enlaceRestablecerContraseñaFallido,it.message),
                                Toast.LENGTH_SHORT
                            )
                                .show() //it tiene la excepción
                        }
                    } else {
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.favorIngresarCorreo),
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }.setNegativeButton(getString(R.string.cancelar)) { dialog, _ ->
                    dialog.dismiss()
                }
                .create()
                .show()
        }
    }

    private fun validaCampos(): Boolean{
        email = binding.etEmail.text.toString().trim() //para que quite espacios en blanco
        contrasenia = binding.etPassword.text.toString().trim()

        if(email.isEmpty()){
            binding.etEmail.error = getString(R.string.seRequiereCorreo)
            binding.etEmail.requestFocus()
            return false
        }

        if(contrasenia.isEmpty() || contrasenia.length < 6){
            binding.etEmail.error = getString(R.string.seRequiereContraseña)
            binding.etEmail.requestFocus()
            return false
        }

        return true
    }

    private fun manejaErrores(task: Task<AuthResult>){
        var errorCode = ""

        try{
            errorCode = (task.exception as FirebaseAuthException).errorCode
        }catch(e: Exception){
            e.printStackTrace()
        }

        when(errorCode){
            "ERROR_INVALID_EMAIL" -> {
                Toast.makeText(requireContext(), getString(R.string.errorCorreoFormato), Toast.LENGTH_SHORT).show()
                binding.etEmail.error = getString(R.string.errorCorreoFormato)
                binding.etPassword.requestFocus()
            }
            "ERROR_WRONG_PASSWORD" -> {
                Toast.makeText(requireContext(), getString(R.string.errorContraseñaInvalida), Toast.LENGTH_SHORT).show()
                binding.etPassword.error = getString(R.string.errorContraseñaInvalida)
                binding.etPassword.requestFocus()
                binding.etPassword.setText("")

            }
            "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL" -> {
                //An account already exists with the same email address but different sign-in credentials. Sign in using a provider associated with this email address.
                Toast.makeText(requireContext(), getString(R.string.errorContraseñaIngreso), Toast.LENGTH_SHORT).show()
            }
            "ERROR_EMAIL_ALREADY_IN_USE" -> {
                Toast.makeText(requireContext(), getString(R.string.errorCuentaYaExiste), Toast.LENGTH_LONG).show()
                binding.etEmail.error = getString(R.string.errorCuentaYaExiste)
                binding.etEmail.requestFocus()
            }
            "ERROR_USER_TOKEN_EXPIRED" -> {
                Toast.makeText(requireContext(), getString(R.string.errorSesionExpirada), Toast.LENGTH_LONG).show()
            }
            "ERROR_USER_NOT_FOUND" -> {
                Toast.makeText(requireContext(), getString(R.string.errorUsuarioInexistente), Toast.LENGTH_LONG).show()
            }
            "ERROR_WEAK_PASSWORD" -> {
                Toast.makeText(requireContext(), getString(R.string.errorContraseñaDebil), Toast.LENGTH_LONG).show()
                binding.etPassword.error = getString(R.string.errorContraseñaDebil)
                binding.etPassword.requestFocus()
            }
            "NO_NETWORK" -> {
                Toast.makeText(requireContext(), getString(R.string.errorNoConexion), Toast.LENGTH_LONG).show()
            }
            else -> {
                Toast.makeText(requireContext(), getString(R.string.errorGeneral), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun autenticaUsuario(usr: String, psw: String){
        firebaseAuth.signInWithEmailAndPassword(usr,psw).addOnCompleteListener {authResult ->
            binding.progressBar.visibility = View.GONE
            if (authResult.isSuccessful){
                Toast.makeText(requireContext(), getString(R.string.autenticacionExitosa), Toast.LENGTH_SHORT).show()
                val intent = Intent(requireContext(), MainActivity::class.java)
                startActivity(intent)
                this.requireActivity().finish()

            }
            else{
                manejaErrores(authResult)
            }
        }
    }

    companion object {
        fun newInstance() = LoginFragment()
    }
}