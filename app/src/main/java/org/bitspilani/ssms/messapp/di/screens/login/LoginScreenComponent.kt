package org.bitspilani.ssms.messapp.di.screens.login

import dagger.Subcomponent
import org.bitspilani.ssms.messapp.screens.login.core.LoginViewModelFactory

@Subcomponent(modules = [LoginScreenModule::class])
interface LoginScreenComponent {

    fun inject(factory: LoginViewModelFactory)
}