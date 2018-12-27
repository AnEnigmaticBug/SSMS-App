package org.bitspilani.ssms.messapp.screens.notice.data.room

import androidx.room.TypeConverter
import org.bitspilani.ssms.messapp.screens.notice.core.model.Priority

class NoticesTypeConverters {

    @TypeConverter
    fun PriorityToString(pr: Priority): String = pr.toString()

    @TypeConverter
    fun StringToPriority(st: String): Priority = Priority.valueOf(st)
}