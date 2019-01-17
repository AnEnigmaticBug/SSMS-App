package org.bitspilani.ssms.messapp.screens.menu.data.repo

import io.reactivex.Completable
import io.reactivex.Observable
import org.bitspilani.ssms.messapp.screens.menu.core.model.Id
import org.bitspilani.ssms.messapp.screens.menu.core.model.MenuItem
import org.bitspilani.ssms.messapp.screens.menu.core.model.Rating
import org.threeten.bp.LocalDate

interface MenuRepository {

    /**
     * Gives all the items to be served on the given date.
     * */
    fun getMenuItemsByDate(date: LocalDate): Observable<List<MenuItem>>

    /**
     * Gives all the dates in the menu in ascending order.
     * */
    fun getDatesInMenu(): Observable<List<LocalDate>>

    /**
     * Rates the item having id [id] with rating [rating].
     * */
    fun rateMenuItemWithId(id: Id, rating: Rating): Completable
}