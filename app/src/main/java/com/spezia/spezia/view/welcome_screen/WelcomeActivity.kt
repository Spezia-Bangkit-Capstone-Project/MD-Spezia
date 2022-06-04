package com.spezia.spezia.view.welcome_screen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.spezia.spezia.databinding.ActivityWelcomeBinding
import com.spezia.spezia.view.login.LoginActivity
import com.spezia.spezia.view.register.RegisterActivity

class WelcomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        setupWelcomeAction()
    }

    private fun setupWelcomeAction() {
        binding.btn1.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
        binding.btn2.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}