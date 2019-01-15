package org.bitspilani.ssms.messapp.screens.shared.data.room.setup

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import org.bitspilani.ssms.messapp.screens.contact.data.room.ContactsDao
import org.bitspilani.ssms.messapp.screens.contact.data.room.model.DataLayerContact
import org.bitspilani.ssms.messapp.screens.grub.shared.data.room.GrubBatchesDao
import org.bitspilani.ssms.messapp.screens.grub.shared.data.room.model.DataLayerGrubBatch
import org.bitspilani.ssms.messapp.screens.menu.data.room.MenuItemsDao
import org.bitspilani.ssms.messapp.screens.menu.data.room.MenuItemsTypeConverters
import org.bitspilani.ssms.messapp.screens.menu.data.room.model.DataLayerMenuItem
import org.bitspilani.ssms.messapp.screens.notice.data.room.NoticesDao
import org.bitspilani.ssms.messapp.screens.notice.data.room.NoticesTypeConverters
import org.bitspilani.ssms.messapp.screens.notice.data.room.model.DataLayerNotice

@Database(entities = [DataLayerMenuItem::class, DataLayerNotice::class, DataLayerGrubBatch::class, DataLayerContact::class], version = 1)
@TypeConverters(AppTypeConverters::class, MenuItemsTypeConverters::class, NoticesTypeConverters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun menuItemsDao(): MenuItemsDao

    abstract fun noticesDao(): NoticesDao

    abstract fun grubBatchesDao(): GrubBatchesDao

    abstract fun contactsDao(): ContactsDao
}