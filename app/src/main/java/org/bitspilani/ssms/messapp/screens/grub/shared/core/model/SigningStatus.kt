package org.bitspilani.ssms.messapp.screens.grub.shared.core.model

/**
 * [SignedForVeg]  means that the  user has signed  up for the vegetarian menu.
 * [SignedForNonVeg]  means that the user has  signed up for  the non-veg menu.
 * [Available]  means that  the user hasn't signed up for  the grub but he can.
 * [NotAvailable] means that the user hasn't signed up for the grub & he can't.
 * */
enum class SigningStatus {

    SignedForVeg, SignedForNonVeg, Available, NotAvailable
}