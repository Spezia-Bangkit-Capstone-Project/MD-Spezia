package com.spezia.spezia.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.spezia.spezia.local_datastore.UserModel
import com.spezia.spezia.local_datastore.UserPreferences
import kotlinx.coroutines.launch

class MainViewModel(private val pref : UserPreferences) : ViewModel() {

    fun getUser() : LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            pref.logout()
        }
    }

}