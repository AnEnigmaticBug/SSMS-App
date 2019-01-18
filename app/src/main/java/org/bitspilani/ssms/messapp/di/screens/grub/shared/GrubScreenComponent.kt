package org.bitspilani.ssms.messapp.di.screens.grub.shared

import dagger.Subcomponent
import org.bitspilani.ssms.messapp.screens.grub.grubdetails.core.GrubDetailsViewModelFactory
import org.bitspilani.ssms.messapp.screens.grub.grublist.core.GrubListViewModelFactory

@Subcomponent(modules = [GrubScreenModule::class])
interface GrubScreenComponent {

    fun inject(factory: GrubListViewModelFactory)

    fun inject(factory: GrubDetailsViewModelFactory)
}