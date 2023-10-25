package com.example.githubapplication.theme

import android.os.Bundle
import android.view.MenuItem
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.example.githubapplication.R
import com.example.githubapplication.R.id.switch_theme
import com.google.android.material.switchmaterial.SwitchMaterial

class DarkActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dark)
        initializeActionBar()
        initializeSwitchTheme()
    }

    private fun initializeActionBar() {
        supportActionBar?.title = "Theme"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
    }

    private fun initializeSwitchTheme() {
        val switchTheme = findViewById<SwitchMaterial>(switch_theme)
        val pref = ConfigurationPreferences.getInstance(application.dataStore)
        val userViewModel = ViewModelProvider(this, ConfigurationModel.ViewModelFactory(pref)).get(
            ConfigurationModel::class.java
        )

        userViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            setAppNightMode(isDarkModeActive)
            switchTheme.isChecked = isDarkModeActive
        }

        switchTheme.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            userViewModel.saveThemeSetting(isChecked)
        }
    }

    private fun setAppNightMode(isDarkModeActive: Boolean) {
        if (isDarkModeActive) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
