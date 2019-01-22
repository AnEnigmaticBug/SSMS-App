package org.bitspilani.ssms.messapp.screens.menu.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Flowable
import org.bitspilani.ssms.messapp.screens.menu.core.model.Id
import org.bitspilani.ssms.messapp.screens.menu.data.room.model.*
import org.threeten.bp.LocalDate

@Dao
interface MenusDao {

    @Query("SELECT DISTINCT date FROM Meals ORDER BY date")
    fun getDatesInMenu(): Flowable<List<LocalDate>>

    @Query("SELECT MAX(date) FROM Meals")
    fun getMaxStoredDate(): LocalDate?

    @Query("SELECT Servings.serving_id, MenuItems.item_name, Meals.date, Meals.type, IFNULL(Ratings.rating, 'NotRated') AS rating " +
            "FROM Servings " +
            "LEFT JOIN Meals     ON Servings.meal_id = Meals.meal_id " +
            "LEFT JOIN MenuItems ON Servings.item_id = MenuItems.item_id " +
            "LEFT JOIN Ratings   ON Servings.item_id = Ratings.item_id AND Servings.meal_id = Ratings.meal_id " +
            "WHERE date = :date AND MenuItems.item_name IS NOT NULL AND Meals.date IS NOT NULL AND Meals.type IS NOT NULL")
    fun getMenuItemsByDate(date: LocalDate): Flowable<List<DataLayerMenuItem>>

    @Query("SELECT * FROM Servings WHERE serving_id = :servingId LIMIT 1")
    fun getServingTupleById(servingId: Id): ServingTuple

    @Query("SELECT * FROM Ratings WHERE rating != 'NotRated'")
    fun getAllRatingTuples(): Flowable<List<RatingTuple>>

    @Query("SELECT COUNT(serving_id) FROM Servings")
    fun getServingTupleCount(): Flowable<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMealTuples(meals: List<MealTuple>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertItemTuples(items: List<MenuItemTuple>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertServingTuples(servings: List<ServingTuple>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRatingTuple(rating: RatingTuple)

    @Query("DELETE FROM Meals")
    fun deleteAllMealTuples()

    @Query("DELETE FROM MenuItems")
    fun deleteAllItemTuples()

    @Query("DELETE FROM Servings")
    fun deleteAllServingTuples()
}