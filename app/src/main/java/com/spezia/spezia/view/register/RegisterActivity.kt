package com.spezia.spezia.view.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.spezia.spezia.R
import com.spezia.spezia.databinding.ActivityRegisterBinding
import com.spezia.spezia.view.login.LoginActivity
import com.spezia.spezia.view.welcome_screen.WelcomeActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.btnRBack.setOnClickListener {
            startActivity(Intent(this, WelcomeActivity::class.java))
        }
        binding.txtLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}