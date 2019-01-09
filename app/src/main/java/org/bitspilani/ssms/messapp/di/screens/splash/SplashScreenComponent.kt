package org.bitspilani.ssms.messapp.di.screens.splash

import dagger.Subcomponent
import org.bitspilani.ssms.messapp.screens.splash.core.SplashViewModelFactory

@Subcomponent(modules = [SplashScreenModule::class])
interface SplashScreenComponent {

    fun inject(factory: SplashViewModelFactory)
}