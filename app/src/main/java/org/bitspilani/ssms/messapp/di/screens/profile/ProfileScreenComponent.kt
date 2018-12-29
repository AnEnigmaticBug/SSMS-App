package org.bitspilani.ssms.messapp.di.screens.profile

import dagger.Subcomponent
import org.bitspilani.ssms.messapp.screens.profile.core.ProfileViewModelFactory

@Subcomponent(modules = [ProfileScreenModule::class])
interface ProfileScreenComponent {

    fun inject(factory: ProfileViewModelFactory)
}