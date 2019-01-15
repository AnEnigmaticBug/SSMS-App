package org.bitspilani.ssms.messapp.screens.contact.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.bitspilani.ssms.messapp.MessApp
import org.bitspilani.ssms.messapp.di.screens.contact.ContactScreenModule
import org.bitspilani.ssms.messapp.screens.contact.data.repo.ContactRepository
import javax.inject.Inject

class ContactViewModelFactory : ViewModelProvider.Factory {

    @Inject
    lateinit var contactRepository: ContactRepository

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        MessApp.appComponent.newContactScreenComponent(ContactScreenModule()).inject(this)
        return ContactViewModel(contactRepository) as T
    }
}