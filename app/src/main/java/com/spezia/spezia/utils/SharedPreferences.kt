package com.spezia.spezia.utils

import android.content.Context
import androidx.preference.PreferenceManager

class SharedPreferences(val context: Context) {

    companion object {
        private const val FIRST_INSTALL = "FIRST_INSTALL"
    }

    private val p = PreferenceManager.getDefaultSharedPreferences(context)

    var firstInstall = p.getBoolean(FIRST_INSTALL, false)
    set(value) = p.edit().putBoolean(FIRST_INSTALL, value).apply()

}