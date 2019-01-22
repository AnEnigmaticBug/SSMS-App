package org.bitspilani.ssms.messapp.screens.menu.data.room.model

import androidx.room.ColumnInfo
import org.bitspilani.ssms.messapp.screens.menu.core.model.Id
import org.bitspilani.ssms.messapp.screens.menu.core.model.Meal
import org.bitspilani.ssms.messapp.screens.menu.core.model.Rating
import org.threeten.bp.LocalDate

data class DataLayerMenuItem(
    @ColumnInfo(name = "serving_id") val id: Id,
    @ColumnInfo(name = "item_name") val name: String,
    @ColumnInfo(name = "date") val date: LocalDate,
    @ColumnInfo(name = "type") val meal: Meal,
    @ColumnInfo(name = "rating") val rating: Rating
)