package com.app.newsbreeze.app

import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App:MultiDexApplication() {
    init {
        instance = this
    }

    companion object {
        lateinit var instance: App
            private set
    }
    override fun onCreate() {
        super.onCreate()
        instance = this
        MultiDex.install(this)
    }
}