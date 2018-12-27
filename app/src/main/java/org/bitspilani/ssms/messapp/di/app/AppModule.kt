package org.bitspilani.ssms.messapp.di.app

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import org.bitspilani.ssms.messapp.screens.shared.data.room.setup.AppDatabase
import javax.inject.Singleton

@Module
class AppModule(private val application: Application) {

    @Provides @Singleton
    fun providesAppDatabase(application: Application): AppDatabase {
        return Room.databaseBuilder(application, AppDatabase::class.java, "mess.db")
            .build()
    }

    @Provides @Singleton
    fun providesApplication(): Application = application
}