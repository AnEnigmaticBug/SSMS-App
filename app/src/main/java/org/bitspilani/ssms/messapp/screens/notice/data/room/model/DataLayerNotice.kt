package org.bitspilani.ssms.messapp.screens.notice.data.room.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.bitspilani.ssms.messapp.screens.notice.core.model.Id
import org.bitspilani.ssms.messapp.screens.notice.core.model.Priority
import org.threeten.bp.LocalDateTime

@Entity(tableName = "Notices")
data class DataLayerNotice(
    @PrimaryKey val id: Id,
    val heading: String,
    val content: String = "",
    val priority: Priority = Priority.Normal,
    val datetime: LocalDateTime = LocalDateTime.now()
)