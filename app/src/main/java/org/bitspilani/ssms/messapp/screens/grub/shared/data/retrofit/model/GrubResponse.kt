package org.bitspilani.ssms.messapp.screens.grub.shared.data.retrofit.model

import com.squareup.moshi.Json
import org.bitspilani.ssms.messapp.screens.grub.shared.core.model.Id

data class GrubResponse(
    @field:Json(name = "id") val id: Id,
    @field:Json(name = "name") val name: String,
    @field:Json(name = "date") val date: String,
    @field:Json(name = "purchase_deadline") val signUpDeadline: String,
    @field:Json(name = "cancellation_deadline") val cancelDeadline: String,
    @field:Json(name = "status") val status: String,
    @field:Json(name = "menues") val menus: List<GrubMenuResponse>,
    @field:Json(name = "slot_a") val slot1Time: String,
    @field:Json(name = "slot_b") val slot2Time: String,
    @field:Json(name = "assoc") val organizer: String
)