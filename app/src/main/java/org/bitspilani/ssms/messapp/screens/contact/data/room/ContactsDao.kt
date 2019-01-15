package org.bitspilani.ssms.messapp.screens.contact.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Flowable
import org.bitspilani.ssms.messapp.screens.contact.data.room.model.DataLayerContact

@Dao
interface ContactsDao {

    @Query("SELECT * FROM Contacts")
    fun getAllContacts(): Flowable<List<DataLayerContact>>

    @Query("SELECT COUNT(name) FROM Contacts")
    fun getContactCount(): Flowable<Int>

    @Insert(onConflict = OnConflictStrategy.FAIL)
    fun insertContacts(contacts: List<DataLayerContact>)

    @Query("DELETE FROM Contacts")
    fun deleteAllContacts()
}