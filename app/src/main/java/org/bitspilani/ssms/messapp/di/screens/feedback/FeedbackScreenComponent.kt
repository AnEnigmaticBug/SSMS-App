package org.bitspilani.ssms.messapp.di.screens.feedback

import dagger.Subcomponent
import org.bitspilani.ssms.messapp.screens.feedback.core.FeedbackViewModelFactory

@Subcomponent(modules = [FeedbackScreenModule::class])
interface FeedbackScreenComponent {

    fun inject(factory: FeedbackViewModelFactory)
}