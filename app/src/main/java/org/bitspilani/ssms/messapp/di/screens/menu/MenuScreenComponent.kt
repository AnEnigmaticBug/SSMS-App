package org.bitspilani.ssms.messapp.di.screens.menu

import dagger.Subcomponent
import org.bitspilani.ssms.messapp.screens.menu.core.MenuViewModelFactory
import org.bitspilani.ssms.messapp.screens.menu.work.SendRatingWorker

@Subcomponent(modules = [MenuScreenModule::class])
interface MenuScreenComponent {

    fun inject(factory: MenuViewModelFactory)

    fun inject(worker: SendRatingWorker)
}