package org.bitspilani.ssms.messapp.screens.shared.data.retrofit.model

import com.squareup.moshi.Json

data class QrResponse(
    @field:Json(name = "QR") val qrCode: String
)