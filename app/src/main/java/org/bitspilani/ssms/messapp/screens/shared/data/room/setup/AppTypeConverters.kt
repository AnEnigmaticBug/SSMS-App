package org.bitspilani.ssms.messapp.screens.shared.data.room.setup

import androidx.room.TypeConverter
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime

class AppTypeConverters {

    @TypeConverter
    fun LocalDateToString(dt: LocalDate): String = dt.toString()

    @TypeConverter
    fun StringToLocalDate(st: String): LocalDate = LocalDate.parse(st)

    @TypeConverter
    fun LocalDateTimeToString(dt: LocalDateTime): String = dt.toString()

    @TypeConverter
    fun StringToLocalDateTime(st: String): LocalDateTime = LocalDateTime.parse(st)
}