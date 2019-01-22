package org.bitspilani.ssms.messapp.di.app

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import org.bitspilani.ssms.messapp.screens.menu.data.repo.MenuRepository
import org.bitspilani.ssms.messapp.screens.menu.data.repo.MenuRepositoryImpl
import org.bitspilani.ssms.messapp.screens.menu.data.retrofit.MenuService
import org.bitspilani.ssms.messapp.screens.menu.data.room.MenusDao
import org.bitspilani.ssms.messapp.screens.shared.data.repo.UserRepository
import org.bitspilani.ssms.messapp.screens.shared.data.repo.UserRepositoryImpl
import org.bitspilani.ssms.messapp.screens.shared.data.retrofit.UserService
import org.bitspilani.ssms.messapp.screens.shared.data.retrofit.setup.BaseInterceptor
import org.bitspilani.ssms.messapp.screens.shared.data.room.setup.AppDatabase
import org.bitspilani.ssms.messapp.util.NetworkWatcher
import org.bitspilani.ssms.messapp.util.NetworkWatcherImpl
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
class AppModule(private val application: Application) {

    @Provides @Singleton
    fun providesMenuRepository(userRepository: UserRepository, menusDao: MenusDao, networkWatcher: NetworkWatcher, menuService: MenuService): MenuRepository = MenuRepositoryImpl(userRepository, menusDao, networkWatcher, menuService)

    @Provides @Singleton
    fun providesMenusDao(appDatabase: AppDatabase): MenusDao = appDatabase.menusDao()

    @Provides @Singleton
    fun providesMenuService(retrofit: Retrofit): MenuService = retrofit.create(MenuService::class.java)

    @Provides @Singleton
    fun providesUserRepository(sharedPreferences: SharedPreferences, networkWatcher: NetworkWatcher, userService: UserService): UserRepository {
        return UserRepositoryImpl(sharedPreferences, networkWatcher, userService)
    }

    @Provides
    fun providesLoginService(retrofit: Retrofit): UserService {
        return retrofit.create(UserService::class.java)
    }

    @Provides @Singleton
    fun providesRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://142.93.213.45/")
            .client(OkHttpClient().newBuilder().addInterceptor(BaseInterceptor()).build())
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    @Provides @Singleton
    fun providesNetworkWatcher(application: Application): NetworkWatcher {
        return NetworkWatcherImpl(application)
    }

    @Provides @Singleton
    fun providesSharedPreferences(application: Application): SharedPreferences {
        return application.getSharedPreferences("mess.sp", Context.MODE_PRIVATE)
    }

    @Provides @Singleton
    fun providesAppDatabase(application: Application): AppDatabase {
        return Room.databaseBuilder(application, AppDatabase::class.java, "mess.db")
            .build()
    }

    @Provides @Singleton
    fun providesApplication(): Application = application
}