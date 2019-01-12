package org.bitspilani.ssms.messapp.screens.grub.shared.data.room.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.bitspilani.ssms.messapp.screens.grub.shared.core.model.Id
import org.threeten.bp.LocalDateTime

@Entity(tableName = "GrubBatches")
data class DataLayerGrubBatch(
    @PrimaryKey val id: Long,
    val grubInstanceId: Id,
    val name: String,
    val organizer: String,
    val menu: Set<String>,
    val datetime: LocalDateTime,
    val cancellationLimit: LocalDateTime,
    val venue: String,
    val price: Int,
    val isVeg: Boolean,
    val isAvailable: Boolean,
    val isSigned: Boolean
)