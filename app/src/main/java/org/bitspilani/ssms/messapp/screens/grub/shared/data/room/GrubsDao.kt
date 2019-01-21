package org.bitspilani.ssms.messapp.screens.grub.shared.data.room

import androidx.room.*
import io.reactivex.Flowable
import org.bitspilani.ssms.messapp.screens.grub.shared.core.model.FoodType
import org.bitspilani.ssms.messapp.screens.grub.shared.core.model.Id
import org.bitspilani.ssms.messapp.screens.grub.shared.core.model.Slot
import org.bitspilani.ssms.messapp.screens.grub.shared.data.room.model.DataLayerGrub
import org.bitspilani.ssms.messapp.screens.grub.shared.data.room.model.DataLayerGrubBatch
import org.bitspilani.ssms.messapp.screens.grub.shared.data.room.model.DataLayerTicket

@Dao
interface GrubsDao {

    @Query("SELECT * FROM Grubs")
    fun getAllGrubs(): Flowable<List<DataLayerGrub>>

    @Query("SELECT * FROM Grubs WHERE id = :id")
    fun getGrubById(id: Id): Flowable<DataLayerGrub>

    @Query("SELECT * FROM GrubBatches")
    fun getAllGrubBatches(): Flowable<List<DataLayerGrubBatch>>

    @Query("SELECT * FROM GrubBatches WHERE grub_id = :grubId")
    fun getGrubBatchesByGrubId(grubId: Id): Flowable<List<DataLayerGrubBatch>>

    @Query("SELECT grub_batch_id FROM GrubBatches WHERE grub_id = :grubId AND food_type = :foodType")
    fun getGrubBatchId(grubId: Id, foodType: FoodType): Long

    @Query("SELECT GrubBatches.food_type FROM Tickets JOIN GrubBatches ON Tickets.grub_batch_id = GrubBatches.grub_batch_id JOIN Grubs ON GrubBatches.grub_id = Grubs.id WHERE Grubs.id = :grubId")
    fun getSignedFoodTypesForGrubById(grubId: Id): List<FoodType>

    @Query("SELECT slot FROM Tickets JOIN GrubBatches ON Tickets.grub_batch_id = GrubBatches.grub_batch_id JOIN Grubs ON GrubBatches.grub_id = Grubs.id WHERE Grubs.id = :grubId")
    fun getSlotForGrubById(grubId: Id): Slot

    @Query("SELECT ticket_id FROM Tickets JOIN GrubBatches ON Tickets.grub_batch_id = GrubBatches.grub_batch_id JOIN Grubs ON GrubBatches.grub_id = Grubs.id WHERE Grubs.id = :grubId")
    fun getTicketIdForGrubOfId(grubId: Id): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGrubs(grubs: List<DataLayerGrub>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGrubBatches(grubBatches: List<DataLayerGrubBatch>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTickets(tickets: List<DataLayerTicket>)

    @Update
    fun updateGrub(grub: DataLayerGrub)

    @Update
    fun updateGrubBatch(grubBatch: DataLayerGrubBatch)

    @Query("DELETE FROM Grubs")
    fun deleteAllGrubs()

    @Query("DELETE FROM GrubBatches")
    fun deleteAllGrubBatches()

    @Query("DELETE FROM Tickets")
    fun deleteAllTickets()

    @Query("DELETE FROM Grubs WHERE id = :id")
    fun deleteGrubById(id: Id)

    @Query("DELETE FROM GrubBatches WHERE grub_batch_id = :id")
    fun deleteGrubBatchById(id: Long)
}