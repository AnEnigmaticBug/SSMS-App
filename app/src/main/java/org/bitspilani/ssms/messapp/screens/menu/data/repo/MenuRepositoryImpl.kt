package org.bitspilani.ssms.messapp.screens.menu.data.repo

import android.annotation.SuppressLint
import android.util.Log
import io.reactivex.*
import io.reactivex.schedulers.Schedulers
import org.bitspilani.ssms.messapp.screens.menu.core.model.Id
import org.bitspilani.ssms.messapp.screens.menu.core.model.Meal
import org.bitspilani.ssms.messapp.screens.menu.core.model.MenuItem
import org.bitspilani.ssms.messapp.screens.menu.core.model.Rating
import org.bitspilani.ssms.messapp.screens.menu.data.retrofit.MenuService
import org.bitspilani.ssms.messapp.screens.menu.data.room.MenuItemsDao
import org.bitspilani.ssms.messapp.screens.menu.data.room.model.DataLayerMenuItem
import org.bitspilani.ssms.messapp.util.NetworkWatcher
import org.bitspilani.ssms.messapp.util.NoDataSourceException
import org.threeten.bp.LocalDate

class MenuRepositoryImpl(
    private val menuItemsDao: MenuItemsDao,
    private val networkWatcher: NetworkWatcher,
    private val menuService: MenuService
) : MenuRepository {

    @SuppressLint("CheckResult")
    override fun getMenuItemsByDate(date: LocalDate): Observable<List<MenuItem>> {
        return menuItemsDao.getMenuItemCount()
            .take(1)
            .flatMap { _count ->
                when {
                    _count <= 0 -> {
                        when(networkWatcher.isConnectedToInternet()) {
                            true  -> when(networkWatcher.isConnectedToInternet()) {
                                true  -> fetchAndUpdateMenuItems().andThen(menuItemsDao.getMenuItemsByDate(date))
                                false -> throw NoDataSourceException("Not connected to the internet")
                            }
                            false -> throw NoDataSourceException("Not connected to the internet")
                        }
                    }
                    else        -> {
                        when(isDataStale()) {
                            true  -> when(networkWatcher.isConnectedToInternet()) {
                                true  -> fetchAndUpdateMenuItems().andThen(menuItemsDao.getMenuItemsByDate(date))
                                false -> throw NoDataSourceException("Not connected to the internet")
                            }
                            false -> {
                                menuItemsDao.getMenuItemsByDate(date)
                            }
                        }
                    }
                }
            }
            .filter { it.isNotEmpty() }
            .map { _items ->
                _items.map { it.toCoreLayer() }
            }
            .toObservable()
            .subscribeOn(Schedulers.io())
    }

    override fun getDatesInMenu(): Observable<List<LocalDate>> {
        return menuItemsDao.getDatesInMenu()
            .toObservable()
            .subscribeOn(Schedulers.io())
    }

    override fun rateMenuItemWithId(id: Id, rating: Rating): Completable {
        return Completable.fromAction {
            menuItemsDao.rateMenuItemWithId(id, rating)
        }.subscribeOn(Schedulers.io())
    }

    @SuppressLint("CheckResult")
    private fun fetchAndUpdateMenuItems(): Completable {
        Log.d("MenuRepositoryImpl", "fetch()")
        return menuService.getMeals()
            .map { _response ->
                Log.d("MenuRepositoryImpl", "${_response.code()}: ${_response.errorBody()?.string()}")
                when(_response.code()) {
                    200  -> _response.body()!!
                    else -> throw Exception("${_response.code()}: ${_response.errorBody()?.string()}")
                }
            }
            .map { _meals ->
                _meals.groupBy { it.date }.map { it.value }
            }
            .map { _menus ->
                _menus.map { _meals ->
                    _meals.map { _meal ->
                        _meal.menuItems.map { _item ->
                            val mealType = when(_meal.meal) {
                                "Breakfast" -> Meal.BreakFast
                                "Lunch"     -> Meal.Lunch
                                "Dinner"    -> Meal.Dinner
                                else        -> throw IllegalArgumentException("Unknown meal: ${_meal.meal}")
                            }

                            DataLayerMenuItem(
                                0,
                                _item.id,
                                _meal.id,
                                _item.name,
                                LocalDate.parse(_meals.first().date),
                                mealType,
                                Rating.NotRated
                            )
                        }
                    }
                }
            }
            .map {
                it.flatten().flatten()
            }
            .doOnSuccess { _items ->
                menuItemsDao.deleteAllMenuItems()
                menuItemsDao.insertMenuItems(_items)
            }
            .ignoreElement()
            .subscribeOn(Schedulers.io())
    }

    private fun isDataStale(): Boolean {
        return menuItemsDao.getLatestStoredDate() < LocalDate.now()
    }

    private fun DataLayerMenuItem.toCoreLayer(): MenuItem {
        return MenuItem(id, name, date, meal, rating)
    }
}