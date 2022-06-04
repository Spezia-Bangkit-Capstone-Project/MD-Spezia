package com.spezia.spezia.view.register

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.spezia.spezia.R
import com.spezia.spezia.databinding.ActivityRegisterBinding
import com.spezia.spezia.local_datastore.UserPreferences
import com.spezia.spezia.utils.ViewModelFactory
import com.spezia.spezia.view_model.RegisterViewModel

private val Context.dataStore : DataStore<Preferences> by preferencesDataStore(name = "settings")

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var registerViewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        setupViewModel()
        setupRegisterButtonClickAction()
        setupBackButtonClickAction()
    }

    private fun setupBackButtonClickAction() {
        binding.btnRBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setupRegisterButtonClickAction() {
        binding.rBtn1.setOnClickListener {
            val username = binding.nameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val pw = binding.passwordEditText.text.toString()
            registerViewModel.registerAccount(this, username, email, pw)
        }
    }

    private fun setupViewModel() {
        registerViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreferences.getInstance(dataStore))
        )[RegisterViewModel::class.java]

        registerViewModel.isLoadingProcess.observe(this){
            isLoading(it)
        }

        registerViewModel.apiMessage.observe(this) { apiMessage ->
            Snackbar.make(binding.svr, apiMessage, Snackbar.LENGTH_LONG).show()
        }

        registerViewModel.isEmptyName.observe(this) {
            if (it) binding.nameEditText.error = getString(R.string.invalid_username_empty)
        }

        registerViewModel.isEmptyEmail.observe(this) {
            if (it) binding.emailEditText.error = getString(R.string.invalid_email_empty)
        }

        registerViewModel.isEmptyPw.observe(this) {
            if (it) binding.passwordEditText.error = getString(R.string.invalid_pw_empty)
        }

        registerViewModel.isValidEmail.observe(this) {
            if (!it) {
                binding.emailEditText.error = getString(R.string.email_invalid)
            } else {
                binding.emailEditText.error = null
            }
        }

        registerViewModel.isValidPw.observe(this) {
            if (!it) {
                binding.passwordEditText.error = getString(R.string.password_invalid)
            } else {
                binding.passwordEditText.error = null
            }
        }
    }

    private fun isLoading(l: Boolean) {
        if (l) {
            binding.rBtn1.isEnabled = false
            binding.whiteCircleBgRegister.visibility = View.VISIBLE
            binding.loadingIndicatorRegister.visibility = View.VISIBLE
        } else {
            binding.rBtn1.isEnabled = true
            binding.whiteCircleBgRegister.visibility = View.GONE
            binding.loadingIndicatorRegister.visibility = View.GONE
        }
    }

    companion object {
        const val TAG = "RegisterActivity"
    }
}