package com.spezia.spezia.api.api_responses.login

import com.google.gson.annotations.SerializedName

data class LoginResponse(

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("data")
	val data: LoginApiModel,
)
