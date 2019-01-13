package org.bitspilani.ssms.messapp.screens.shared.data.retrofit

import io.reactivex.Single
import okhttp3.RequestBody
import org.bitspilani.ssms.messapp.screens.shared.data.retrofit.model.UserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UserService {

    @POST("api/login")
    fun login(@Body body: RequestBody): Single<Response<UserResponse>>

}