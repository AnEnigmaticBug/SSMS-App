package org.bitspilani.ssms.messapp.screens.menu.core

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import org.bitspilani.ssms.messapp.screens.menu.core.model.Id
import org.bitspilani.ssms.messapp.screens.menu.core.model.Meal
import org.bitspilani.ssms.messapp.screens.menu.core.model.MenuItem
import org.bitspilani.ssms.messapp.screens.menu.core.model.Rating
import org.bitspilani.ssms.messapp.screens.menu.data.repo.MenuRepository
import org.bitspilani.ssms.messapp.screens.menu.view.model.UiOrder
import org.bitspilani.ssms.messapp.screens.menu.view.model.ViewLayerDate
import org.bitspilani.ssms.messapp.screens.menu.view.model.ViewLayerMeal
import org.bitspilani.ssms.messapp.screens.menu.view.model.ViewLayerMenuItem
import org.bitspilani.ssms.messapp.util.NoDataSourceException
import org.bitspilani.ssms.messapp.util.NoLoggedUserException
import org.bitspilani.ssms.messapp.util.set
import org.bitspilani.ssms.messapp.util.toMut
import org.threeten.bp.LocalDate

class MenuViewModel(private val mRepo: MenuRepository) : ViewModel() {

    val order: LiveData<UiOrder> = MutableLiveData()
    val toast: LiveData<String?> = MutableLiveData()


    // Holds the last date the user picked. Useful when retrying after an error.
    private lateinit var pickedDate: LocalDate


    private val d1 = CompositeDisposable()
    private val d2 = CompositeDisposable()


    init {
        initializeWithDate(LocalDate.now())
    }


    fun onRetryAction() {
        initializeWithDate(pickedDate)
    }

    fun onPickDateAction(id: Long) {
        check(order.value is UiOrder.ShowWorking) { "Date picked without WorkingState" }

        val date = LocalDate.ofEpochDay(id)
        pickedDate = date

        d1.set(mRepo.getAllMenuItems()
            .subscribe(
                { _items ->
                    val dates = _items.toViewLayerDates(pickedDate = date)
                    val meals = _items.toViewLayerMeals(pickedDate = date)
                    order.toMut().postValue(UiOrder.ShowWorking(dates, meals))
                },
                {
                    order.toMut().postValue(when(it) {
                        is NoLoggedUserException -> UiOrder.MoveToLogin
                        else                     -> UiOrder.ShowFailure("Something went wrong :(")
                    })
                }
            ))
    }

    fun onRateItemAction(id: Id, rating: Rating) {
        check(order.value is UiOrder.ShowWorking) { "Item rated without WorkingState" }

        if(pickedDate != LocalDate.now()) {
            toast.toMut().postValue("You can only rate today's items")
            return
        }

        d2.set(mRepo.rateMenuItemWithId(id, rating)
            .subscribe(
                {

                },
                {
                    toast.toMut().postValue("An error occurred")
                }
            ))
    }


    private fun initializeWithDate(date: LocalDate) {
        pickedDate = date

        order.toMut().value = UiOrder.ShowLoading

        d1.set(mRepo.getAllMenuItems()
            .subscribe(
                { _items ->
                    val dates = _items.toViewLayerDates(pickedDate = date)
                    val meals = _items.toViewLayerMeals(pickedDate = date)
                    order.toMut().postValue(UiOrder.ShowWorking(dates, meals))
                },
                {
                    order.toMut().postValue(when(it) {
                        is NoDataSourceException -> UiOrder.ShowFailure("Couldn't connect to server")
                        is NoLoggedUserException -> UiOrder.MoveToLogin
                        else                     -> UiOrder.ShowFailure("Something went wrong :(")
                    })

                }
            ))
    }


    private fun List<MenuItem>.toViewLayerDates(pickedDate: LocalDate): List<ViewLayerDate> {

        fun LocalDate.toViewLayer(isSelected: Boolean): ViewLayerDate {
            return ViewLayerDate(toEpochDay(), dayOfMonth.toString(), month.toString(), isSelected)
        }

        return this.distinctBy { it.date }.map {
            it.date.toViewLayer(it.date == pickedDate)
        }
    }

    private fun List<MenuItem>.toViewLayerMeals(pickedDate: LocalDate): List<ViewLayerMeal> {
        return this.filter { it.date == pickedDate }.groupBy { it.meal }.map {
            val name = when(it.key) {
                Meal.BreakFast -> "Breakfast"
                Meal.Lunch     -> "Lunch"
                Meal.Dinner    -> "Dinner"
            }

            return@map ViewLayerMeal(name, it.value.map { ViewLayerMenuItem(it.id, it.name, it.rating) })
        }
    }


    override fun onCleared() {
        super.onCleared()
        d1.clear()
        d2.clear()
    }
}