package org.bitspilani.ssms.messapp.screens.notice.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Flowable
import org.bitspilani.ssms.messapp.screens.notice.core.model.Id
import org.bitspilani.ssms.messapp.screens.notice.data.room.model.DataLayerNotice

@Dao
interface NoticesDao {

    @Query("SELECT * FROM Notices")
    fun getAllNotices(): Flowable<List<DataLayerNotice>>

    @Query("SELECT id FROM Notices")
    fun getAllIds(): List<Id>

    @Query("SELECT COUNT(id) FROM Notices")
    fun getNoticeCount(): Flowable<Int>

    @Insert(onConflict = OnConflictStrategy.FAIL)
    fun insertNotices(notices: List<DataLayerNotice>)

    @Query("DELETE FROM Notices")
    fun deleteAllNotices()

    @Query("DELETE FROM Notices WHERE id = :id")
    fun deleteNoticeById(id: Id)
}