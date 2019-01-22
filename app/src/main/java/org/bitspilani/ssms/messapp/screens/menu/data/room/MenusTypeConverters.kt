package org.bitspilani.ssms.messapp.screens.menu.data.room

import androidx.room.TypeConverter
import org.bitspilani.ssms.messapp.screens.menu.core.model.Meal
import org.bitspilani.ssms.messapp.screens.menu.core.model.Rating

class MenusTypeConverters {

    @TypeConverter
    fun MealToString(ml: Meal): String = ml.toString()

    @TypeConverter
    fun StringToMeal(st: String): Meal = Meal.valueOf(st)

    @TypeConverter
    fun RatingToString(rt: Rating): String = rt.toString()

    @TypeConverter
    fun StringToRating(st: String): Rating = Rating.valueOf(st)
}