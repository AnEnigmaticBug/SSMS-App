package org.bitspilani.ssms.messapp

import android.app.Application
import android.util.Log
import com.jakewharton.threetenabp.AndroidThreeTen
import io.reactivex.plugins.RxJavaPlugins
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

        RxJavaPlugins.setErrorHandler {
            Log.e("MessApp", "MessApp: Undeliverable exception of type $it: ${it.message?: "No message"}")
        }

        AndroidThreeTen.init(this)
    }
}