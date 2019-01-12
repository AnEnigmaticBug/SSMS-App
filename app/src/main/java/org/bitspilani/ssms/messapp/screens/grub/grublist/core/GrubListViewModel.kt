package org.bitspilani.ssms.messapp.screens.grub.grublist.core

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import org.bitspilani.ssms.messapp.screens.grub.grublist.view.model.UiOrder
import org.bitspilani.ssms.messapp.screens.grub.grublist.view.model.ViewLayerGrub
import org.bitspilani.ssms.messapp.screens.grub.shared.core.model.Grub
import org.bitspilani.ssms.messapp.screens.grub.shared.core.model.Status
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
        d1.set(gRepo.getAllGrubs()
            .map { _grubs ->
                _grubs.filter { !viewOnlySigned || it.isSigned() }.sortedByDescending { it.datetime }.map { it.toViewLayer() }
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


    private fun Grub.isSigned(): Boolean {
        return this.status == Status.SignedForVeg || this.status == Status.SignedForNonVeg
    }

    private fun Grub.toViewLayer(): ViewLayerGrub {
        val date = "${datetime.month.toString().toLowerCase().capitalize()}, ${datetime.dayOfMonth}"
        val type = when {
            vegBatch == null && nonVegBatch != null -> "Non-Veg"
            vegBatch != null && nonVegBatch == null -> "Veg"
            vegBatch != null && nonVegBatch != null -> "Veg + Non-Veg"
            else                                    -> throw IllegalArgumentException("Grub contains no batch")
        }
        return ViewLayerGrub(id, name, "By: $organizer", date, "Type: $type")
    }


    override fun onCleared() {
        super.onCleared()
        d1.clear()
    }
}