package org.bitspilani.ssms.messapp.screens.notice.data.retrofit

import io.reactivex.Single
import org.bitspilani.ssms.messapp.screens.notice.data.retrofit.model.NoticeResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface NoticeService {

    @GET("api/notice/valid/")
    fun getNotices(@Header("Authorization") jwt: String): Single<Response<List<NoticeResponse>>>
}