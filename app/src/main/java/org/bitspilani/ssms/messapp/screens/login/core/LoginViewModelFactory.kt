package org.bitspilani.ssms.messapp.screens.login.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.bitspilani.ssms.messapp.MessApp
import org.bitspilani.ssms.messapp.di.screens.login.LoginScreenModule
import org.bitspilani.ssms.messapp.screens.shared.data.repo.UserRepository
import javax.inject.Inject

class LoginViewModelFactory : ViewModelProvider.Factory {

    @Inject
    lateinit var userRepository: UserRepository

    init {
        MessApp.appComponent.newLoginScreenComponent(LoginScreenModule()).inject(this)
    }

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return LoginViewModel(userRepository) as T
    }
}