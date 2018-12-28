package org.bitspilani.ssms.messapp.screens.shared.core.model

/**
 * Represents a user of the app.
 * @param id  the BITS id of the user.
 * @param name  the name of the user as registered in BITS.
 * @param room  hostel + room number of the user.
 * @param profilePicUrl  the user's profile pic's URL.
 * @param qrCode  the QR code unique to the user.
 * @param jwt  the JWT(Json Web Token) associated with the user.
 * */
data class User(
    val id: String,
    val name: String,
    val room: String,
    val profilePicUrl: String,
    val qrCode: String,
    val jwt: String
)