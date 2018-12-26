package org.bitspilani.ssms.messapp.di.app

import dagger.Component
import javax.inject.Singleton
import org.bitspilani.ssms.messapp.di.screens.menu.MenuScreenComponent
import org.bitspilani.ssms.messapp.di.screens.menu.MenuScreenModule

@Singleton @Component(modules = [AppModule::class])
interface AppComponent {

    fun newMenuScreenComponent(m1: MenuScreenModule): MenuScreenComponent
}