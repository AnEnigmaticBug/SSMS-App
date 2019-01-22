package org.bitspilani.ssms.messapp.di.screens.menu

import dagger.Subcomponent
import org.bitspilani.ssms.messapp.screens.menu.core.MenuViewModelFactory

@Subcomponent(modules = [MenuScreenModule::class])
interface MenuScreenComponent {

    fun inject(factory: MenuViewModelFactory)
}