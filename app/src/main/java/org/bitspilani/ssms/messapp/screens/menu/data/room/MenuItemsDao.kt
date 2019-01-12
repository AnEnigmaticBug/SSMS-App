package org.bitspilani.ssms.messapp.screens.menu.data.room

import androidx.room.*
import io.reactivex.Flowable
import org.bitspilani.ssms.messapp.screens.menu.core.model.Id
import org.bitspilani.ssms.messapp.screens.menu.core.model.Rating
import org.bitspilani.ssms.messapp.screens.menu.data.room.model.DataLayerMenuItem

@Dao
interface MenuItemsDao {

    @Query("SELECT * FROM MenuItems")
    fun getAllMenuItems(): Flowable<List<DataLayerMenuItem>>

    @Query("SELECT * FROM MenuItems WHERE id = :id")
    fun getMenuItemById(id: Id): Flowable<DataLayerMenuItem>

    @Insert(onConflict = OnConflictStrategy.FAIL)
    fun insertMenuItems(menuItems: List<DataLayerMenuItem>)

    @Update
    fun updateMenuItem(menuItem: DataLayerMenuItem)

    @Query("UPDATE MenuItems SET rating = :rating WHERE id = :id")
    fun rateMenuItemWithId(id: Id, rating: Rating)

    @Query("DELETE FROM MenuItems")
    fun deleteAllMenuItems()
}