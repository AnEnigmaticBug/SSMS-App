package org.bitspilani.ssms.messapp.di.screens.notice

import dagger.Module
import dagger.Provides
import org.bitspilani.ssms.messapp.screens.notice.data.repo.NoticeRepository
import org.bitspilani.ssms.messapp.screens.notice.data.repo.NoticeRepositoryImpl
import org.bitspilani.ssms.messapp.screens.notice.data.room.NoticesDao
import org.bitspilani.ssms.messapp.screens.shared.data.room.setup.AppDatabase

@Module
class NoticeScreenModule {

    @Provides
    fun providesNoticeRepository(): NoticeRepository = NoticeRepositoryImpl()

    @Provides
    fun providesNoticesDao(appDatabase: AppDatabase): NoticesDao = appDatabase.noticesDao()
}