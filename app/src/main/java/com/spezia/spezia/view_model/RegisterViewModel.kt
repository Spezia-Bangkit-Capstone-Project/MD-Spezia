package com.spezia.spezia.view_model

import android.content.Context
import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.spezia.spezia.R
import com.spezia.spezia.api.api_model.register.SuccessfulRegisterResponse
import com.spezia.spezia.api.configuration.ApiConfig
import com.spezia.spezia.local_datastore.UserPreferences
import com.spezia.spezia.view.register.RegisterActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel(private val pref : UserPreferences) : ViewModel() {

    private val loadingProcess = MutableLiveData<Boolean>()
    val isLoadingProcess : LiveData<Boolean> = loadingProcess

    private val emptyName = MutableLiveData<Boolean>()
    val isEmptyName : LiveData<Boolean> = emptyName

    private val validEmail = MutableLiveData<Boolean>()
    val isValidEmail : LiveData<Boolean> = validEmail

    private val emptyEmail = MutableLiveData<Boolean>()
    val isEmptyEmail : LiveData<Boolean> = emptyEmail

    private val emptyPw = MutableLiveData<Boolean>()
    val isEmptyPw : LiveData<Boolean> = emptyPw

    private val validPw = MutableLiveData<Boolean>()
    val isValidPw : LiveData<Boolean> = validPw

    private val apiMsg = MutableLiveData<String>()
    val apiMessage : LiveData<String> = apiMsg

    private fun validatePw(pw: String) : Boolean{
        return pw.length > 8
    }

    private fun validateEmail(email: String) : Boolean{
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun notEmptyField(username : String, email : String, pw : String) : Boolean {
        var isNotEmptyField = true
        when {
            username.isEmpty() -> {
                emptyName.value = true
                isNotEmptyField = false
            }
            email.isEmpty() -> {
                emptyEmail.value = true
                isNotEmptyField = false
            }
            pw.isEmpty() -> {
                emptyPw.value = true
                isNotEmptyField = false
            }
        }
        return isNotEmptyField
    }

    private fun validate(username : String, email: String, pw: String) : Boolean {
        var isValidate = true
        if (notEmptyField(username, email, pw)) {
            if (!validateEmail(email)){
                validEmail.value = false
                isValidate = false
            } else if (!validatePw(pw)){
                validPw.value = false
                isValidate = false
            }
        }
        else {
            isValidate = false
        }
        return isValidate
    }

    fun registerAccount(context : Context, username : String, email: String, pw: String) {
        if (validate(username, email, pw)){
            val client = ApiConfig.getApiService().registerAccount(username, email, pw)
            loadingProcess.value = true
            client.enqueue(object : Callback<SuccessfulRegisterResponse> {
                override fun onResponse(
                    call: Call<SuccessfulRegisterResponse>,
                    response: Response<SuccessfulRegisterResponse>
                ) {
                    val respBody = response.body()
                    Log.d("RegisterViewModel : ", "Response Body Check")

                    if (response.isSuccessful && respBody != null) {
                        Log.d("RegisterViewModel : ", "Response Body Check - Success")
                        val err = respBody.error
                        if (!err) {
                            Log.d("RegisterViewModel:","Response Body Check - Success 2")
                            Log.d("RegisterViewModel:","Message - ${respBody.message}")
                            loadingProcess.value = false
                            apiMsg.value = context.getString(R.string.account_created_successfully)
                        } else {
                            loadingProcess.value = true
                            apiMsg.value = context.getString(R.string.api_message_registration_failed)
                        }
                    } else {
                        Log.d("RegisterViewModel:", "Response body success, BUT THE EMAIL HAS BEEN USED!")
                        loadingProcess.value = false
                        apiMsg.value = context.getString(R.string.api_message_email_has_been_used)
                        Log.d(RegisterActivity.TAG, "onFailure : ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<SuccessfulRegisterResponse>, t: Throwable) {
                    loadingProcess.value = true
                    apiMsg.value = context.getString(R.string.api_message_error_on_server)
                    Log.e(RegisterActivity.TAG, "onFailure : ${t.message}")
                }
            })
        }
    }

}