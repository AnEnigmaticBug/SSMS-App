package org.bitspilani.ssms.messapp.di.screens.notice

import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import org.bitspilani.ssms.messapp.screens.notice.data.repo.NoticeRepository
import org.bitspilani.ssms.messapp.screens.notice.data.repo.NoticeRepositoryImpl
import org.bitspilani.ssms.messapp.screens.notice.data.retrofit.NoticeService
import org.bitspilani.ssms.messapp.screens.notice.data.room.NoticesDao
import org.bitspilani.ssms.messapp.screens.shared.data.repo.UserRepository
import org.bitspilani.ssms.messapp.screens.shared.data.room.setup.AppDatabase
import org.bitspilani.ssms.messapp.util.NetworkWatcher
import retrofit2.Retrofit

@Module
class NoticeScreenModule {

    @Provides
    fun providesNoticeRepository(userRepository: UserRepository, sharedPreferences: SharedPreferences, noticesDao: NoticesDao, networkWatcher: NetworkWatcher, noticeService: NoticeService): NoticeRepository = NoticeRepositoryImpl(userRepository, sharedPreferences, noticesDao, networkWatcher, noticeService)

    @Provides
    fun noticeService(retrofit: Retrofit): NoticeService = retrofit.create(NoticeService::class.java)

    @Provides
    fun providesNoticesDao(appDatabase: AppDatabase): NoticesDao = appDatabase.noticesDao()
}