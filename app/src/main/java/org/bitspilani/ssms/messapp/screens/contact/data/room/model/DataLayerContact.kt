package org.bitspilani.ssms.messapp.screens.contact.data.room.model

import androidx.room.Entity

@Entity(tableName = "Contacts", primaryKeys = ["name", "post"])
data class DataLayerContact(val name: String, val post: String, val picUrl: String, val phone: String)