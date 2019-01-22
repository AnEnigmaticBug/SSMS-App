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
import org.bitspilani.ssms.messapp.screens.menu.data.room.MenusDao
import org.bitspilani.ssms.messapp.screens.menu.data.room.model.*
import org.bitspilani.ssms.messapp.screens.shared.data.repo.UserRepository
import org.bitspilani.ssms.messapp.util.NetworkWatcher
import org.bitspilani.ssms.messapp.util.NoDataSourceException
import org.bitspilani.ssms.messapp.util.getBody
import org.bitspilani.ssms.messapp.util.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import org.threeten.bp.LocalDate

class MenuRepositoryImpl(
    private val userRepository: UserRepository,
    private val menusDao: MenusDao,
    private val networkWatcher: NetworkWatcher,
    private val menuService: MenuService
) : MenuRepository {

    init {
        sendRatings()
    }

    // True if the data has been loaded once since the app started
    private var isDataLoadedInCurrentRun = false

    @SuppressLint("CheckResult")
    override fun getMenuItemsByDate(date: LocalDate): Observable<List<MenuItem>> {
        val dataSource = when(isDataLoadedInCurrentRun) {
            true  -> menusDao.getMenuItemsByDate(date)
            false -> menusDao.getServingTupleCount()
                .take(1)
                .flatMap { _count ->
                    when {
                        _count <= 0 -> {
                            when(networkWatcher.isConnectedToInternet()) {
                                true  -> fetchAndUpdateMenuItems().andThen(menusDao.getMenuItemsByDate(date))
                                false -> throw NoDataSourceException()
                            }
                        }
                        else        -> {
                            when(isDataStale()) {
                                true  -> when(networkWatcher.isConnectedToInternet()) {
                                    true  -> fetchAndUpdateMenuItems().andThen(menusDao.getMenuItemsByDate(date))
                                    false -> throw NoDataSourceException()
                                }
                                false -> when(networkWatcher.isConnectedToInternet()) {
                                    true  -> Flowable.mergeDelayError(fetchAndUpdateMenuItems().toFlowable(), menusDao.getMenuItemsByDate(date))
                                    false -> menusDao.getMenuItemsByDate(date)
                                }
                            }
                        }
                    }
                }
        }

        return dataSource
            .filter { it.isNotEmpty() }
            .map { _items ->
                _items.map { it.toCoreLayer() }
            }
            .toObservable()
            .subscribeOn(Schedulers.io())
    }

    override fun getDatesInMenu(): Observable<List<LocalDate>> {
        return menusDao.getDatesInMenu()
            .toObservable()
            .subscribeOn(Schedulers.io())
    }

    override fun rateMenuItemWithId(id: Id, rating: Rating): Completable {
        return Completable.fromAction {
            val (_, itemId, mealId) = menusDao.getServingTupleById(id)
            menusDao.insertRatingTuple(RatingTuple(itemId, mealId, rating))
        }.subscribeOn(Schedulers.io())
    }

    @SuppressLint("CheckResult")
    private fun sendRatings() {
        menusDao.getAllRatingTuples()
            .filter { _tuples ->
                _tuples.isNotEmpty()
            }
            .take(1)
            .map { _tuples ->
                _tuples.groupBy { it.rating }
            }
            .map { _groups ->
                JSONObject().apply {
                    put("upvote"  , JSONArray(_groups[Rating.Positive].toJsonObjectList()))
                    put("downvote", JSONArray(_groups[Rating.Negative].toJsonObjectList()))
                }
            }
            .map { it.toRequestBody() }
            .flatMapCompletable { _body ->
                userRepository.getUser()
                    .map { it.jwt }
                    .flatMapCompletable { _jwt ->
                        menuService.rateItems(_jwt, _body)
                    }
            }
            .subscribeOn(Schedulers.io())
            .subscribe(
                {

                },
                {

                }
            )
    }

    @SuppressLint("CheckResult")
    private fun fetchAndUpdateMenuItems(): Completable {
        Log.d("MenuRepositoryImpl", "fetch()")
        return menuService.getMeals()
            .map { _response ->
                _response.getBody()
            }
            .doOnSuccess { _meals ->
                menusDao.deleteAllMealTuples()
                menusDao.insertMealTuples(_meals.map { _meal ->
                    val date = LocalDate.parse(_meal.date)
                    val type = when(_meal.meal) {
                        "Breakfast" -> Meal.BreakFast
                        "Lunch"     -> Meal.Lunch
                        "Dinner"    -> Meal.Dinner
                        else        -> throw IllegalArgumentException("Unknown meal: ${_meal.meal}")
                    }
                    Log.d("MenuRepositoryImpl", "${_meal.id}")
                    MealTuple(_meal.id, date, type)
                })
            }
            .doOnSuccess { _meals ->
                menusDao.deleteAllServingTuples()
                menusDao.insertServingTuples(_meals.map { _meal ->
                    _meal.menuItems.map { _menuItem ->
                        ServingTuple(0L, _menuItem.id, _meal.id)
                    }
                }.flatten())
            }
            .map { _meals ->
                _meals.map { it.menuItems }.flatten()
            }
            .doOnSuccess { _items ->
                menusDao.deleteAllItemTuples()
                menusDao.insertItemTuples(_items.map { _item ->
                    MenuItemTuple(_item.id, _item.name)
                })
            }
            .doOnSuccess { isDataLoadedInCurrentRun = true }
            .ignoreElement()
            .subscribeOn(Schedulers.io())
    }


    private fun isDataStale(): Boolean {
        val maxDate = menusDao.getMaxStoredDate()
        return when {
            maxDate == null           -> true
            maxDate < LocalDate.now() -> true
            else                      -> false
        }
    }

    private fun DataLayerMenuItem.toCoreLayer(): MenuItem {
        return MenuItem(id, name, date, meal, rating)
    }

    private fun List<RatingTuple>?.toJsonObjectList(): List<JSONObject> {
        if(this == null) {
            return listOf()
        }
        return this.groupBy { it.mealId }
            .map { _group ->
                JSONObject().apply {
                    put("menu_id", _group.key)
                    put("item_ids", JSONArray(_group.value.map { it.itemId }))
                }
            }
    }
}