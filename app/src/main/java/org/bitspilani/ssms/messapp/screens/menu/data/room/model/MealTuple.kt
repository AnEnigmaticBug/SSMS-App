package org.bitspilani.ssms.messapp.screens.menu.data.room.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.bitspilani.ssms.messapp.screens.menu.core.model.Meal
import org.threeten.bp.LocalDate

@Entity(tableName = "Meals")
data class MealTuple(
    @PrimaryKey @ColumnInfo(name = "meal_id") val id: Long,
    @ColumnInfo(name = "date") val date: LocalDate,
    @ColumnInfo(name = "type") val type: Meal
)