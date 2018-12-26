package org.bitspilani.ssms.messapp.screens.menu.core

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import org.bitspilani.ssms.messapp.screens.menu.core.model.Id
import org.bitspilani.ssms.messapp.screens.menu.core.model.Meal
import org.bitspilani.ssms.messapp.screens.menu.core.model.MenuItem
import org.bitspilani.ssms.messapp.screens.menu.core.model.Rating
import org.bitspilani.ssms.messapp.screens.menu.data.repo.MenuRepository
import org.bitspilani.ssms.messapp.screens.menu.view.model.UIState
import org.bitspilani.ssms.messapp.screens.menu.view.model.ViewLayerDate
import org.bitspilani.ssms.messapp.screens.menu.view.model.ViewLayerMeal
import org.bitspilani.ssms.messapp.screens.menu.view.model.ViewLayerMenuItem
import org.bitspilani.ssms.messapp.util.set
import org.bitspilani.ssms.messapp.util.toMut
import org.threeten.bp.LocalDate

class MenuViewModel(private val mRepo: MenuRepository) : ViewModel() {


    val state: LiveData<UIState> = MutableLiveData()
    val meals: LiveData<List<ViewLayerMeal>> = MutableLiveData()
    val dates: LiveData<List<ViewLayerDate>> = MutableLiveData()
    val toast: LiveData<String> = MutableLiveData()


    private val d1 = CompositeDisposable()


    init {
        state.toMut().value = UIState.Initializing
        d1.set(mRepo.getAllMenuItems()
            .subscribe(
                { _menuItems ->
                    dates.toMut().postValue(_menuItems.extractWithPickedDate(LocalDate.now()))
                    meals.toMut().postValue(_menuItems.filter { it.date == LocalDate.now() }.toViewLayer())
                    state.toMut().postValue(UIState.Working)
                },
                {
                    state.toMut().postValue(UIState.Failure)
                }
            ))
    }


    fun onPickDateAction(id: Long) {
        check(dates.value != null) { "Date(${LocalDate.ofEpochDay(id)}) picked before initialization" }

        dates.toMut().value = dates.value!!.map { it.copy(isSelected = it.id == id) }
        updateItemsAndSetWorkingState(LocalDate.ofEpochDay(id))
    }

    @SuppressLint("CheckResult")
    fun onRateItemAction(id: Id, rating: Rating) {
        check(dates.value != null) { "Dates not initialized" }
        check(meals.value != null) { "Meals not initialized" }

        if(getPickedDate() != LocalDate.now()) {
            toast.toMut().value = "You can only rate today's items"
            return
        }

        mRepo.rateMenuItemWithId(id, rating).subscribe()
    }


    private fun getPickedDate(): LocalDate {
        check(dates.value != null) { "Dates not initialized" }

        return LocalDate.ofEpochDay(dates.value!!.find { it.isSelected }!!.id)
    }


    private fun updateItemsAndSetWorkingState(date: LocalDate) {
        d1.set(mRepo.getAllMenuItems()
            .subscribe(
                { _menuItems ->
                    meals.toMut().postValue(_menuItems.filter { it.date == date }.toViewLayer())
                    state.toMut().postValue(UIState.Working)

                },
                {
                    state.toMut().postValue(UIState.Failure)
                }
            ))
    }


    private fun List<MenuItem>.extractWithPickedDate(date: LocalDate): List<ViewLayerDate> {

        fun LocalDate.toViewLayer(isSelected: Boolean): ViewLayerDate {
            return ViewLayerDate(toEpochDay(), dayOfMonth.toString(), month.toString(), isSelected)
        }

        return this.distinctBy { it.date }.map {
            it.date.toViewLayer(it.date == date)
        }
    }

    private fun List<MenuItem>.toViewLayer(): List<ViewLayerMeal> {
        return this.groupBy { it.meal }.map {
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
    }
}