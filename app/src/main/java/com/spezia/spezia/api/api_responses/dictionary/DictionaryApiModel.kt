package com.spezia.spezia.api.api_responses.dictionary

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class DictionaryApiModel(

    @field:SerializedName("spiceId")
    val spiceId: String,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("latin_name")
    val latin_name: String,

    @field:SerializedName("image")
    val image: String,

    @field:SerializedName("description")
    val description: String,

    @field:SerializedName("benefits")
    val benefits: List<String>,
) : Parcelable
