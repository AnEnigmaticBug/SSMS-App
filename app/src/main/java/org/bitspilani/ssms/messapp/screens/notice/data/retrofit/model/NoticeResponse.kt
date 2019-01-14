package org.bitspilani.ssms.messapp.screens.notice.data.retrofit.model

import com.squareup.moshi.Json

data class NoticeResponse(
    @field:Json(name = "id") val id: Long,
    @field:Json(name = "heading") val heading: String,
    @field:Json(name = "body") val content: String,
    @field:Json(name = "start_date") val date: String,
    @field:Json(name = "notice_type") val priority: String
)