package org.bitspilani.ssms.messapp.screens.grub.shared.core.model

import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime

typealias Id = Long

/**
 * Represents the details common to all grubs.
 *
 * @property signUpDeadline  is the last date by which you can sign-up  for a grub.
 * @property cancelDeadline  is the last date by which you can cancel your signing.
 * @property slot  is the slot allotted to the user. It is null if a slot hasn't been allotted.
 * */
data class GrubDetails(
    val id: Id,
    val name: String,
    val organizer: String,
    val foodOption: FoodOption,
    val date: LocalDate,
    val slot1Time: LocalTime,
    val slot2Time: LocalTime,
    val signUpDeadline: LocalDate,
    val cancelDeadline: LocalDate,
    val signingStatus: SigningStatus,
    val slot: Slot?
)