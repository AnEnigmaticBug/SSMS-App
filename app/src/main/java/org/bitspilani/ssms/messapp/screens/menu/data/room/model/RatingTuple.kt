package org.bitspilani.ssms.messapp.screens.menu.data.room.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import org.bitspilani.ssms.messapp.screens.menu.core.model.Rating

@Entity(tableName = "Ratings", primaryKeys = ["item_id", "meal_id"])
data class RatingTuple(
    @ColumnInfo(name = "item_id") val itemId: Long,
    @ColumnInfo(name = "meal_id") val mealId: Long,
    @ColumnInfo(name = "rating" ) val rating: Rating
)