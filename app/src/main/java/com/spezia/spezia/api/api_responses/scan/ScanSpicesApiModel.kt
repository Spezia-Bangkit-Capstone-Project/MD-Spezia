package com.spezia.spezia.api.api_responses.scan

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ScanSpicesApiModel(
    @field:SerializedName("confidence")
    val confidence: String,

    @field:SerializedName("spiceId")
    val spiceId: String,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("latin_name")
    val latinName: String,

    @field:SerializedName("image")
    val image: String,

    @field:SerializedName("description")
    val description: String,

    @field:SerializedName("benefits")
    val benefits: List<String>
) : Parcelable