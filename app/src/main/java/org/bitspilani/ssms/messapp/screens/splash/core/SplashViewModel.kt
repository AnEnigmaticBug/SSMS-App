package org.bitspilani.ssms.messapp.screens.splash.core

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.bitspilani.ssms.messapp.screens.shared.data.repo.UserRepository
import org.bitspilani.ssms.messapp.screens.splash.view.model.UiOrder
import org.bitspilani.ssms.messapp.util.toMut

class SplashViewModel(uRepo: UserRepository) : ViewModel() {

    val order: LiveData<UiOrder> = MutableLiveData()

    init {
        uRepo.getUser()
            .subscribe(
                {
                    order.toMut().postValue(UiOrder.MoveToMainApp)
                },
                {
                    throw IllegalStateException("Couldn't uRepo.getUser()...")
                },
                {
                    order.toMut().postValue(UiOrder.MoveToLogin)
                }
            )
    }
}