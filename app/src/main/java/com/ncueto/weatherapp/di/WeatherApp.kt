package com.ncueto.weatherapp.di

import android.app.Application
import com.ncueto.weatherapp.BuildConfig
import com.ncueto.weatherapp.di.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import timber.log.Timber

class WeatherApp: Application() {
    override fun onCreate() {
        super.onCreate()

        // Initialize Timber for logging
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        // Initialize Koin
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@WeatherApp)
            modules(appModules)
        }
    }
}