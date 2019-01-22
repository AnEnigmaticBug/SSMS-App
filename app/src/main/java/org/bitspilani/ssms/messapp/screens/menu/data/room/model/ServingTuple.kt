package org.bitspilani.ssms.messapp.screens.menu.data.room.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.bitspilani.ssms.messapp.screens.grub.shared.core.model.Id

@Entity(tableName = "Servings")
data class ServingTuple(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "serving_id") val servingId: Id,
    @ColumnInfo(name = "item_id") val itemId: Long,
    @ColumnInfo(name = "meal_id") val mealId: Long
)