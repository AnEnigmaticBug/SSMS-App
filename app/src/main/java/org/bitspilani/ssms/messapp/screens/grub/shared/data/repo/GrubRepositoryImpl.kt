package org.bitspilani.ssms.messapp.screens.grub.shared.data.repo

import android.util.Log
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import org.bitspilani.ssms.messapp.screens.grub.shared.core.model.*
import org.bitspilani.ssms.messapp.screens.grub.shared.data.retrofit.GrubService
import org.bitspilani.ssms.messapp.screens.grub.shared.data.retrofit.model.GrubMenuResponse
import org.bitspilani.ssms.messapp.screens.grub.shared.data.retrofit.model.GrubResponse
import org.bitspilani.ssms.messapp.screens.grub.shared.data.room.GrubsDao
import org.bitspilani.ssms.messapp.screens.grub.shared.data.room.model.DataLayerGrub
import org.bitspilani.ssms.messapp.screens.grub.shared.data.room.model.DataLayerGrubBatch
import org.bitspilani.ssms.messapp.screens.grub.shared.data.room.model.DataLayerTicket
import org.bitspilani.ssms.messapp.screens.shared.data.repo.UserRepository
import org.bitspilani.ssms.messapp.util.getBody
import org.bitspilani.ssms.messapp.util.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime

