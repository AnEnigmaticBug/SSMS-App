package org.bitspilani.ssms.messapp.di.screens.menu

import dagger.Module
import dagger.Provides
import org.bitspilani.ssms.messapp.screens.menu.data.repo.MenuRepository
import org.bitspilani.ssms.messapp.screens.menu.data.repo.MenuRepositoryImpl
import org.bitspilani.ssms.messapp.screens.menu.data.room.MenuItemsDao
import org.bitspilani.ssms.messapp.screens.shared.data.room.setup.AppDatabase

@Module
class MenuScreenModule {

    @Provides
    fun providesMenuRepository(menuItemsDao: MenuItemsDao): MenuRepository = MenuRepositoryImpl(menuItemsDao)

    @Provides
    fun providesMenuItemsDao(appDatabase: AppDatabase): MenuItemsDao = appDatabase.menuItemsDao()
}