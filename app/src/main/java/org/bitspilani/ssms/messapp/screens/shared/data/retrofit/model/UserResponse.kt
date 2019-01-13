package org.bitspilani.ssms.messapp.screens.shared.data.retrofit.model

import com.squareup.moshi.Json

data class UserResponse(
    @field:Json(name = "name") val name: String,
    @field:Json(name = "bits_id") val id: String,
    @field:Json(name = "room") val roomNo: String,
    @field:Json(name = "qr_code") val qrCode: String,
    @field:Json(name = "JWT") val jwt: String
)