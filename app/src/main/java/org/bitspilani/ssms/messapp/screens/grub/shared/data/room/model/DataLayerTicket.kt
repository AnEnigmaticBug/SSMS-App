package org.bitspilani.ssms.messapp.screens.grub.shared.data.room.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.bitspilani.ssms.messapp.screens.grub.shared.core.model.Slot

@Entity(tableName = "Tickets")
data class DataLayerTicket(
    @PrimaryKey @ColumnInfo(name = "ticket_id") val id: Long,
    @ColumnInfo(name = "grub_batch_id") val grubBatchId: Long,
    @ColumnInfo(name = "slot") val slot: Slot
)