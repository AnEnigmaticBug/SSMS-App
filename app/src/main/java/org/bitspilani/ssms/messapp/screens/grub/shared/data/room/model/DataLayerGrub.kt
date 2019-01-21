package org.bitspilani.ssms.messapp.screens.grub.shared.data.room.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.bitspilani.ssms.messapp.screens.grub.shared.core.model.FoodOption
import org.bitspilani.ssms.messapp.screens.grub.shared.core.model.Id
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime

@Entity(tableName = "Grubs")
data class DataLayerGrub(
    @PrimaryKey @ColumnInfo(name = "id") val id: Id,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "organizer") val organizer: String,
    @ColumnInfo(name = "food_option") val foodOption: FoodOption,
    @ColumnInfo(name = "date") val date: LocalDate,
    @ColumnInfo(name = "slot1_time") val slot1Time: LocalTime,
    @ColumnInfo(name = "slot2_time") val slot2Time: LocalTime,
    @ColumnInfo(name = "sign_up_deadline") val signUpDeadline: LocalDate,
    @ColumnInfo(name = "cancel_deadline") val cancelDeadline: LocalDate
)