class GrubRepositoryImpl(
    private val userRepository: UserRepository,
    private val grubsDao: GrubsDao,
    private val grubService: GrubService
) : GrubRepository {

    private var isDataFresh = false

    override fun forceDataRefresh(): Completable {
        return fetchAndUpdateGrubs()
            .doOnComplete { isDataFresh = true }
    }

    override fun getAllGrubDetails(): Observable<List<GrubDetails>> {
        val localSource = grubsDao.getAllGrubs()
            .map { _grubs ->
                _grubs.map { _grub -> _grub.getDetails() }
            }

        if(isDataFresh) {
            return localSource.toObservable()
        }
        return forceDataRefresh().andThen(localSource)
            .toObservable()
    }

    override fun getGrubById(id: Id): Observable<Grub> {
        return grubsDao.getGrubById(id)
            .flatMap { _grub ->
                val details = _grub.getDetails()

                grubsDao.getGrubBatchesByGrubId(id)
                    .map { _grubBatches ->
                        val vegBatch = _grubBatches.firstOrNull { it.foodType == FoodType.Veg }
                        val nonVegBatch = _grubBatches.firstOrNull { it.foodType == FoodType.NonVeg }
                        Log.d("GrubRepositoryImpl", vegBatch!!.menu.joinToString("<|>"))
                        Grub(
                            details, vegBatch?.toCoreLayer(), nonVegBatch?.toCoreLayer()
                        )
                    }
            }
            .subscribeOn(Schedulers.io())
            .toObservable()
    }

    override fun signUpGrubWithId(id: Id, type: FoodType): Completable {
        return userRepository.getUser()
            .flatMapCompletable { _user ->
                val body = JSONObject().apply {
                    put("ids", JSONArray(listOf(grubsDao.getGrubBatchId(id, type))))
                }.toRequestBody()
                grubService.signUpGrub(_user.jwt, body)
                    .map { _response -> _response.getBody() }
                    .flatMapCompletable { fetchAndUpdateGrubs() }
            }
            .subscribeOn(Schedulers.io())
    }

    override fun cancelGrubWithId(id: Id): Completable {
        return userRepository.getUser()
            .flatMapCompletable { _user ->
                val body = JSONObject().apply {
                    put("id", grubsDao.getTicketIdForGrubOfId(id))
                }.toRequestBody()
                grubService.cancelGrub(_user.jwt, body)
                    .map { _response -> _response.getBody() }
                    .flatMapCompletable { fetchAndUpdateGrubs() }
            }
            .subscribeOn(Schedulers.io())
    }

    private fun fetchAndUpdateGrubs(): Completable {
        return userRepository.getUser()
            .flatMapCompletable { _user ->
                grubService.getGrubs()
                    .map { _response ->
                        _response.getBody()
                    }
                    .map { _grubs ->
                        Pair(_grubs.extractGrubs(), _grubs.extractGrubBatches())
                    }
                    .doOnSuccess { _pair ->
                        grubsDao.deleteAllGrubs()
                        grubsDao.deleteAllGrubBatches()

                        grubsDao.insertGrubs(_pair.first)
                        grubsDao.insertGrubBatches(_pair.second)
                    }
                    .flatMapCompletable { fetchAndUpdateTickets(_user.jwt) }
            }
            .subscribeOn(Schedulers.io())
    }


    private fun fetchAndUpdateTickets(jwt: String): Completable {
        return grubService.getTickets(jwt)
            .map { _response ->
                _response.getBody()
            }
            .map { _tickets ->
                _tickets.map { _ticket ->
                    DataLayerTicket(_ticket.id, _ticket.grubBatchId, _ticket.slot.toSlot())
                }
            }
            .doOnSuccess { _tickets ->
                grubsDao.deleteAllTickets()
                grubsDao.insertTickets(_tickets)
            }
            .ignoreElement()
            .subscribeOn(Schedulers.io())
    }


    private fun DataLayerGrub.getDetails(): GrubDetails {
        val signedFoodTypes = grubsDao.getSignedFoodTypesForGrubById(this.id)
        val signingStatus = when {
            signedFoodTypes.isEmpty()                 -> SigningStatus.Available
            signedFoodTypes.contains(FoodType.Veg)    -> SigningStatus.SignedForVeg
            signedFoodTypes.contains(FoodType.NonVeg) -> SigningStatus.SignedForNonVeg
            else                                      -> throw IllegalStateException()
        }
        return GrubDetails(
            id,
            name,
            organizer,
            foodOption,
            date,
            slot1Time,
            slot2Time,
            signUpDeadline,
            cancelDeadline,
            signingStatus,
            grubsDao.getSlotForGrubById(id)
        )
    }

    private fun List<GrubResponse>.extractGrubs(): List<DataLayerGrub> {
        return this.map { _grub ->
            DataLayerGrub(
                _grub.id,
                _grub.name,
                _grub.organizer,
                _grub.getFoodOption(),
                LocalDate.parse(_grub.date),
                LocalTime.parse(_grub.slot1Time),
                LocalTime.parse(_grub.slot2Time),
                LocalDate.parse(_grub.signUpDeadline),
                LocalDate.parse(_grub.cancelDeadline)
            )
        }
    }

    private fun List<GrubResponse>.extractGrubBatches(): List<DataLayerGrubBatch> {
        return this.map { _grub ->
            _grub.menus.map { _menu ->
                DataLayerGrubBatch(
                    _menu.id,
                    _grub.id,
                    _menu.getFoodType(),
                    _menu.items.map { it.name }.toSet(),
                    _menu.venue,
                    _menu.price.toFloat().toInt()
                )
            }
        }.flatten()
    }

    private fun GrubResponse.getSigningStatus(): SigningStatus {
        return when(status) {
            "Active" -> SigningStatus.Available
            else     -> SigningStatus.NotAvailable
        }
    }

    private fun GrubMenuResponse.getFoodType(): FoodType {
        return when(category) {
            "Veg"     -> FoodType.Veg
            "Non-veg" -> FoodType.NonVeg
            else      -> throw java.lang.IllegalArgumentException("Grub($id) without veg or non-veg menus")
        }
    }

    private fun GrubResponse.getFoodOption(): FoodOption {
        val hasVegOption = this.menus.any { it.category == "Veg" }
        val hasNonVegOption = this.menus.any { it.category == "Non-veg" }
        return when {
             hasVegOption && !hasNonVegOption -> FoodOption.Veg
            !hasVegOption &&  hasNonVegOption -> FoodOption.NonVeg
             hasVegOption &&  hasNonVegOption -> FoodOption.VegAndNonVeg
            else                              -> throw IllegalArgumentException("Grub($id) without veg or non-veg menus")
        }
    }

    private fun String.toSlot(): Slot {
        return when(this) {
            "A"  -> Slot.Slot1
            "B"  -> Slot.Slot2
            else -> throw IllegalArgumentException("Unknown slot: $this")
        }
    }

    private fun DataLayerGrubBatch.toCoreLayer(): GrubBatch {
        return GrubBatch(foodType, menu, venue, price)
    }
}