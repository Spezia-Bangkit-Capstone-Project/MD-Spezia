package com.spezia.spezia.view.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.spezia.spezia.R
import com.spezia.spezia.databinding.ActivityLoginBinding
import com.spezia.spezia.view.register.RegisterActivity
import com.spezia.spezia.view.welcome_screen.WelcomeActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.btnLBack.setOnClickListener {
            startActivity(Intent(this, WelcomeActivity::class.java))
        }
        binding.txtRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}