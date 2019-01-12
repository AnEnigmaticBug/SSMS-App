package org.bitspilani.ssms.messapp.screens.grub.grublist.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.bitspilani.ssms.messapp.MessApp
import org.bitspilani.ssms.messapp.di.screens.grub.shared.GrubScreenModule
import org.bitspilani.ssms.messapp.screens.grub.shared.data.repo.GrubRepository
import javax.inject.Inject

class GrubListViewModelFactory : ViewModelProvider.Factory {

    @Inject
    lateinit var grubRepository: GrubRepository

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        MessApp.appComponent.newGrubScreenComponent(GrubScreenModule()).inject(this)
        return GrubListViewModel(grubRepository) as T
    }
}