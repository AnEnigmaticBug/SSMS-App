package org.bitspilani.ssms.messapp.screens.grub.shared.data.room

import androidx.room.*
import io.reactivex.Flowable
import org.bitspilani.ssms.messapp.screens.grub.shared.core.model.FoodType
import org.bitspilani.ssms.messapp.screens.grub.shared.core.model.GrubDetails
import org.bitspilani.ssms.messapp.screens.grub.shared.core.model.Id
import org.bitspilani.ssms.messapp.screens.grub.shared.data.room.model.DataLayerGrubBatch

@Dao
interface GrubBatchesDao {

    @Query("SELECT DISTINCT grubId grubId, name, organizer, foodOption, date, slot1Time, slot2Time, signUpDeadline, cancelDeadline, signingStatus, slot FROM GrubBatches")
    fun getAllGrubDetails(): Flowable<List<GrubDetails>>

    @Query("SELECT * FROM GrubBatches WHERE grubId = :grubId")
    fun getGrubBatchesByGrubId(grubId: Id): Flowable<List<DataLayerGrubBatch>>

    @Query("SELECT id FROM GrubBatches WHERE grubId = :grubId AND foodType = :foodType")
    fun getGrubBatchId(grubId: Id, foodType: FoodType): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGrubBatches(grubBatches: List<DataLayerGrubBatch>)

    @Update
    fun updateGrubBatch(grubBatch: DataLayerGrubBatch)

    @Query("DELETE FROM GrubBatches")
    fun deleteAllGrubBatches()

    @Query("DELETE FROM GrubBatches WHERE id = :id")
    fun deleteGrubBatchById(id: Long)
}