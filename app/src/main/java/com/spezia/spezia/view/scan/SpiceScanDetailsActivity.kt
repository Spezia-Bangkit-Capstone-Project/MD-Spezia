package com.spezia.spezia.view.scan

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.bumptech.glide.Glide
import com.spezia.spezia.api.api_responses.scan.ScanSpicesApiModel
import com.spezia.spezia.databinding.ActivitySpiceScanDetailsBinding

class SpiceScanDetailsActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySpiceScanDetailsBinding

    companion object {
        const val EXTRA_SPICES_SCAN_DETAILS = "extra_spices_scan_details"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val spiceScanDetailsMetadata = intent.getParcelableExtra<ScanSpicesApiModel>(
            EXTRA_SPICES_SCAN_DETAILS) as ScanSpicesApiModel
        Log.d("ScanDetailsAct : ", spiceScanDetailsMetadata.toString())

        binding = ActivitySpiceScanDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.backIcon.setOnClickListener {
            onBackPressed()
        }

        Glide.with(this).load(spiceScanDetailsMetadata.image).into(binding.imgSpice)

        binding.apply {
            tvSpiceName.text = spiceScanDetailsMetadata.name
            tvScientificName.text = spiceScanDetailsMetadata.latinName
            tvSpiceDescription.text = spiceScanDetailsMetadata.description
            tvSpiceBenefits.text = spiceScanDetailsMetadata.benefits.toString()
        }
    }
}