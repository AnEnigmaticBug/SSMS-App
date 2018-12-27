package org.bitspilani.ssms.messapp.screens.menu.data.repo

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import org.bitspilani.ssms.messapp.screens.menu.core.model.Id
import org.bitspilani.ssms.messapp.screens.menu.core.model.Meal
import org.bitspilani.ssms.messapp.screens.menu.core.model.MenuItem
import org.bitspilani.ssms.messapp.screens.menu.core.model.Rating
import org.bitspilani.ssms.messapp.screens.menu.data.room.MenuItemsDao
import org.bitspilani.ssms.messapp.screens.menu.data.room.model.DataLayerMenuItem
import org.threeten.bp.LocalDate

class MenuRepositoryImpl(private val menuItemsDao: MenuItemsDao) : MenuRepository {

    init {
        val baseDate = LocalDate.now()
        Completable.fromAction {
            menuItemsDao.deleteAllMenuItems()
            menuItemsDao.insertMenuItems(listOf(
                DataLayerMenuItem(1, "Choice of Eggs", baseDate.plusDays(0), Meal.BreakFast, Rating.NotRated),
                DataLayerMenuItem(2, "Cornflakes", baseDate.plusDays(0), Meal.BreakFast, Rating.Positive),
                DataLayerMenuItem(3, "Bread + Jam", baseDate.plusDays(0), Meal.BreakFast, Rating.NotRated),
                DataLayerMenuItem(4, "Veg Poha", baseDate.plusDays(0), Meal.BreakFast, Rating.NotRated),
                DataLayerMenuItem(5, "Orange", baseDate.plusDays(0), Meal.BreakFast, Rating.Negative),
                DataLayerMenuItem(6, "Milk", baseDate.plusDays(0), Meal.BreakFast, Rating.NotRated),
                DataLayerMenuItem(7, "Finger Salad", baseDate.plusDays(0), Meal.Lunch, Rating.NotRated),
                DataLayerMenuItem(8, "Veg 65", baseDate.plusDays(0), Meal.Lunch, Rating.Positive),
                DataLayerMenuItem(9, "Chapati", baseDate.plusDays(0), Meal.Lunch, Rating.NotRated),
                DataLayerMenuItem(10, "Lemon + Onion", baseDate.plusDays(0), Meal.Lunch, Rating.NotRated),
                DataLayerMenuItem(11, "Curd", baseDate.plusDays(0), Meal.Lunch, Rating.Negative),
                DataLayerMenuItem(12, "Chule Baturey", baseDate.plusDays(0), Meal.Dinner, Rating.NotRated),
                DataLayerMenuItem(13, "Ginger Rasam", baseDate.plusDays(0), Meal.Dinner, Rating.NotRated),
                DataLayerMenuItem(14, "Chopped Onions", baseDate.plusDays(0), Meal.Dinner, Rating.Positive),
                DataLayerMenuItem(15, "Baadam Milk", baseDate.plusDays(0), Meal.Dinner, Rating.NotRated),
                DataLayerMenuItem(16, "Rice", baseDate.plusDays(0), Meal.Dinner, Rating.NotRated),
                DataLayerMenuItem(17, "Gulab Jamun", baseDate.plusDays(0), Meal.Dinner, Rating.Negative),
                DataLayerMenuItem(18, "Item18", baseDate.plusDays(1), Meal.Dinner, Rating.NotRated),
                DataLayerMenuItem(19, "Item18", baseDate.plusDays(2), Meal.Dinner, Rating.NotRated),
                DataLayerMenuItem(20, "Item18", baseDate.plusDays(3), Meal.Dinner, Rating.NotRated),
                DataLayerMenuItem(21, "Item18", baseDate.plusDays(4), Meal.Dinner, Rating.NotRated),
                DataLayerMenuItem(22, "Item18", baseDate.plusDays(5), Meal.Dinner, Rating.NotRated),
                DataLayerMenuItem(23, "Item18", baseDate.plusDays(6), Meal.Dinner, Rating.NotRated),
                DataLayerMenuItem(24, "Item18", baseDate.plusDays(7), Meal.Dinner, Rating.NotRated),
                DataLayerMenuItem(25, "Item18", baseDate.plusDays(8), Meal.Dinner, Rating.NotRated)
            ))
        }.subscribeOn(Schedulers.io()).subscribe()
    }

    override fun getAllMenuItems(): Observable<List<MenuItem>> {
        return menuItemsDao.getAllMenuItems()
            .subscribeOn(Schedulers.io())
            .filter { it.isNotEmpty() }
            .map { it.map { MenuItem(it.id, it.name, it.date, it.meal, it.rating) } }
            .toObservable()
    }

    override fun rateMenuItemWithId(id: Id, rating: Rating): Completable {
        return Completable.fromAction {
            menuItemsDao.getMenuItemById(id)
                .take(1)
                .subscribe {
                    menuItemsDao.updateMenuItem(it.copy(rating = rating))
                }
        }.subscribeOn(Schedulers.io())
    }
}