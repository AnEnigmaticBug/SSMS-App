package org.bitspilani.ssms.messapp.di.screens.feedback

import dagger.Module
import dagger.Provides
import org.bitspilani.ssms.messapp.screens.feedback.data.repo.FeedbackRepository
import org.bitspilani.ssms.messapp.screens.feedback.data.repo.FeedbackRepositoryImpl

@Module
class FeedbackScreenModule {

    @Provides
    fun providesFeedbackRepository(): FeedbackRepository = FeedbackRepositoryImpl()
}