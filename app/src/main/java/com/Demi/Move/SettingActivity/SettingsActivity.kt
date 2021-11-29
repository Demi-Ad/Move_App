package com.Demi.Move.SettingActivity

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceFragmentCompat
import com.Demi.Move.R
import com.Demi.Move.databinding.SettingsActivityBinding

class SettingsActivity : AppCompatActivity() {
    private val binding by lazy {
        SettingsActivityBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.settingToolbar)
        supportActionBar?.title = "Setting"
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                supportActionBar?.setDisplayHomeAsUpEnabled(true)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
