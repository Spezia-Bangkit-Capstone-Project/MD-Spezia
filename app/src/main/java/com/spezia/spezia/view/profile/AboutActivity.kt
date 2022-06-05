package com.spezia.spezia.view.profile

import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import com.spezia.spezia.R
import com.spezia.spezia.databinding.ActivityAboutBinding
import com.spezia.spezia.databinding.ActivityProfileBinding

class AboutActivity : AppCompatActivity() {

    private lateinit var binding : ActivityAboutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.backIcon.setOnClickListener {
            onBackPressed()
        }

        binding.cvSpeziaDefinition.setOnClickListener {
            if (binding.descSpezia.visibility == View.GONE) {
                TransitionManager.beginDelayedTransition(binding.cvSpeziaDefinition, AutoTransition())
                binding.descSpezia.visibility = View.VISIBLE
                binding.downArrow1.background = AppCompatResources.getDrawable(this, R.drawable.ic_up_arrow)
            } else {
                TransitionManager.beginDelayedTransition(binding.cvSpeziaDefinition, AutoTransition())
                binding.descSpezia.visibility = View.GONE
                binding.downArrow1.background = AppCompatResources.getDrawable(this, R.drawable.ic_down_arrow)
            }
        }

        binding.cvAppDeveloper.setOnClickListener {
            if (binding.descAppDev.visibility == View.GONE) {
                TransitionManager.beginDelayedTransition(binding.cvAppDeveloper, AutoTransition())
                binding.descAppDev.visibility = View.VISIBLE
                binding.downArrow2.background = AppCompatResources.getDrawable(this, R.drawable.ic_up_arrow)
            } else {
                TransitionManager.beginDelayedTransition(binding.cvAppDeveloper, AutoTransition())
                binding.descAppDev.visibility = View.GONE
                binding.downArrow2.background = AppCompatResources.getDrawable(this, R.drawable.ic_down_arrow)
            }
        }

        binding.cvResources.setOnClickListener {
            if (binding.descResources.visibility == View.GONE) {
                TransitionManager.beginDelayedTransition(binding.cvResources, AutoTransition())
                binding.descResources.visibility = View.VISIBLE
                binding.downArrow3.background = AppCompatResources.getDrawable(this, R.drawable.ic_up_arrow)
            } else {
                TransitionManager.beginDelayedTransition(binding.cvResources, AutoTransition())
                binding.descResources.visibility = View.GONE
                binding.downArrow3.background = AppCompatResources.getDrawable(this, R.drawable.ic_down_arrow)
            }
        }

        binding.cvContactUs.setOnClickListener {
            if (binding.descContactUs.visibility == View.GONE) {
                TransitionManager.beginDelayedTransition(binding.cvContactUs, AutoTransition())
                binding.descContactUs.visibility = View.VISIBLE
                binding.downArrow4.background = AppCompatResources.getDrawable(this, R.drawable.ic_up_arrow)
            } else {
                TransitionManager.beginDelayedTransition(binding.cvContactUs, AutoTransition())
                binding.descContactUs.visibility = View.GONE
                binding.downArrow4.background = AppCompatResources.getDrawable(this, R.drawable.ic_down_arrow)
            }
        }

        binding.cvAppVersion.setOnClickListener {
            if (binding.descAppVersion.visibility == View.GONE) {
                TransitionManager.beginDelayedTransition(binding.cvAppVersion, AutoTransition())
                binding.descAppVersion.visibility = View.VISIBLE
                binding.downArrow5.background = AppCompatResources.getDrawable(this, R.drawable.ic_up_arrow)
            } else {
                TransitionManager.beginDelayedTransition(binding.cvAppVersion, AutoTransition())
                binding.descAppVersion.visibility = View.GONE
                binding.downArrow5.background = AppCompatResources.getDrawable(this, R.drawable.ic_down_arrow)
            }
        }
    }


}