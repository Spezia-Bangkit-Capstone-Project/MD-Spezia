package com.spezia.spezia.view.profile

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.spezia.spezia.R
import com.spezia.spezia.databinding.ActivityProfileBinding
import com.spezia.spezia.local_datastore.UserModel
import com.spezia.spezia.local_datastore.UserPreferences
import com.spezia.spezia.utils.ViewModelFactory
import com.spezia.spezia.view.dictionary.DictionaryActivity
import com.spezia.spezia.view.main_menu.MainActivity
import com.spezia.spezia.view.welcome_screen.WelcomeActivity
import com.spezia.spezia.view_model.ProfileViewModel

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class ProfileActivity : AppCompatActivity() {

    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var binding: ActivityProfileBinding
    private lateinit var user :UserModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        setupViewModel()
        setupActionOnTopBackButton()
        setupProfileItemMenuClickAction()
    }

    private fun setupActionOnTopBackButton() {
        binding.whiteCircleBackProfile.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setupProfileItemMenuClickAction() {
        binding.cvProfileLanguage.setOnClickListener {
            launchIntentImplicitLanguageSettings()
        }

        binding.cvProfileAbout.setOnClickListener {
            moveToAboutActivity()
        }

        binding.cvProfileLogout.setOnClickListener {
            goLogout()
        }
    }

    private fun goLogout() {
        AlertDialog.Builder(this).apply {
            setCancelable(false)
            setIcon(R.drawable.ic_warning)
            setTitle(getString(R.string.logout))
            setMessage(getString(R.string.logout_confirmation))
            setPositiveButton(getString(R.string.logout)) { _, _ ->
                profileViewModel.logoutInProfile()
            }
            setNegativeButton(getString(R.string.cancel)){ dialog, _ ->
                dialog.cancel()
            }
            create()
            show()
        }
    }

    private fun moveToAboutActivity() {
        startActivity(Intent(this, AboutActivity::class.java))
    }

    private fun launchIntentImplicitLanguageSettings() {
        startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
    }

    private fun setupViewModel() {
        profileViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreferences.getInstance(dataStore))
        )[ProfileViewModel::class.java]

        profileViewModel.getUserInProfile().observe(this) { user ->
            if (user.isLogin) {
                binding.textName.text = getString(R.string.greeting, user.username)
                binding.textEmail.text = user.email
            } else {
                finish()
            }
        }
    }

    override fun onBackPressed() {
        val i = Intent(this, MainActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(i)
        finish()
    }

    companion object{
        const val EXTRA_TOKEN_PROFILE = "extra_token_profile"
    }
}