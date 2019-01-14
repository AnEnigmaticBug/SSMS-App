package org.bitspilani.ssms.messapp.screens.menu.data.retrofit

import io.reactivex.Single
import org.bitspilani.ssms.messapp.screens.menu.data.retrofit.model.MealResponse
import retrofit2.Response
import retrofit2.http.GET

interface MenuService {

    @GET("api/mess/menu")
    fun getMeals(): Single<Response<List<MealResponse>>>
}