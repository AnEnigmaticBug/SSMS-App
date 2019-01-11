package org.bitspilani.ssms.messapp.screens.feedback.data.repo

import io.reactivex.Completable
import org.bitspilani.ssms.messapp.screens.feedback.core.model.Feedback

interface FeedbackRepository {

    fun giveFeedback(feedback: Feedback): Completable
}