package org.bitspilani.ssms.messapp.di.screens.notice

import dagger.Module
import dagger.Provides
import org.bitspilani.ssms.messapp.screens.notice.data.repo.NoticeRepository
import org.bitspilani.ssms.messapp.screens.notice.data.repo.NoticeRepositoryImpl

@Module
class NoticeScreenModule {

    @Provides
    fun providesNoticeRepository(): NoticeRepository = NoticeRepositoryImpl()
}