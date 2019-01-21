package org.bitspilani.ssms.messapp.screens.grub.shared.data.retrofit.model

import com.squareup.moshi.Json

data class TicketResponse(
    @field:Json(name = "id") val id: Long,
    @field:Json(name = "grub") val grubBatchId: Long,
    @field:Json(name = "slot") val slot: String
)