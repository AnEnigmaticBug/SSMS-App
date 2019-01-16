package org.bitspilani.ssms.messapp.screens.grub.shared.data.room.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.bitspilani.ssms.messapp.screens.grub.shared.core.model.*
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime

@Entity(tableName = "GrubBatches")
data class DataLayerGrubBatch(
    @PrimaryKey val id: Long,
    val grubId: Id,
    val name: String,
    val organizer: String,
    val foodOption: FoodOption,
    val date: LocalDate,
    val slot1Time: LocalTime,
    val slot2Time: LocalTime,
    val signUpDeadline: LocalDate,
    val cancelDeadline: LocalDate,
    val signingStatus: SigningStatus,
    val slot: Slot?,
    val foodType: FoodType,
    val menu: Set<String>,
    val venue: String,
    val price: Int,
    val ticketId: Long?
)