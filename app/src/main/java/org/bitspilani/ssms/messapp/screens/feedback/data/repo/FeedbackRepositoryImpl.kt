package org.bitspilani.ssms.messapp.screens.feedback.data.repo

import io.reactivex.Completable
import org.bitspilani.ssms.messapp.screens.feedback.core.model.Feedback
import java.util.concurrent.TimeUnit

class FeedbackRepositoryImpl : FeedbackRepository {

    override fun giveFeedback(feedback: Feedback): Completable {
        return Completable.complete().delay(2, TimeUnit.SECONDS)
    }
}