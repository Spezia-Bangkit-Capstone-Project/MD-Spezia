package com.spezia.spezia.view.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.spezia.spezia.R

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        supportActionBar?.hide()
    }

    companion object{
        const val EXTRA_TOKEN_PROFILE = "extra_token_profile"
    }
}