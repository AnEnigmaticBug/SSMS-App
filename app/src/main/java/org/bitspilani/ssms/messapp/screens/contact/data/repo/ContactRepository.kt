package org.bitspilani.ssms.messapp.screens.contact.data.repo

import io.reactivex.Observable
import org.bitspilani.ssms.messapp.screens.contact.core.model.Contact

interface ContactRepository {

    fun getAllContacts(): Observable<List<Contact>>
}