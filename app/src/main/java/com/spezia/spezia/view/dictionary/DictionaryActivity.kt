package com.spezia.spezia.view.dictionary

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.spezia.spezia.R
import com.spezia.spezia.adapter.ListSpicesDictionaryRVAdapter
import com.spezia.spezia.api.api_responses.dictionary.DictionaryApiModel
import com.spezia.spezia.databinding.ActivityDictionaryBinding
import com.spezia.spezia.local_datastore.UserPreferences
import com.spezia.spezia.utils.ViewModelFactory
import com.spezia.spezia.view.main_menu.MainActivity
import com.spezia.spezia.view_model.DictionaryViewModel

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class DictionaryActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDictionaryBinding
    private lateinit var dictionaryViewModel: DictionaryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDictionaryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        setupDictionaryViewModel()
        setupBackIcon()
    }

    private fun setupBackIcon() {
        binding.backIcon.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setupDictionaryViewModel() {
        dictionaryViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreferences.getInstance(dataStore))
        )[DictionaryViewModel::class.java]

        dictionaryViewModel.allSpicesDictionary.observe(this){
            setAdapter(it)
            isLoadingProcess(false)
        }

        dictionaryViewModel.getUser().observe(this) { user ->
            Log.d("DictionaryActivity", "isLogin : ${user.isLogin}")
            if (user.isLogin) {
                user.token.let { dictionaryViewModel.getAllSpicesDictionary(user.token) }
            }
        }

        dictionaryViewModel.isLoadingProcess.observe(this) {
            isLoadingProcess(it)
        }

        dictionaryViewModel.apiMessage.observe(this) {
            Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun isLoadingProcess(lP : Boolean) {
        if (lP) {
            binding.whiteCircleBgDictionary.visibility = View.VISIBLE
            binding.loadingIndicatorDictionary.visibility = View.VISIBLE
            binding.rvDictionarySpices.visibility = View.GONE
        } else {
            binding.whiteCircleBgDictionary.visibility = View.GONE
            binding.loadingIndicatorDictionary.visibility = View.GONE
            binding.rvDictionarySpices.visibility = View.VISIBLE
        }
    }

    private fun setAdapter(listAllSpicesDictionary : ArrayList<DictionaryApiModel>) {
        val rvAdapter = ListSpicesDictionaryRVAdapter(listAllSpicesDictionary)
        binding.apply {
            rvDictionarySpices.layoutManager = LinearLayoutManager(this@DictionaryActivity)
            rvDictionarySpices.setHasFixedSize(true)
            rvDictionarySpices.adapter = rvAdapter
        }
    }

    override fun onBackPressed() {
        val i = Intent(this, MainActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(i)
        finish()
    }

    companion object{
        const val EXTRA_TOKEN_DICTIONARY = "extra_token_dictionary"
    }
}