package org.bitspilani.ssms.messapp.screens.splash.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.bitspilani.ssms.messapp.MessApp
import org.bitspilani.ssms.messapp.di.screens.splash.SplashScreenModule
import org.bitspilani.ssms.messapp.screens.shared.data.repo.UserRepository
import javax.inject.Inject

class SplashViewModelFactory : ViewModelProvider.Factory {

    @Inject
    lateinit var userRepository: UserRepository

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        MessApp.appComponent.newSplashScreenComponent(SplashScreenModule()).inject(this)
        return SplashViewModel(userRepository) as T
    }
}