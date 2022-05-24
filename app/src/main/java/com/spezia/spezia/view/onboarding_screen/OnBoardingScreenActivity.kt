package com.spezia.spezia.view.onboarding_screen

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.spezia.spezia.MainActivity
import com.spezia.spezia.R
import com.spezia.spezia.adapter.OnBoardingScreenViewPagerAdapter
import com.spezia.spezia.databinding.ActivityOnBoardingScreenBinding
import com.spezia.spezia.model.OnBoardingScreenData

class OnBoardingScreenActivity : AppCompatActivity() {

    private var onBoardingScreenViewPagerAdapter : OnBoardingScreenViewPagerAdapter? = null
    private var onBoardingScreenViewPager : ViewPager? = null
    private lateinit var binding: ActivityOnBoardingScreenBinding
    var position = 0
    private var sharedPrefs : SharedPreferences? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (restorePrefOnBoardingData() == true) {
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        supportActionBar?.hide()

        binding = ActivityOnBoardingScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val onBoardingScreenData : MutableList<OnBoardingScreenData> =
            ArrayList<OnBoardingScreenData>().apply {

                add(
                    OnBoardingScreenData(
                        title = getString(R.string.onBoarding_title1),
                        description = getString(R.string.onBoarding_desc1),
                        imgUrl = R.drawable.identify_spices
                    )
                )
                add(
                    OnBoardingScreenData(
                        title = getString(R.string.onBoarding_title2),
                        description = getString(R.string.onBoarding_desc2),
                        imgUrl = R.drawable.learn_spice_specieces
                    )
                )
            }
        setOnBoardingViewPagerAdapter(onBoardingScreenData)

        position = onBoardingScreenViewPager!!.currentItem

        binding.nextOnBoarding.setOnClickListener {
            if (position < onBoardingScreenData.size) {
                position++
                onBoardingScreenViewPager?.currentItem = position
            }
            if (position == onBoardingScreenData.size) {
                savePrefOnBoardingData()
                val intent = Intent(applicationContext, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        binding.tabLayoutIndicator.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                position = tab!!.position
                if (tab.position == onBoardingScreenData.size - 1) {
                    binding.nextOnBoarding.text = getString(R.string.get_started)
                } else {
                    binding.nextOnBoarding.text = getString(R.string.next)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) { }

            override fun onTabReselected(tab: TabLayout.Tab?) { }
        })
    }

    private fun setOnBoardingViewPagerAdapter(onBoardingScreenData : List<OnBoardingScreenData>) {

        onBoardingScreenViewPager = findViewById(R.id.screenForViewPager)
        onBoardingScreenViewPagerAdapter = OnBoardingScreenViewPagerAdapter(this, onBoardingScreenData)
        onBoardingScreenViewPager?.adapter = onBoardingScreenViewPagerAdapter

        binding.tabLayoutIndicator.setupWithViewPager(onBoardingScreenViewPager)

    }

    private fun savePrefOnBoardingData() {
        sharedPrefs = applicationContext.getSharedPreferences("prefs", Context.MODE_PRIVATE)
        val editor = sharedPrefs?.edit()
        editor?.putBoolean("isFirstTimeUsed", true)
        editor?.apply()
    }

    private fun restorePrefOnBoardingData() : Boolean? {
        sharedPrefs = applicationContext.getSharedPreferences("prefs", Context.MODE_PRIVATE)
        return sharedPrefs?.getBoolean("isFirstTimeUsed", false)
    }


}