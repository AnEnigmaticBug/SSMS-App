package org.bitspilani.ssms.messapp.screens.menu.core

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.bitspilani.ssms.messapp.MessApp
import org.bitspilani.ssms.messapp.di.screens.menu.MenuScreenModule
import org.bitspilani.ssms.messapp.screens.menu.data.repo.MenuRepository
import javax.inject.Inject

class MenuViewModelFactory : ViewModelProvider.Factory {

    @Inject
    lateinit var menuRepository: MenuRepository

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        MessApp.appComponent.newMenuScreenComponent(MenuScreenModule()).inject(this)
        Log.d("MenuViewModelFactory", "create")
        return MenuViewModel(menuRepository) as T
    }
}