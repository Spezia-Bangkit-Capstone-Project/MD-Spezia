package com.spezia.spezia.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.spezia.spezia.R
import com.spezia.spezia.model.OnBoardingScreenData

class OnBoardingScreenViewPagerAdapter(
    private var context : Context,
    private var onBoardingScreenDataList: List<OnBoardingScreenData>
) : PagerAdapter() {

    override fun getCount(): Int {
        return onBoardingScreenDataList.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        val view = LayoutInflater.from(context).inflate(R.layout.onboarding_screen_layout, null)

        val imgOnBoarding : ImageView = view.findViewById(R.id.imgOnBoarding)
        val titleOnBoarding : TextView = view.findViewById(R.id.titleOnBoarding)
        val descOnBoarding : TextView = view.findViewById(R.id.descOnBoarding)

        imgOnBoarding.setImageResource(onBoardingScreenDataList[position].imgUrl)
        titleOnBoarding.text = onBoardingScreenDataList[position].title
        descOnBoarding.text = onBoardingScreenDataList[position].description

        container.addView(view)
        return view
    }
}