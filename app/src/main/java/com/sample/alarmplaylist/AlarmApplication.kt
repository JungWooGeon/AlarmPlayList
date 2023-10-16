package com.sample.alarmplaylist

import android.app.Application
import com.sample.alarmplaylist.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class AlarmApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@AlarmApplication)
            modules(appModule)
        }
    }
}