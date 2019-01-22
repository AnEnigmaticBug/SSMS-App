package org.bitspilani.ssms.messapp.screens.menu.data.retrofit

import io.reactivex.Completable
import io.reactivex.Single
import okhttp3.RequestBody
import org.bitspilani.ssms.messapp.screens.menu.data.retrofit.model.MealResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface MenuService {

    @GET("api/mess/menu")
    fun getMeals(): Single<Response<List<MealResponse>>>

    @POST("api/mess/menu/")
    fun rateItems(@Header("Authorization") jwt: String, @Body body: RequestBody): Completable
}