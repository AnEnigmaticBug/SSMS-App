package org.bitspilani.ssms.messapp.screens.menu.data.repo

import io.reactivex.Completable
import io.reactivex.Observable
import org.bitspilani.ssms.messapp.screens.menu.core.model.Id
import org.bitspilani.ssms.messapp.screens.menu.core.model.Meal
import org.bitspilani.ssms.messapp.screens.menu.core.model.MenuItem
import org.bitspilani.ssms.messapp.screens.menu.core.model.Rating
import org.threeten.bp.LocalDate

class MenuRepositoryImpl : MenuRepository {

    override fun getAllMenuItems(): Observable<List<MenuItem>> {
        val baseDate = LocalDate.now()
        return Observable.just(
            listOf(
                MenuItem(1, "Choice of Eggs", baseDate.plusDays(0), Meal.BreakFast),
                MenuItem(2, "Cornflakes", baseDate.plusDays(0), Meal.BreakFast, Rating.Positive),
                MenuItem(3, "Bread + Jam", baseDate.plusDays(0), Meal.BreakFast),
                MenuItem(4, "Veg Poha", baseDate.plusDays(0), Meal.BreakFast),
                MenuItem(5, "Orange", baseDate.plusDays(0), Meal.BreakFast, Rating.Negative),
                MenuItem(6, "Milk", baseDate.plusDays(0), Meal.BreakFast),
                MenuItem(7, "Finger Salad", baseDate.plusDays(0), Meal.Lunch),
                MenuItem(8, "Veg 65", baseDate.plusDays(0), Meal.Lunch, Rating.Positive),
                MenuItem(9, "Chapati", baseDate.plusDays(0), Meal.Lunch),
                MenuItem(10, "Lemon + Onion", baseDate.plusDays(0), Meal.Lunch),
                MenuItem(11, "Curd", baseDate.plusDays(0), Meal.Lunch, Rating.Negative),
                MenuItem(12, "Chule Baturey", baseDate.plusDays(0), Meal.Dinner),
                MenuItem(13, "Ginger Rasam", baseDate.plusDays(0), Meal.Dinner),
                MenuItem(14, "Chopped Onions", baseDate.plusDays(0), Meal.Dinner, Rating.Positive),
                MenuItem(15, "Baadam Milk", baseDate.plusDays(0), Meal.Dinner),
                MenuItem(16, "Rice", baseDate.plusDays(0), Meal.Dinner),
                MenuItem(17, "Gulab Jamun", baseDate.plusDays(0), Meal.Dinner, Rating.Negative),
                MenuItem(18, "Item18", baseDate.plusDays(1), Meal.Dinner),
                MenuItem(18, "Item18", baseDate.plusDays(2), Meal.Dinner),
                MenuItem(18, "Item18", baseDate.plusDays(3), Meal.Dinner),
                MenuItem(18, "Item18", baseDate.plusDays(4), Meal.Dinner),
                MenuItem(18, "Item18", baseDate.plusDays(5), Meal.Dinner),
                MenuItem(18, "Item18", baseDate.plusDays(6), Meal.Dinner),
                MenuItem(18, "Item18", baseDate.plusDays(7), Meal.Dinner),
                MenuItem(18, "Item18", baseDate.plusDays(8), Meal.Dinner)
            )
        )
    }

    override fun rateMenuItemWithId(id: Id, rating: Rating): Completable {
        return Completable.complete()
    }
}