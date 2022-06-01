package com.spezia.spezia.api.api_model.register

import com.google.gson.annotations.SerializedName

data class SuccessfulRegisterResponse(

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String
)
