package org.bitspilani.ssms.messapp.screens.profile.core

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import org.bitspilani.ssms.messapp.screens.profile.view.model.UiOrder
import org.bitspilani.ssms.messapp.screens.profile.view.model.ViewLayerUser
import org.bitspilani.ssms.messapp.screens.shared.data.repo.UserRepository
import org.bitspilani.ssms.messapp.util.set
import org.bitspilani.ssms.messapp.util.toMut

class ProfileViewModel(private val uRepo: UserRepository) : ViewModel() {

    val order: LiveData<UiOrder> = MutableLiveData()
    val toast: LiveData<String?> = MutableLiveData()


    private val d1 = CompositeDisposable()
    private val d2 = CompositeDisposable()


    init {
        updateUser()
    }


    fun onLogoutAction() {
        order.toMut().postValue(UiOrder.ShowLoading)
        uRepo.setUser(null).subscribe()
    }


    fun onRefreshQrCodeAction() {
        order.toMut().postValue(UiOrder.ShowLoading)
        d2.set(uRepo.refreshQrCode()
            .subscribe(
                {
                    updateUser()
                    toast.toMut().postValue("QR Code refreshed successfully")
                },
                {
                    order.toMut().postValue(UiOrder.ShowFailure("Something went wrong :("))
                }
            ))
    }


    private fun updateUser() {
        d1.set(uRepo.getUser()
            .subscribe(
                { _user ->
                    order.toMut().postValue(
                        UiOrder.ShowWorking(
                            ViewLayerUser(_user.id, _user.name, _user.room, _user.profilePicUrl, _user.qrCode)
                        )
                    )
                },
                {
                    order.toMut().postValue(UiOrder.ShowFailure("Something went wrong :("))
                },
                {
                    order.toMut().postValue(UiOrder.MoveToLogin)
                }
            ))
    }


    override fun onCleared() {
        super.onCleared()
        d1.clear()
        d2.clear()
    }
}