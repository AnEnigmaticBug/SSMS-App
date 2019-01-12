package org.bitspilani.ssms.messapp.screens.grub.shared.data.repo

import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import org.bitspilani.ssms.messapp.screens.grub.shared.core.model.Grub
import org.bitspilani.ssms.messapp.screens.grub.shared.core.model.GrubBatch
import org.bitspilani.ssms.messapp.screens.grub.shared.core.model.Id
import org.bitspilani.ssms.messapp.screens.grub.shared.core.model.Status
import org.bitspilani.ssms.messapp.screens.grub.shared.data.room.GrubBatchesDao
import org.bitspilani.ssms.messapp.screens.grub.shared.data.room.model.DataLayerGrubBatch
import org.threeten.bp.LocalDateTime
import java.util.concurrent.TimeUnit

class GrubRepositoryImpl(private val grubBatchesDao: GrubBatchesDao) : GrubRepository {

    init {
        Completable.fromAction {
            grubBatchesDao.deleteAllGrubBatches()
            grubBatchesDao.insertGrubBatches(listOf(
                DataLayerGrubBatch(1, 1, "Dilli Darbar", "Capitol", setOf(), LocalDateTime.now().plusDays(2), LocalDateTime.now().minusDays(1), "VK Mess", 200, true, true, false),
                DataLayerGrubBatch(2, 1, "Dilli Darbar", "Capitol", setOf(), LocalDateTime.now().plusDays(2), LocalDateTime.now().minusDays(1), "RP Mess", 250, false, true, false),
                DataLayerGrubBatch(3, 2, "Abhiruchi", "Andhra Samiti", setOf(), LocalDateTime.now().plusDays(4), LocalDateTime.now().plusDays(1), "VK Mess", 180, true, true, false),
                DataLayerGrubBatch(4, 2, "Abhiruchi", "Andhra Samiti", setOf(), LocalDateTime.now().plusDays(4), LocalDateTime.now().plusDays(1), "RP Mess", 200, false, true, false),
                DataLayerGrubBatch(5, 3, "Lazzat", "Udgam", setOf(), LocalDateTime.now().plusDays(5), LocalDateTime.now().plusDays(3), "VK Mess", 160, true, true, false),
                DataLayerGrubBatch(6, 4, "Aaswad", "Maharastra Mandal", setOf(), LocalDateTime.now().plusDays(14), LocalDateTime.now().plusDays(10), "VK Mess", 220, true, false, true),
                DataLayerGrubBatch(7, 4, "Aaswad", "Maharastra Mandal", setOf(), LocalDateTime.now().plusDays(14), LocalDateTime.now().plusDays(10), "RP Mess", 300, false, false, false),
                DataLayerGrubBatch(8, 5, "Tripti", "Maurya Vihar", setOf(), LocalDateTime.now().plusDays(23), LocalDateTime.now().plusDays(20), "VK Mess", 260, true, false, false)
            ))
        }.subscribeOn(Schedulers.io()).subscribe()
    }

    override fun getLastUpdatedDateTime(): Maybe<LocalDateTime> {
        return Maybe.just(LocalDateTime.now().minusDays(2))
    }

    override fun forceDataRefresh(): Completable {
        return Completable.complete().delay(3, TimeUnit.SECONDS)
    }

    override fun getAllGrubs(): Observable<List<Grub>> {
        return grubBatchesDao.getAllGrubBatches()
            .map { _batches ->
                _batches.groupBy { it.grubInstanceId }.map { it.value }.map { it.toGrub() }
            }
            .toObservable()
    }

    override fun getGrubById(id: Id): Observable<Grub> {
        return grubBatchesDao.getGrubBatchesByGrubInstanceId(id)
            .map { it.toGrub() }
            .toObservable()
    }

    override fun signUpGrubWithId(id: Id, type: GrubRepository.GrubType): Completable {
        return Completable.fromAction {
            val batchId = grubBatchesDao.getGrubBatchId(id, type == GrubRepository.GrubType.Veg)
        }
            .subscribeOn(Schedulers.io())
            .delay(2, TimeUnit.SECONDS)
    }

    override fun cancelGrubWithId(id: Id, type: GrubRepository.GrubType): Completable {
        return Completable.fromAction {
            val batchId = grubBatchesDao.getGrubBatchId(id, type == GrubRepository.GrubType.Veg)
        }
            .subscribeOn(Schedulers.io())
            .delay(2, TimeUnit.SECONDS)
    }

    private fun DataLayerGrubBatch.toCoreLayer(): GrubBatch {
        return GrubBatch(menu, venue, price)
    }

    private fun List<DataLayerGrubBatch>.toGrub(): Grub {

        fun DataLayerGrubBatch?.isSigned(): Boolean {
            return this?.isSigned?: false
        }

        val vegBatch = this.firstOrNull { it.isVeg }
        val nonVegBatch = this.firstOrNull { !it.isVeg }

        require(vegBatch != null || nonVegBatch != null) { "Grub has no menu" }
        require(this.count() <= 2) { "Grub has more than two menus" }

        val status = when {
             vegBatch.isSigned() && !nonVegBatch.isSigned() -> Status.SignedForVeg
            !vegBatch.isSigned() &&  nonVegBatch.isSigned() -> Status.SignedForNonVeg
             vegBatch.isSigned() &&  nonVegBatch.isSigned() -> throw IllegalArgumentException("Signed for both menus")
            else                                            -> {
                when(this.first().isAvailable) {
                    true  -> Status.Available
                    false -> Status.NotAvailable
                }
            }
        }

        return Grub(
            this.first().grubInstanceId,
            this.first().name,
            this.first().organizer,
            this.first().datetime,
            this.first().cancellationLimit,
            vegBatch?.toCoreLayer(),
            nonVegBatch?.toCoreLayer(),
            status
        )
    }
}