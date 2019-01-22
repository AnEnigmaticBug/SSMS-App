package org.bitspilani.ssms.messapp.screens.menu.data.room.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "MenuItems")
data class MenuItemTuple(
    @PrimaryKey @ColumnInfo(name = "item_id") val id: Long,
    @ColumnInfo(name = "item_name") val name: String
)