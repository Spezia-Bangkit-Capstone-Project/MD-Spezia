package com.spezia.spezia.api.api_responses.dictionary

import com.google.gson.annotations.SerializedName

data class DictionaryResponse(

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("spicesResult")
	val spicesResult: ArrayList<DictionaryApiModel>
)