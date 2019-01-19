package org.bitspilani.ssms.messapp.screens.grub.shared.data.repo

import android.util.Log
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import org.bitspilani.ssms.messapp.screens.grub.shared.core.model.*
import org.bitspilani.ssms.messapp.screens.grub.shared.data.retrofit.GrubService
import org.bitspilani.ssms.messapp.screens.grub.shared.data.retrofit.model.GrubMenuResponse
import org.bitspilani.ssms.messapp.screens.grub.shared.data.retrofit.model.GrubResponse
import org.bitspilani.ssms.messapp.screens.grub.shared.data.retrofit.model.TicketResponse
import org.bitspilani.ssms.messapp.screens.grub.shared.data.room.GrubBatchesDao
import org.bitspilani.ssms.messapp.screens.grub.shared.data.room.model.DataLayerGrubBatch
import org.bitspilani.ssms.messapp.screens.shared.data.repo.UserRepository
import org.bitspilani.ssms.messapp.util.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime

class GrubRepositoryImpl(
    private val userRepository: UserRepository,
    private val grubBatchesDao: GrubBatchesDao,
    private val grubService: GrubService
) : GrubRepository {

    private var isDataFresh = false

    override fun forceDataRefresh(): Completable {
        return fetchAndUpdateGrubs()
            .doOnComplete { isDataFresh = true }
    }

    override fun getAllGrubDetails(): Observable<List<GrubDetails>> {
        if(isDataFresh) {
            return grubBatchesDao.getAllGrubDetails()
                .toObservable()
        }
        return forceDataRefresh().andThen(grubBatchesDao.getAllGrubDetails())
            .toObservable()
    }

    override fun getGrubById(id: Id): Observable<Grub> {
        return grubBatchesDao.getGrubBatchesByGrubId(id)
            .filter { it.isNotEmpty() }
            .map { it.toGrub() }
            .toObservable()
    }

    override fun signUpGrubWithId(id: Id, type: FoodType): Completable {
        return userRepository.getUser()
            .flatMapCompletable { _user ->
                val body = JSONObject().apply {
                    put("ids", JSONArray(listOf(grubBatchesDao.getGrubBatchId(id, type))))
                }.toRequestBody()
                grubService.signUpGrub(_user.jwt, body)
            }
            .subscribeOn(Schedulers.io())
    }

    override fun cancelGrubWithId(id: Id): Completable {
        return userRepository.getUser()
            .flatMapCompletable { _user ->
                val body = JSONObject().apply {
                    put("id", grubBatchesDao.getSignedGrubBatchId(id))
                }.toRequestBody()
                grubService.cancelGrub(_user.jwt, body)
            }
    }

    private fun fetchAndUpdateGrubs(): Completable {
        return userRepository.getUser()
            .flatMapSingle { _user ->
                grubService.getGrubs()
                    .map { _response ->
                        when(_response.code()) {
                            200  -> _response.body()!!
                            else -> throw Exception("${_response.code()}: ${_response.errorBody()?.string()}")
                        }
                    }
                    .map { _grubs ->
                        _grubs.map { _grub ->

                            _grub.menus.map { _menu ->
                                DataLayerGrubBatch(
                                    _menu.id,
                                    _grub.id,
                                    _grub.name,
                                    _grub.organizer,
                                    _grub.getFoodOption(),
                                    LocalDate.parse(_grub.date),
                                    LocalTime.parse(_grub.slot1Time),
                                    LocalTime.parse(_grub.slot2Time),
                                    LocalDate.parse(_grub.signUpDeadline),
                                    LocalDate.parse(_grub.cancelDeadline),
                                    _grub.getSigningStatus(),
                                    null,
                                    _menu.getFoodType(),
                                    _menu.items.map { it.name }.toSet(),
                                    _menu.venue,
                                    _menu.price.toFloat().toInt(),
                                    null
                                )
                            }
                        }
                    }
                    .map { it.flatten() }
                    .flatMap { _grubBatches ->
                        grubService.getTickets(_user.jwt)
                            .map { _response ->
                                Log.d("GrubRepositoryImpl", "${_response.code()}")

                                when (_response.code()) {
                                    200 -> _response.body()!!
                                    else -> throw Exception("${_response.code()}: ${_response.errorBody()?.string()}")
                                }
                            }
                            .map { _tickets ->
                                _grubBatches.updateWithTicketInfo(_tickets)
                            }
                    }
            }
            .doOnSuccess { _grubBatches ->
                grubBatchesDao.deleteAllGrubBatches()
                grubBatchesDao.insertGrubBatches(_grubBatches)
            }
            .ignoreElement()
            .subscribeOn(Schedulers.io())
    }


    private fun List<DataLayerGrubBatch>.updateWithTicketInfo(tickets: List<TicketResponse>): List<DataLayerGrubBatch> {
        return this.map { _grubBatch ->
            when (_grubBatch.id) {
                in tickets.map { it.id } -> {
                    val ticket = tickets.find { it.id == _grubBatch.id }!!
                    _grubBatch.copy(slot = ticket.slot.toSlot(), ticketId = ticket.id)
                }
                else -> _grubBatch
            }
        }
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