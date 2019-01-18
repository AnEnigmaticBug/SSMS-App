package org.bitspilani.ssms.messapp.screens.grub.grubdetails.core

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import org.bitspilani.ssms.messapp.screens.grub.grubdetails.view.model.UiOrder
import org.bitspilani.ssms.messapp.screens.grub.grubdetails.view.model.ViewLayerGrub
import org.bitspilani.ssms.messapp.screens.grub.shared.core.model.FoodType
import org.bitspilani.ssms.messapp.screens.grub.shared.core.model.Grub
import org.bitspilani.ssms.messapp.screens.grub.shared.core.model.Id
import org.bitspilani.ssms.messapp.screens.grub.shared.core.model.SigningStatus
import org.bitspilani.ssms.messapp.screens.grub.shared.data.repo.GrubRepository
import org.bitspilani.ssms.messapp.util.*
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime

class GrubDetailsViewModel(private val id: Id, private val gRepo: GrubRepository) : ViewModel() {

    val order: LiveData<UiOrder> = MutableLiveData()
    val toast: LiveData<String?> = SingleLiveEvent()


    private val d1 = CompositeDisposable()


    init {
        initialize()
    }


    fun onRetryAction() {
        initialize()
    }

    @SuppressLint("CheckResult")
    fun onSignUpAction(foodType: FoodType) {
        order.toMut().value = UiOrder.ShowLoading
        gRepo.signUpGrubWithId(id, foodType)
            .subscribe(
                {
                    retrieveData()
                    toast.toMut().postValue("Successfully signed up for the grub")
                },
                {
                    when(it) {
                        is NoLoggedUserException -> order.toMut().postValue(UiOrder.MoveToLogin)
                        is NoDataSourceException -> order.toMut().postValue(UiOrder.ShowFailure("Not connected to internet"))
                        else                     -> toast.toMut().postValue("Something went wrong. Please restart the app..")
                    }
                }
            )
    }

    @SuppressLint("CheckResult")
    fun onCancelAction() {
        order.toMut().value = UiOrder.ShowLoading
        gRepo.cancelGrubWithId(id)
            .subscribe(
                {
                    retrieveData()
                    toast.toMut().postValue("Successfully cancelled the signing")
                },
                {
                    when(it) {
                        is NoLoggedUserException -> order.toMut().postValue(UiOrder.MoveToLogin)
                        is NoDataSourceException -> order.toMut().postValue(UiOrder.ShowFailure("Not connected to internet"))
                        else                     -> toast.toMut().postValue("Something went wrong. Please restart the app..")
                    }
                }
            )
    }


    @SuppressLint("CheckResult")
    private fun initialize() {
        order.toMut().value = UiOrder.ShowLoading

        retrieveData()
    }

    private fun retrieveData() {
        d1.set(gRepo.getGrubById(id)
            .map { _grub ->
                _grub.toViewLayer()
            }
            .map { _grub ->
                UiOrder.ShowWorking(_grub)
            }
            .subscribe(
                { _order ->
                    order.toMut().postValue(_order)
                },
                {
                    when(it) {
                        is NoLoggedUserException -> order.toMut().postValue(UiOrder.MoveToLogin)
                        else                     -> order.toMut().postValue(UiOrder.ShowFailure("Something went wrong"))
                    }
                }
            ))
    }


    private fun Grub.toViewLayer(): ViewLayerGrub {
        val slot = when(this.isSigned()) {
            true  -> this.details.slot?.toString()?: "Not yet allotted"
            false -> "N/A"
        }

        return ViewLayerGrub(
            this.details.name,
            this.details.organizer,
            this.details.foodOption,
            this.details.date.prettyString(),
            this.details.slot1Time.prettyString(),
            this.details.slot2Time.prettyString(),
            this.details.signUpDeadline.prettyString(),
            this.details.cancelDeadline.prettyString(),
            "₹ ${this.vegBatch?.price?: "N/A"}",
            "₹ ${this.vegBatch?.price?: "N/A"}",
            this.vegBatch?.venue?: "N/A",
            this.nonVegBatch?.venue?: "N/A",
            this.vegBatch?.menu?: setOf(),
            this.nonVegBatch?.menu?: setOf(),
            this.details.signingStatus,
            slot
        )
    }

    private fun Grub.isSigned(): Boolean {
        return details.signingStatus == SigningStatus.SignedForVeg || details.signingStatus == SigningStatus.SignedForNonVeg
    }

    private fun LocalDate.prettyString(): String {
        return "${month.toString().toLowerCase().capitalize()}, $dayOfMonth"
    }

    private fun LocalTime.prettyString(): String {
        return "${hour.makeTwoDigit()}:${minute.makeTwoDigit()}"
    }
}