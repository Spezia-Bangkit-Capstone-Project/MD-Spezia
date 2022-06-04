package com.spezia.spezia.view.main_menu

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.spezia.spezia.databinding.ActivityMainBinding
import com.spezia.spezia.local_datastore.UserModel
import com.spezia.spezia.local_datastore.UserPreferences
import com.spezia.spezia.utils.ViewModelFactory
import com.spezia.spezia.view.article.ArticleActivity
import com.spezia.spezia.view.dictionary.DictionaryActivity
import com.spezia.spezia.view.profile.ProfileActivity
import com.spezia.spezia.view.scan.ScanActivity
import com.spezia.spezia.view.welcome_screen.WelcomeActivity
import com.spezia.spezia.view_model.MainViewModel

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var binding : ActivityMainBinding
    private lateinit var user : UserModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        setupViewModel()
        setupMainMenuAction()

    }

    private fun setupMainMenuAction() {
        binding.menuScan.setOnClickListener {
            shareTokenAndMoveToScanActivity()
        }

        binding.menuDictionary.setOnClickListener {
            shareTokenAndMoveToDictionaryActivity()
        }

        binding.menuArticle.setOnClickListener {
            moveToArticleActivity()
        }

        binding.menuProfile.setOnClickListener {
            shareTokenAndMoveToProfileActivity()
        }
    }

    private fun shareTokenAndMoveToProfileActivity() {
        mainViewModel.getUser().observe(this) { user ->
            this.user = user
            Log.d("Move Activity","Your Token : ${user.token}")

            if (this.user.isLogin) {
                val intent = Intent(this, ProfileActivity::class.java)
                intent.let {
                    it.putExtra(ProfileActivity.EXTRA_TOKEN_PROFILE, user.token)
                    startActivity(it)
                }
            }
        }
    }

    private fun moveToArticleActivity() {
        startActivity(Intent(this, ArticleActivity::class.java))
    }

    private fun shareTokenAndMoveToDictionaryActivity() {
        mainViewModel.getUser().observe(this) { user ->
            this.user = user
            Log.d("Move Activity","Your Token : ${user.token}")

            if (this.user.isLogin) {
                val intent = Intent(this, DictionaryActivity::class.java)
                intent.let {
                    it.putExtra(DictionaryActivity.EXTRA_TOKEN_DICTIONARY, user.token)
                    startActivity(it)
                }
            }
        }
    }

    private fun shareTokenAndMoveToScanActivity() {
        mainViewModel.getUser().observe(this) { user ->
            this.user = user
            Log.d("Move Activity","Your Token : ${user.token}")

            if (this.user.isLogin) {
                val intent = Intent(this, ScanActivity::class.java)
                intent.let {
                    it.putExtra(ScanActivity.EXTRA_TOKEN_SCAN, user.token)
                    startActivity(it)
                }
            }
        }
    }

    private fun setupViewModel() {
        mainViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreferences.getInstance(dataStore))
        )[MainViewModel::class.java]

        mainViewModel.getUser().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            }
        }
    }

    companion object {
        const val EXTRA_MAIN_MENU = "extra_main_menu"
    }
}