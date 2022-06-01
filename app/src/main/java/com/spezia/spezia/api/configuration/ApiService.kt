package com.spezia.spezia.api.configuration

import com.spezia.spezia.api.api_model.login.LoginResponse
import com.spezia.spezia.api.api_model.register.SuccessfulRegisterResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @FormUrlEncoded
    @POST("register")
    fun registerAccount(
        @Field("username") username : String,
        @Field("email") email : String,
        @Field("password") password : String
    ) : Call<SuccessfulRegisterResponse>

    @FormUrlEncoded
    @POST("login")
    fun loginAccount(
        @Field("email") email: String,
        @Field("password") password: String
    ) : Call<LoginResponse>

}