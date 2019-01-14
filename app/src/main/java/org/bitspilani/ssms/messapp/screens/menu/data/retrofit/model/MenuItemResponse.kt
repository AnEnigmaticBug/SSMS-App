package org.bitspilani.ssms.messapp.screens.menu.data.retrofit.model

import com.squareup.moshi.Json
import org.bitspilani.ssms.messapp.screens.menu.core.model.Id

data class MenuItemResponse(
    @field:Json(name = "id") val id: Id,
    @field:Json(name = "name") val name: String
)