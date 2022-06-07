package com.spezia.spezia.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.spezia.spezia.local_datastore.UserPreferences
import com.spezia.spezia.view_model.*
import java.lang.IllegalArgumentException

class ViewModelFactory(private val pref : UserPreferences) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(DictionaryViewModel::class.java) -> {
                DictionaryViewModel(pref) as T
            }
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(pref) as T
            }
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(pref) as T
            }
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(pref) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(pref) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel Class : " + modelClass.name)
        }
    }

}