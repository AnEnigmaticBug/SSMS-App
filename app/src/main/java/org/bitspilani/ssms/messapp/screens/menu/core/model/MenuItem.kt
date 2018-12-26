package org.bitspilani.ssms.messapp.screens.menu.core.model

import org.threeten.bp.LocalDate

typealias Id = Long

data class MenuItem(
    val id: Id,
    val name: String,
    val date: LocalDate,
    val meal: Meal,
    val rating: Rating = Rating.NotRated
)