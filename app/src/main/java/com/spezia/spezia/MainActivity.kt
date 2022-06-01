package com.spezia.spezia

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.spezia.spezia.databinding.ActivityMainBinding
import com.spezia.spezia.local_datastore.UserModel
import com.spezia.spezia.local_datastore.UserPreferences
import com.spezia.spezia.utils.ViewModelFactory
import com.spezia.spezia.view.welcome_screen.WelcomeActivity
import com.spezia.spezia.view_model.MainViewModel

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding
    private lateinit var user : UserModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()

        binding.btnLogout.setOnClickListener {
            AlertDialog.Builder(this).apply {
                setCancelable(false)
                setIcon(R.drawable.ic_warning)
                setTitle(getString(R.string.logout))
                setMessage(getString(R.string.logout_confirmation))
                setPositiveButton(getString(R.string.logout)) { _, _ ->
                    mainViewModel.logout()
                }
                setNegativeButton(getString(R.string.cancel)){ dialog, _ ->
                    dialog.cancel()
                }
                create()
                show()
            }
        }

    }

    private fun setupViewModel() {
        mainViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreferences.getInstance(dataStore))
        )[MainViewModel::class.java]

        mainViewModel.getUser().observe(this) { user ->
            if (user.isLogin) {
                binding.hiTextView.text = getString(R.string.greeting, user.username)
            } else {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    companion object {
        const val EXTRA_MAIN_MENU = "extra_main_menu"
    }
}