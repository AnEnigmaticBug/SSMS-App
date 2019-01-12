package org.bitspilani.ssms.messapp.screens.grub.shared.data.room

import androidx.room.*
import io.reactivex.Flowable
import org.bitspilani.ssms.messapp.screens.grub.shared.core.model.Id
import org.bitspilani.ssms.messapp.screens.grub.shared.data.room.model.DataLayerGrubBatch

@Dao
interface GrubBatchesDao {

    @Query("SELECT * FROM GrubBatches")
    fun getAllGrubBatches(): Flowable<List<DataLayerGrubBatch>>

    @Query("SELECT * FROM GrubBatches WHERE grubInstanceId = :grubInstanceId")
    fun getGrubBatchesByGrubInstanceId(grubInstanceId: Id): Flowable<List<DataLayerGrubBatch>>

    @Query("SELECT id FROM GrubBatches WHERE grubInstanceId = :grubInstanceId AND isVeg = :isVeg")
    fun getGrubBatchId(grubInstanceId: Id, isVeg: Boolean): Long

    @Insert(onConflict = OnConflictStrategy.FAIL)
    fun insertGrubBatches(grubBatches: List<DataLayerGrubBatch>)

    @Update
    fun updateGrubBatch(grubBatch: DataLayerGrubBatch)

    @Query("DELETE FROM GrubBatches")
    fun deleteAllGrubBatches()

    @Query("DELETE FROM GrubBatches WHERE id = :id")
    fun deleteGrubBatchById(id: Long)
}