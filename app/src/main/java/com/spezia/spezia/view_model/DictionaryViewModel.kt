package com.spezia.spezia.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.spezia.spezia.api.api_responses.dictionary.DictionaryApiModel
import com.spezia.spezia.api.api_responses.dictionary.DictionaryResponse
import com.spezia.spezia.api.configuration.ApiConfig
import com.spezia.spezia.local_datastore.UserModel
import com.spezia.spezia.local_datastore.UserPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DictionaryViewModel(private val pref : UserPreferences) : ViewModel() {

    private val loadingProcess = MutableLiveData<Boolean>()
    val isLoadingProcess : LiveData<Boolean> = loadingProcess

    private val apiMsg = MutableLiveData<String>()
    val apiMessage: LiveData<String> = apiMsg

    val allSpicesDictionary = MutableLiveData<ArrayList<DictionaryApiModel>>()

    fun getAllSpicesDictionary(token : String) {
        val listSpicesDictionary = ArrayList<DictionaryApiModel>()
        val client = ApiConfig.getApiService().getAllSpicesDictionary("Bearer $token")
        loadingProcess.value = true

        client.enqueue(object : Callback<DictionaryResponse> {
            override fun onResponse(
                call: Call<DictionaryResponse>,
                response: Response<DictionaryResponse>
            ) {
                val respBody = response.body()
                if (response.isSuccessful && respBody != null) {
                    Log.d("DictionaryActivity", "Token : $token")
                    Log.d("DictionaryActivity", "Message : ${respBody.message}")
                    allSpicesDictionary.postValue(respBody.data)
                    allSpicesDictionary.value = listSpicesDictionary
                } else {
                    Log.d("DictionaryActivity", "Token : $token")
                    loadingProcess.value = false
                    apiMsg.value = respBody?.message
                    Log.e("DictionaryActivity","onFailure : ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DictionaryResponse>, t: Throwable) {
                Log.d("DictionaryActivity", "Token : $token")
                loadingProcess.value = false
                apiMsg.value = t.message
                Log.e("DictionaryActivity", "onFailure : ${t.message}")
            }

        })
    }

    fun getUser() : LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }

}