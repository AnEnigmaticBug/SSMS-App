package org.bitspilani.ssms.messapp.screens.menu.data.room

import androidx.room.*
import io.reactivex.Flowable
import org.bitspilani.ssms.messapp.screens.menu.core.model.Id
import org.bitspilani.ssms.messapp.screens.menu.core.model.Rating
import org.bitspilani.ssms.messapp.screens.menu.data.room.model.DataLayerMenuItem
import org.threeten.bp.LocalDate

@Dao
interface MenuItemsDao {

    @Query("SELECT * FROM MenuItems ORDER BY date")
    fun getAllMenuItems(): Flowable<List<DataLayerMenuItem>>

    @Query("SELECT * FROM MenuItems WHERE id = :id")
    fun getMenuItemById(id: Id): Flowable<DataLayerMenuItem>

    @Query("SELECT * FROM MenuItems WHERE date = :date ORDER BY date")
    fun getMenuItemsByDate(date: LocalDate): Flowable<List<DataLayerMenuItem>>

    @Query("SELECT * FROM MenuItems WHERE rating != 'NotRated'")
    fun getAllRatedMenuItems(): Flowable<List<DataLayerMenuItem>>

    @Query("SELECT COUNT(id) FROM MenuItems")
    fun getMenuItemCount(): Flowable<Int>

    @Query("SELECT DISTINCT date FROM MenuItems ORDER BY date")
    fun getDatesInMenu(): Flowable<List<LocalDate>>

    @Query("SELECT MAX(date) FROM MenuItems")
    fun getLatestStoredDate(): LocalDate

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMenuItems(menuItems: List<DataLayerMenuItem>)

    @Update
    fun updateMenuItem(menuItem: DataLayerMenuItem)

    @Query("UPDATE MenuItems SET rating = :rating WHERE id = :id")
    fun rateMenuItemWithId(id: Id, rating: Rating)

    @Query("DELETE FROM MenuItems")
    fun deleteAllMenuItems()
}