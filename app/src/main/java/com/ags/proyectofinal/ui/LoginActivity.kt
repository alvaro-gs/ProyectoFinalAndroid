package com.ags.proyectofinal.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.ags.proyectofinal.R
import com.ags.proyectofinal.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        val screenSplash = installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Thread.sleep(1000)
        screenSplash.setKeepOnScreenCondition{false}
        supportFragmentManager.beginTransaction()
            .replace(R.id.fgContainerView,LoginFragment.newInstance())
            .commit()


    }

}