package org.bitspilani.ssms.messapp.screens.profile.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.bitspilani.ssms.messapp.MessApp
import org.bitspilani.ssms.messapp.di.screens.profile.ProfileScreenModule
import org.bitspilani.ssms.messapp.screens.shared.data.repo.UserRepository
import javax.inject.Inject

class ProfileViewModelFactory : ViewModelProvider.Factory {

    @Inject
    lateinit var userRepository: UserRepository

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        MessApp.appComponent.newProfileScreenComponent(ProfileScreenModule()).inject(this)
        return ProfileViewModel(userRepository) as T
    }
}