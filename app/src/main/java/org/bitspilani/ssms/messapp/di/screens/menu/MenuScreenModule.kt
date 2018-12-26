package org.bitspilani.ssms.messapp.di.screens.menu

import dagger.Module
import dagger.Provides
import org.bitspilani.ssms.messapp.screens.menu.data.repo.MenuRepository
import org.bitspilani.ssms.messapp.screens.menu.data.repo.MenuRepositoryImpl

@Module
class MenuScreenModule {

    @Provides
    fun providesMenuRepository(): MenuRepository = MenuRepositoryImpl()
}