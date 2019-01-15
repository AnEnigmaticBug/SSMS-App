package org.bitspilani.ssms.messapp.di.screens.contact

import dagger.Subcomponent
import org.bitspilani.ssms.messapp.screens.contact.core.ContactViewModelFactory

@Subcomponent(modules = [ContactScreenModule::class])
interface ContactScreenComponent {

    fun inject(factory: ContactViewModelFactory)
}