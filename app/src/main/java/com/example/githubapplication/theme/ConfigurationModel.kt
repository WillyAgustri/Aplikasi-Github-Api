package com.example.githubapplication.theme

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ConfigurationModel(private val pref: ConfigurationPreferences) : ViewModel() {

    private val themeSetting = MutableStateFlow<Boolean>(false)

    fun getThemeSettings(): LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData()
    }

    fun saveThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            themeSetting.value = isDarkModeActive
            pref.saveThemeSetting(isDarkModeActive)
        }
    }
    class ViewModelFactory(private val pref: ConfigurationPreferences) : ViewModelProvider.NewInstanceFactory() {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ConfigurationModel::class.java)) {
                return ConfigurationModel(pref) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
}
}
