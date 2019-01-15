package org.bitspilani.ssms.messapp.di.screens.contact

import dagger.Module
import dagger.Provides
import org.bitspilani.ssms.messapp.screens.contact.data.repo.ContactRepository
import org.bitspilani.ssms.messapp.screens.contact.data.repo.ContactRepositoryImpl
import org.bitspilani.ssms.messapp.screens.contact.data.retrofit.ContactService
import org.bitspilani.ssms.messapp.screens.contact.data.room.ContactsDao
import org.bitspilani.ssms.messapp.screens.shared.data.room.setup.AppDatabase
import org.bitspilani.ssms.messapp.util.NetworkWatcher
import retrofit2.Retrofit

@Module
class ContactScreenModule {

    @Provides
    fun providesContactRepository(contactsDao: ContactsDao, networkWatcher: NetworkWatcher, contactService: ContactService): ContactRepository = ContactRepositoryImpl(contactsDao, networkWatcher, contactService)

    @Provides
    fun providesContactService(retrofit: Retrofit): ContactService = retrofit.create(ContactService::class.java)

    @Provides
    fun providesContactsDao(appDatabase: AppDatabase): ContactsDao = appDatabase.contactsDao()
}