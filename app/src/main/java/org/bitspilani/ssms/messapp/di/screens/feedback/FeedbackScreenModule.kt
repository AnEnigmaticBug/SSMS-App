package org.bitspilani.ssms.messapp.di.screens.feedback

import dagger.Module
import dagger.Provides
import org.bitspilani.ssms.messapp.screens.feedback.data.repo.FeedbackRepository
import org.bitspilani.ssms.messapp.screens.feedback.data.repo.FeedbackRepositoryImpl
import org.bitspilani.ssms.messapp.screens.feedback.data.retrofit.FeedbackService
import org.bitspilani.ssms.messapp.screens.shared.data.repo.UserRepository
import org.bitspilani.ssms.messapp.util.NetworkWatcher
import retrofit2.Retrofit

@Module
class FeedbackScreenModule {

    @Provides
    fun providesFeedbackRepository(userRepository: UserRepository, networkWatcher: NetworkWatcher, feedbackService: FeedbackService): FeedbackRepository = FeedbackRepositoryImpl(userRepository, networkWatcher, feedbackService)

    @Provides
    fun providesFeedbackService(retrofit: Retrofit): FeedbackService = retrofit.create(FeedbackService::class.java)
}