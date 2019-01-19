package org.bitspilani.ssms.messapp.screens.grub.shared.data.retrofit.model

import com.squareup.moshi.Json

data class GrubMenuResponse(
    @field:Json(name = "id") val id: Long,
    @field:Json(name = "category") val category: String,
    @field:Json(name = "items") val items: List<GrubItemResponse>,
    @field:Json(name = "mess_name") val venue: String,
    @field:Json(name = "app_price") val price: String
)