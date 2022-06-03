package com.spezia.spezia.view

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.spezia.spezia.R

class ScanActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)
    }

    companion object{
        const val EXTRA_TOKEN_SCAN = "extra_token_scan"
    }
}