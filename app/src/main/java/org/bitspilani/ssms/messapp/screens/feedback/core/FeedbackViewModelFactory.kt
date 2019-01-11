package org.bitspilani.ssms.messapp.screens.feedback.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.bitspilani.ssms.messapp.MessApp
import org.bitspilani.ssms.messapp.di.screens.feedback.FeedbackScreenModule
import org.bitspilani.ssms.messapp.screens.feedback.data.repo.FeedbackRepository
import javax.inject.Inject

class FeedbackViewModelFactory : ViewModelProvider.Factory {

    @Inject
    lateinit var feedbackRepository: FeedbackRepository

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        MessApp.appComponent.newFeedbackScreenComponent(FeedbackScreenModule()).inject(this)
        return FeedbackViewModel(feedbackRepository) as T
    }
}