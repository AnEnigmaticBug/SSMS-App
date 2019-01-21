package org.bitspilani.ssms.messapp.screens.feedback.data.repo

import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers
import org.bitspilani.ssms.messapp.screens.feedback.core.model.Feedback
import org.bitspilani.ssms.messapp.screens.feedback.data.retrofit.FeedbackService
import org.bitspilani.ssms.messapp.screens.shared.data.repo.UserRepository
import org.bitspilani.ssms.messapp.util.NetworkWatcher
import org.bitspilani.ssms.messapp.util.NoConnectionException
import org.bitspilani.ssms.messapp.util.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class FeedbackRepositoryImpl(
    private val userRepository: UserRepository,
    private val networkWatcher: NetworkWatcher,
    private val feedbackService: FeedbackService
) : FeedbackRepository {

    override fun giveFeedback(feedback: Feedback): Completable {
        if(!networkWatcher.isConnectedToInternet()) {
            return Completable.error(NoConnectionException())
        }
        return userRepository.getUser()
            .toSingle()
            .flatMapCompletable { _user ->
                val body = JSONObject().apply {
                    put("body", feedback.content)
                    put("tags", JSONArray(feedback.tags))
                }
                feedbackService.sendFeedback(_user.jwt, body.toRequestBody())
            }
            .subscribeOn(Schedulers.io())
    }
}