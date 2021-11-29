package com.Demi.Move

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.Demi.Move.Utils.PreferenceUtil

class MoveApplication : Application() {
    companion object {
        lateinit var prefs: PreferenceUtil
    }

    override fun onCreate() {
        prefs = PreferenceUtil(applicationContext)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate()
    }
}

