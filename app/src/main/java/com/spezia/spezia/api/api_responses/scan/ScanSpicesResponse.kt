package com.spezia.spezia.api.api_responses.scan

import com.google.gson.annotations.SerializedName

data class ScanSpicesResponse(

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("data")
	val data: ScanSpicesApiModel
)