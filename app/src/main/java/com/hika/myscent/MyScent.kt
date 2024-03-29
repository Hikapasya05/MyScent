package com.hika.myscent

import android.app.Application
import com.hika.myscent.di.databaseModule
import com.hika.myscent.di.firebaseModule
import com.hika.myscent.di.repositoryModule
import com.hika.myscent.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MyScent: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.NONE)
            androidContext(this@MyScent)
            modules(
                listOf(
                    firebaseModule,
                    databaseModule,
                    repositoryModule,
                    viewModelModule
                )
            )
        }
    }
}