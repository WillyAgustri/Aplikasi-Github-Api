package com.example.githubapplication.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.githubapplication.data.database.FavoriteUserRepo

class FavoriteViewModel(application: Application) : ViewModel() {
    private val detailUserData: FavoriteUserRepo = FavoriteUserRepo(application)


    fun getAllFavoriteData() = detailUserData.getAllFavorite()

    class ViewModelFactory(private val application: Application) :
        ViewModelProvider.NewInstanceFactory() {
        companion object {
            @Volatile
            private var INSTANCE: ViewModelFactory? = null

            @JvmStatic
            fun getInstance(application: Application): ViewModelFactory {
                if (INSTANCE == null) {
                    synchronized(ViewModelFactory::class.java) {
                        INSTANCE = ViewModelFactory(application)
                    }
                }
                return INSTANCE as ViewModelFactory
            }

        }

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(FavoriteViewModel::class.java)) {
                return FavoriteViewModel(application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel Class: " + modelClass.name)
        }
    }
}