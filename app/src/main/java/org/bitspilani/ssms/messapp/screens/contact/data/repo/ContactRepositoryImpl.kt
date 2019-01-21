package org.bitspilani.ssms.messapp.screens.contact.data.repo

import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.bitspilani.ssms.messapp.screens.contact.core.model.Contact
import org.bitspilani.ssms.messapp.screens.contact.data.retrofit.ContactService
import org.bitspilani.ssms.messapp.screens.contact.data.room.ContactsDao
import org.bitspilani.ssms.messapp.screens.contact.data.room.model.DataLayerContact
import org.bitspilani.ssms.messapp.util.NetworkWatcher
import org.bitspilani.ssms.messapp.util.NoDataSourceException
import org.bitspilani.ssms.messapp.util.getBody

class ContactRepositoryImpl(
    private val contactsDao: ContactsDao,
    private val networkWatcher: NetworkWatcher,
    private val contactService: ContactService
) : ContactRepository {

    override fun getAllContacts(): Observable<List<Contact>> {
        return contactsDao.getContactCount()
            .take(1)
            .flatMap { _count ->
                when {
                    _count <= 0 -> {
                        when(networkWatcher.isConnectedToInternet()) {
                            true  -> Flowable.concat(fetchAndUpdateContacts().toFlowable(), contactsDao.getAllContacts())
                            false -> throw NoDataSourceException()
                        }
                    }
                    else        -> Flowable.merge(fetchAndUpdateContacts().toFlowable(), contactsDao.getAllContacts())
                }
            }
            .onErrorResumeNext(contactsDao.getAllContacts())
            .map { _contacts -> _contacts.map { Contact(it.name, it.post, it.picUrl, it.phone) } }
            .filter { it.isNotEmpty() }
            .subscribeOn(Schedulers.io())
            .toObservable()
    }

    private fun fetchAndUpdateContacts(): Single<List<DataLayerContact>> {
        return contactService.getAllContacts()
            .map { _response ->
                _response.getBody()
            }
            .map { _contacts ->
                _contacts.map { DataLayerContact(it.name, it.post, it.picUrl, it.phone) }
            }
            .doOnSuccess { _contacts ->
                contactsDao.deleteAllContacts()
                contactsDao.insertContacts(_contacts)
            }
            .subscribeOn(Schedulers.io())
    }
}