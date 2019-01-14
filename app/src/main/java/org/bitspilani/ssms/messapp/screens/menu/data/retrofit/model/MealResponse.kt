package org.bitspilani.ssms.messapp.screens.menu.data.retrofit.model

import com.squareup.moshi.Json

data class MealResponse(
    @field:Json(name = "id") val id: Long,
    @field:Json(name = "date") val date: String,
    @field:Json(name = "category") val meal: String,
    @field:Json(name = "items") val menuItems: List<MenuItemResponse>
)