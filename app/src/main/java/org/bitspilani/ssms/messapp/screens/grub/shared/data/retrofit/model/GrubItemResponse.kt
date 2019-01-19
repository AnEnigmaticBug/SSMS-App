package org.bitspilani.ssms.messapp.screens.grub.shared.data.retrofit.model

import com.squareup.moshi.Json

data class GrubItemResponse(
    @field:Json(name = "name") val name: String
)