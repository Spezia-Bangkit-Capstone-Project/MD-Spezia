package com.spezia.spezia.view.login

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.spezia.spezia.MainActivity
import com.spezia.spezia.R
import com.spezia.spezia.databinding.ActivityLoginBinding
import com.spezia.spezia.local_datastore.UserModel
import com.spezia.spezia.local_datastore.UserPreferences
import com.spezia.spezia.utils.ViewModelFactory
import com.spezia.spezia.view.register.RegisterActivity
import com.spezia.spezia.view.welcome_screen.WelcomeActivity
import com.spezia.spezia.view_model.LoginViewModel

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var user : UserModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        setupViewModel()
        setupLoginButtonClickAction()
        setupActionOnTopBackButton()

    }

    private fun setupActionOnTopBackButton() {
        binding.btnLBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setupLoginButtonClickAction() {
        binding.lBtn1.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val pw = binding.passwordEditText.text.toString()

            loginViewModel.login(this, email, pw)
        }
    }

    private fun setupViewModel() {
        loginViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreferences.getInstance(dataStore))
        )[LoginViewModel::class.java]

        loginViewModel.getUser().observe(this) { user ->
            Log.d("Login Successful : ", user.token)
            this.user = user
            Log.d("LoginActivity", "Your Token : ${user.token}")

            if (this.user.isLogin) {
                AlertDialog.Builder(this).apply {
                    setCancelable(false)
                    setIcon(R.drawable.ic_success)
                    setTitle(getString(R.string.awesome))
                    setMessage(getString(R.string.login_success))
                    setPositiveButton(getString(R.string.continueAlertDialog)) { _, _ ->
                        val intent = Intent(context, MainActivity::class.java)
                        intent.let {
                            it.putExtra(MainActivity.EXTRA_MAIN_MENU, user.token)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(it)
                            finish()
                        }
                    }
                    create()
                    show()
                }
            }
        }

        loginViewModel.isLoadingProcess.observe(this) {
            isLoading(it)
        }

        loginViewModel.apiMessage.observe(this) { msg ->
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        }

        loginViewModel.isEmptyEmail.observe(this) {
            if (it) binding.emailEditText.error = getString(R.string.invalid_email_empty)
        }

        loginViewModel.isEmptyPw.observe(this) {
            if (it) binding.passwordEditText.error = getString(R.string.invalid_pw_empty)
        }

        loginViewModel.isValidEmail.observe(this) {
            if (!it) binding.emailEditText.error =
                getString(R.string.email_invalid) else binding.emailEditText.error = null
        }

        loginViewModel.isValidPw.observe(this) {
            if (!it) binding.passwordEditText.error =
                getString(R.string.password_invalid) else binding.passwordEditText.error = null
        }

    }

    private fun isLoading(v:Boolean){
        if (v){
            binding.lBtn1.isEnabled = false
            binding.whiteCircleBgLogin.visibility = View.VISIBLE
            binding.loadingIndicatorLogin.visibility = View.VISIBLE
        } else {
            binding.whiteCircleBgLogin.visibility = View.GONE
            binding.loadingIndicatorLogin.visibility = View.GONE
            binding.lBtn1.isEnabled = true
        }
    }

    companion object{
        const val TAG = "LoginActivity"
    }
}