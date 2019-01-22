package org.bitspilani.ssms.messapp.screens.shared.data.room.setup

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import org.bitspilani.ssms.messapp.screens.contact.data.room.ContactsDao
import org.bitspilani.ssms.messapp.screens.contact.data.room.model.DataLayerContact
import org.bitspilani.ssms.messapp.screens.grub.shared.data.room.GrubsDao
import org.bitspilani.ssms.messapp.screens.grub.shared.data.room.GrubBatchesTypeConverters
import org.bitspilani.ssms.messapp.screens.grub.shared.data.room.model.DataLayerGrub
import org.bitspilani.ssms.messapp.screens.grub.shared.data.room.model.DataLayerGrubBatch
import org.bitspilani.ssms.messapp.screens.grub.shared.data.room.model.DataLayerTicket
import org.bitspilani.ssms.messapp.screens.menu.data.room.MenusTypeConverters
import org.bitspilani.ssms.messapp.screens.menu.data.room.MenusDao
import org.bitspilani.ssms.messapp.screens.menu.data.room.model.*
import org.bitspilani.ssms.messapp.screens.notice.data.room.NoticesDao
import org.bitspilani.ssms.messapp.screens.notice.data.room.NoticesTypeConverters
import org.bitspilani.ssms.messapp.screens.notice.data.room.model.DataLayerNotice

@Database(entities = [MenuItemTuple::class, MealTuple::class, ServingTuple::class, RatingTuple::class, DataLayerNotice::class, DataLayerGrub::class, DataLayerGrubBatch::class, DataLayerTicket::class, DataLayerContact::class], version = 1)
@TypeConverters(AppTypeConverters::class, MenusTypeConverters::class, NoticesTypeConverters::class, GrubBatchesTypeConverters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun menusDao(): MenusDao

    abstract fun noticesDao(): NoticesDao

    abstract fun grubBatchesDao(): GrubsDao

    abstract fun contactsDao(): ContactsDao
}