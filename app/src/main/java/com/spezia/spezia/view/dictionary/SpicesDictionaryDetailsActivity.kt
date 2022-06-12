package com.spezia.spezia.view.dictionary

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.bumptech.glide.Glide
import com.spezia.spezia.R
import com.spezia.spezia.api.api_responses.dictionary.DictionaryApiModel
import com.spezia.spezia.databinding.ActivitySpicesDictionaryDetailsBinding
import com.spezia.spezia.view.main_menu.MainActivity

class SpicesDictionaryDetailsActivity : AppCompatActivity() {

    private lateinit var binding :ActivitySpicesDictionaryDetailsBinding

    companion object {
        const val EXTRA_SPICES_DICTIONARY_DETAILS = "extra_spices_dictionary_details"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val spicesDictionaryMetadata = intent.getParcelableExtra<DictionaryApiModel>(
            EXTRA_SPICES_DICTIONARY_DETAILS) as DictionaryApiModel
        Log.d("DictionaryDetailsAct : ", spicesDictionaryMetadata.toString())

        binding = ActivitySpicesDictionaryDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.backIcon.setOnClickListener {
            onBackPressed()
        }

        Glide.with(this).load(spicesDictionaryMetadata.image).into(binding.imgSpice)

        binding.apply {
            tvSpiceName.text = spicesDictionaryMetadata.name
            tvScientificName.text = spicesDictionaryMetadata.latin_name
            tvSpiceDescription.text = spicesDictionaryMetadata.description
            tvSpiceBenefits.text = spicesDictionaryMetadata.benefits.toString()
        }
    }

    override fun onBackPressed() {
        Intent.FLAG_ACTIVITY_CLEAR_TOP
        finish()
    }
}