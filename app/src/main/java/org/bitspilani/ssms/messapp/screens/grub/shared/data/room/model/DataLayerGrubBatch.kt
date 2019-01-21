package org.bitspilani.ssms.messapp.screens.grub.shared.data.room.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.bitspilani.ssms.messapp.screens.grub.shared.core.model.*
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime

@Entity(tableName = "GrubBatches")
data class DataLayerGrubBatch(
    @PrimaryKey @ColumnInfo(name = "grub_batch_id") val id: Long,
    @ColumnInfo(name = "grub_id") val grubId: Id,
    @ColumnInfo(name = "food_type") val foodType: FoodType,
    @ColumnInfo(name = "menu") val menu: Set<String>,
    @ColumnInfo(name = "venue") val venue: String,
    @ColumnInfo(name = "price") val price: Int
)