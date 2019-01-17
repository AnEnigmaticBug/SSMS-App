package org.bitspilani.ssms.messapp.screens.login.core

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.bitspilani.ssms.messapp.screens.login.view.model.UiOrder
import org.bitspilani.ssms.messapp.screens.shared.data.repo.UserRepository
import org.bitspilani.ssms.messapp.util.InvalidCredentialsException
import org.bitspilani.ssms.messapp.util.NoConnectionException
import org.bitspilani.ssms.messapp.util.SingleLiveEvent
import org.bitspilani.ssms.messapp.util.toMut

class LoginViewModel(private val uRepo: UserRepository) : ViewModel() {

    val order: LiveData<UiOrder> = MutableLiveData()
    val toast: LiveData<String?> = SingleLiveEvent()


    init {
        order.toMut().postValue(UiOrder.ShowLoading)
        initialize()
    }


    @SuppressLint("CheckResult")
    fun onLoginAction(idToken: String, profilePicUrl: String) {
        order.toMut().postValue(UiOrder.ShowLoading)
        uRepo.login(idToken, profilePicUrl)
            .subscribe(
                {
                    order.toMut().postValue(UiOrder.MoveToMainApp)
                },
                {
                    order.toMut().postValue(UiOrder.ShowWorking)
                    val message = when(it) {
                        is NoConnectionException       -> "Device isn't connected to the internet"
                        is InvalidCredentialsException -> "Invalid sign-in credentials. Try again"
                        else                           -> "Couldn't sign-in right now.  Try again"
                    }
                    toast.toMut().postValue(message)
                }
            )
    }


    @SuppressLint("CheckResult")
    private fun initialize() {
        uRepo.setUser(null)
            .subscribe(
                {
                    order.toMut().postValue(UiOrder.ShowWorking)
                },
                {
                    toast.toMut().postValue("A serious error occured. Please restart the app")
                }
            )
    }
}