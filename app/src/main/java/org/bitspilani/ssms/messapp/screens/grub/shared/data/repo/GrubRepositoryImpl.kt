package org.bitspilani.ssms.messapp.screens.grub.shared.data.repo

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import org.bitspilani.ssms.messapp.screens.grub.shared.core.model.*
import org.bitspilani.ssms.messapp.screens.grub.shared.data.room.GrubBatchesDao
import org.bitspilani.ssms.messapp.screens.grub.shared.data.room.model.DataLayerGrubBatch
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime
import java.util.concurrent.TimeUnit

class GrubRepositoryImpl(private val grubBatchesDao: GrubBatchesDao) : GrubRepository {

    init {
        Completable.fromAction {
            grubBatchesDao.deleteAllGrubBatches()
            val today = LocalDate.now()
            val time1 = LocalTime.of(20, 30)
            val time2 = LocalTime.of(21, 30)
            grubBatchesDao.insertGrubBatches(listOf(
                DataLayerGrubBatch(1, 1, "Dilli Darbar", "Capitol", FoodOption.VegAndNonVeg, today.plusDays(2), time1, time2, today.minusDays(4), today.minusDays(2), SigningStatus.Available, null, FoodType.Veg, setOf(), "VK Mess", 200, null),
                DataLayerGrubBatch(2, 1, "Dilli Darbar", "Capitol", FoodOption.VegAndNonVeg, today.plusDays(2), time1, time2, today.minusDays(4), today.minusDays(2), SigningStatus.Available, null, FoodType.NonVeg, setOf(), "RP Mess", 250, null),
                DataLayerGrubBatch(3, 2, "Abhiruchi", "Andhra Samiti", FoodOption.VegAndNonVeg, today.plusDays(4), time1, time2, today.minusDays(2), today.plusDays(0), SigningStatus.Available, Slot.Slot1, FoodType.Veg, setOf(), "VK Mess", 180, null),
                DataLayerGrubBatch(4, 2, "Abhiruchi", "Andhra Samiti", FoodOption.VegAndNonVeg, today.plusDays(4), time1, time2, today.minusDays(2), today.plusDays(0), SigningStatus.Available, Slot.Slot1, FoodType.NonVeg, setOf(), "RP Mess", 200, null),
                DataLayerGrubBatch(5, 3, "Lazzat", "Udgam", FoodOption.Veg, today.plusDays(3), time1, time2, today.minusDays(3), today.minusDays(1), SigningStatus.SignedForNonVeg, Slot.Slot2, FoodType.NonVeg, setOf(), "VK Mess", 160, null),
                DataLayerGrubBatch(6, 4, "Aaswad", "Maharastra Mandal", FoodOption.VegAndNonVeg, today.plusDays(10), time1, time2, today.plusDays(4), today.plusDays(6), SigningStatus.SignedForVeg, null, FoodType.Veg, setOf(), "VK Mess", 220, null),
                DataLayerGrubBatch(7, 4, "Aaswad", "Maharastra Mandal", FoodOption.VegAndNonVeg, today.plusDays(10), time1, time2, today.plusDays(4), today.plusDays(6), SigningStatus.SignedForVeg, null, FoodType.NonVeg, setOf(), "RP Mess", 300, null),
                DataLayerGrubBatch(8, 5, "Tripti", "Maurya Vihar", FoodOption.NonVeg, today.plusDays(20), time1, time2, today.plusDays(14), today.plusDays(16), SigningStatus.NotAvailable, null, FoodType.Veg, setOf(), "VK Mess", 260, null)
            ))
        }.subscribeOn(Schedulers.io()).subscribe()
    }

    override fun forceDataRefresh(): Completable {
        return Completable.complete().delay(3, TimeUnit.SECONDS)
    }

    override fun getAllGrubDetails(): Observable<List<GrubDetails>> {
        return grubBatchesDao.getAllGrubDetails()
            .toObservable()
    }

    override fun getGrubById(id: Id): Observable<Grub> {
        return grubBatchesDao.getGrubBatchesByGrubId(id)
            .filter { it.isNotEmpty() }
            .map { it.toGrub() }
            .toObservable()
    }

    override fun signUpGrubWithId(id: Id, type: FoodType): Completable {
        return Completable.fromAction {
            val batchId = grubBatchesDao.getGrubBatchId(id, type)
        }
            .subscribeOn(Schedulers.io())
            .delay(2, TimeUnit.SECONDS)
    }

    override fun cancelGrubWithId(id: Id, type: FoodType): Completable {
        return Completable.fromAction {
            val batchId = grubBatchesDao.getGrubBatchId(id, type)
        }
            .subscribeOn(Schedulers.io())
            .delay(2, TimeUnit.SECONDS)
    }

    private fun DataLayerGrubBatch.toCoreLayer(): GrubBatch {
        return GrubBatch(foodType, menu, venue, price)
    }

    private fun List<DataLayerGrubBatch>.toGrub(): Grub {
        val vegBatch = this.firstOrNull { it.foodType == FoodType.Veg }
        val nonVegBatch = this.firstOrNull { it.foodType == FoodType.NonVeg }

        require(vegBatch != null || nonVegBatch != null) { "Grub has no menu" }
        require(this.count() <= 2) { "Grub has more than two menus" }

        return Grub(
            GrubDetails(
                this.first().grubId,
                this.first().name,
                this.first().organizer,
                this.first().foodOption,
                this.first().date,
                this.first().slot1Time,
                this.first().slot2Time,
                this.first().signUpDeadline,
                this.first().cancelDeadline,
                this.first().signingStatus,
                this.first().slot
            ),
            vegBatch?.toCoreLayer(),
            nonVegBatch?.toCoreLayer()
        )
    }
}