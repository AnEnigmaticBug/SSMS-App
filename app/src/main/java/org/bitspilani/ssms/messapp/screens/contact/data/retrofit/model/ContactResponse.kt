package org.bitspilani.ssms.messapp.screens.contact.data.retrofit.model

import com.squareup.moshi.Json

data class ContactResponse(
    @field:Json(name = "name") val name: String,
    @field:Json(name = "post") val post: String,
    @field:Json(name = "pic_url") val picUrl: String,
    @field:Json(name = "phone") val phone: String
)