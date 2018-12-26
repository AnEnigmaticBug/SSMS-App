package org.bitspilani.ssms.messapp

import android.app.Application
import org.bitspilani.ssms.messapp.di.app.AppComponent
import org.bitspilani.ssms.messapp.di.app.AppModule
import org.bitspilani.ssms.messapp.di.app.DaggerAppComponent

class MessApp : Application() {

    companion object {

        lateinit var appComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
    }
}