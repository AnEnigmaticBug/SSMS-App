package org.bitspilani.ssms.messapp.screens.menu.data.room.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.bitspilani.ssms.messapp.screens.menu.core.model.Id
import org.bitspilani.ssms.messapp.screens.menu.core.model.Meal
import org.bitspilani.ssms.messapp.screens.menu.core.model.Rating
import org.threeten.bp.LocalDate

@Entity(tableName = "MenuItems")
data class DataLayerMenuItem(
    @PrimaryKey(autoGenerate = true) val id: Id,
    val itemId: Long,
    val mealId: Long,
    val name: String,
    val date: LocalDate,
    val meal: Meal,
    val rating: Rating
)