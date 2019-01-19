package org.bitspilani.ssms.messapp.screens.grub.shared.data.retrofit

import io.reactivex.Completable
import io.reactivex.Single
import okhttp3.RequestBody
import org.bitspilani.ssms.messapp.screens.grub.shared.data.retrofit.model.GrubResponse
import org.bitspilani.ssms.messapp.screens.grub.shared.data.retrofit.model.TicketResponse
import retrofit2.Response
import retrofit2.http.*

interface GrubService {

    @GET("api/grubs/view")
    fun getGrubs(): Single<Response<List<GrubResponse>>>

    @GET("api/grubs/user/view")
    fun getTickets(@Header("JWT") jwt: String): Single<Response<List<TicketResponse>>>

    @POST("api/grubs/user/view/")
    fun signUpGrub(@Header("JWT") jwt: String, @Body body: RequestBody): Completable

    @HTTP(method = "DELETE", path = "api/grubs/user/view/", hasBody = true)
    fun cancelGrub(@Header("JWT") jwt: String, @Body body: RequestBody): Completable
}