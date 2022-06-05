package com.spezia.spezia.view_model

import android.content.Context
import android.util.Log
import android.util.Patterns
import androidx.lifecycle.*
import com.spezia.spezia.R
import com.spezia.spezia.api.api_model.login.LoginResponse
import com.spezia.spezia.api.configuration.ApiConfig
import com.spezia.spezia.local_datastore.UserModel
import com.spezia.spezia.local_datastore.UserPreferences
import com.spezia.spezia.view.login.LoginActivity
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(private val pref : UserPreferences) : ViewModel() {

    private val loadingProcess = MutableLiveData<Boolean>()
    val isLoadingProcess : LiveData<Boolean> = loadingProcess

    private val validEmail = MutableLiveData<Boolean>()
    val isValidEmail : LiveData<Boolean> = validEmail

    private val emptyEmail = MutableLiveData<Boolean>()
    val isEmptyEmail : LiveData<Boolean> = emptyEmail

    private val validPw = MutableLiveData<Boolean>()
    val isValidPw : LiveData<Boolean> = validPw

    private val emptyPw = MutableLiveData<Boolean>()
    val isEmptyPw : LiveData<Boolean> = emptyPw

    private val apiMsg = MutableLiveData<String>()
    val apiMessage : LiveData<String> = apiMsg

    fun getUserInLogin() : LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }
    fun loginInLogin(user : UserModel) {
        viewModelScope.launch {
            pref.login(user)
        }
    }

    private fun validateEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun validatePw(pw: String): Boolean {
        return pw.length > 8
    }

    private fun notEmptyField(email: String, pw: String): Boolean {
        var isNotEmptyField = true
        if (email.isEmpty()) {
            emptyEmail.value = true
            isNotEmptyField = false
        }
        if (pw.isEmpty()) {
            emptyPw.value = true
            isNotEmptyField = false
        }
        return isNotEmptyField
    }

    private fun checkField(email: String, pw: String) : Boolean {
        var isCorrected = true
        if (notEmptyField(email, pw)) {
            if (!validateEmail(email)) {
                validEmail.value = false
                isCorrected = false
            } else if (!validatePw(pw)) {
                validPw.value = false
                isCorrected = false
            }
        } else {
            isCorrected = false
        }
        return isCorrected
    }

    fun loginToApp(context : Context, email: String, pw: String){
        if (checkField(email, pw)) {
            val client = ApiConfig.getApiService().loginAccount(email, pw)
            loadingProcess.value = true
            Log.d("LoginViewModel", "Not yet validated")
            client.enqueue(object : Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    val respBody = response.body()
                    Log.d("LoginViewModel :", "Response Body Check")
                    if (response.isSuccessful && respBody != null) {
                        Log.d("LoginViewModel :", "Response Body Check - Success")
                        val error = respBody.error
                        if (!error) {
                            Log.d("LoginViewModel :","Response Body Check - Success 2")
                            val user = UserModel(
                                respBody.loginResult.userId,
                                respBody.loginResult.username,
                                respBody.loginResult.email,
                                respBody.loginResult.token,
                                true
                            )
                            loginInLogin(user)
                            loadingProcess.value = false
                            apiMsg.value = context.getString(R.string.login_success2)
                            Log.e(LoginActivity.TAG, "onSuccess: ${respBody.message}")
                        } else {
                            loadingProcess.value = false
                            apiMsg.value = context.getString(R.string.login_failed)
                        }
                    } else {
                        loadingProcess.value = false
                        apiMsg.value = context.getString(R.string.wrong_email_pw)
                        Log.e(LoginActivity.TAG, "onFailure: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    loadingProcess.value = false
                    apiMsg.value = context.getString(R.string.api_message_error_on_server)
                    Log.e(LoginActivity.TAG, "onFailure: ${t.message}")
                }

            })
        }
    }

}