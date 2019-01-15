package org.bitspilani.ssms.messapp

import android.app.Application
import androidx.work.*
import com.jakewharton.threetenabp.AndroidThreeTen
import org.bitspilani.ssms.messapp.di.app.AppComponent
import org.bitspilani.ssms.messapp.di.app.AppModule
import org.bitspilani.ssms.messapp.di.app.DaggerAppComponent
import org.bitspilani.ssms.messapp.screens.menu.work.SendRatingWorker
import java.util.concurrent.TimeUnit

class MessApp : Application() {

    companion object {

        lateinit var appComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()

        AndroidThreeTen.init(this)
    }

    private fun scheduleWork() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val work = PeriodicWorkRequestBuilder<SendRatingWorker>(12, TimeUnit.HOURS)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance().enqueueUniquePeriodicWork("RATE_ITEMS", ExistingPeriodicWorkPolicy.KEEP, work)
    }
}