package org.bitspilani.ssms.messapp.screens.menu.data.repo

import io.reactivex.Completable
import io.reactivex.Observable
import org.bitspilani.ssms.messapp.screens.menu.core.model.Id
import org.bitspilani.ssms.messapp.screens.menu.core.model.MenuItem
import org.bitspilani.ssms.messapp.screens.menu.core.model.Rating

interface MenuRepository {

    fun getAllMenuItems(): Observable<List<MenuItem>>

    fun rateMenuItemWithId(id: Id, rating: Rating): Completable
}