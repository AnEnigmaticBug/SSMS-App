package org.bitspilani.ssms.messapp.screens.feedback.data.retrofit

import io.reactivex.Completable
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface FeedbackService {

    @POST("api/feedback/")
    fun sendFeedback(@Header("Authorization") jwt: String, @Body body: RequestBody): Completable
}