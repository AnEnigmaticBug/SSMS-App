package org.bitspilani.ssms.messapp.screens.grub.grublist.core

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import org.bitspilani.ssms.messapp.screens.grub.grublist.view.model.UiOrder
import org.bitspilani.ssms.messapp.screens.grub.grublist.view.model.ViewLayerGrub
import org.bitspilani.ssms.messapp.screens.grub.shared.core.model.FoodOption
import org.bitspilani.ssms.messapp.screens.grub.shared.core.model.Grub
import org.bitspilani.ssms.messapp.screens.grub.shared.core.model.GrubDetails
import org.bitspilani.ssms.messapp.screens.grub.shared.core.model.SigningStatus
import org.bitspilani.ssms.messapp.screens.grub.shared.data.repo.GrubRepository
import org.bitspilani.ssms.messapp.util.NoLoggedUserException
import org.bitspilani.ssms.messapp.util.set
import org.bitspilani.ssms.messapp.util.toMut

class GrubListViewModel(private val gRepo: GrubRepository) : ViewModel() {

    val order: LiveData<UiOrder> = MutableLiveData()


    private val d1 = CompositeDisposable()


    init {
        initialize()
    }


    fun onRetryAction() {
        initialize()
    }

    fun onViewSignedGrubsAction() {
        showGrubs(true)
    }

    fun onViewUpcomingGrubsAction() {
        showGrubs(false)
    }


    private fun showGrubs(viewOnlySigned: Boolean) {

        fun GrubDetails.isSigned(): Boolean {
            return signingStatus == SigningStatus.SignedForVeg || signingStatus == SigningStatus.SignedForNonVeg
        }

        d1.set(gRepo.getAllGrubDetails()
            .map { _grubs ->
                _grubs
                    .filter { !viewOnlySigned || it.isSigned() }
                    .sortedByDescending {it.date }
                    .map { it.toViewLayer() }
            }
            .subscribe(
                { _grubs ->
                    order.toMut().postValue(UiOrder.ShowWorking(_grubs, viewOnlySigned))
                },
                {
                    if(it is NoLoggedUserException) {
                        order.toMut().postValue(UiOrder.MoveToLogin)
                    }
                    order.toMut().postValue(UiOrder.ShowFailure("Something went wrong"))
                }
            ))
    }

    private fun initialize() {
        order.toMut().value = UiOrder.ShowLoading
        showGrubs(false)
    }

    private fun GrubDetails.toViewLayer(): ViewLayerGrub {
        val date = "${date.month.toString().toLowerCase().capitalize()}, ${date.dayOfMonth}"
        val foodOption = when(foodOption) {
            FoodOption.NonVeg       -> "Non-Veg"
            FoodOption.Veg          -> "Veg"
            FoodOption.VegAndNonVeg -> "Veg + Non-Veg"
        }
        return ViewLayerGrub(id, name, "By: $organizer", date, "Type: $foodOption")
    }


    override fun onCleared() {
        super.onCleared()
        d1.clear()
    }
}