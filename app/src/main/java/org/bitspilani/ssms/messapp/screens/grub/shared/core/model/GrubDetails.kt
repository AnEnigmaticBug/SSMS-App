package org.bitspilani.ssms.messapp.screens.grub.shared.core.model

import androidx.room.ColumnInfo
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
    @ColumnInfo(name = "grubId") val id: Id,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "organizer") val organizer: String,
    @ColumnInfo(name = "foodOption") val foodOption: FoodOption,
    @ColumnInfo(name = "date") val date: LocalDate,
    @ColumnInfo(name = "slot1Time") val slot1Time: LocalTime,
    @ColumnInfo(name = "slot2Time") val slot2Time: LocalTime,
    @ColumnInfo(name = "signUpDeadline") val signUpDeadline: LocalDate,
    @ColumnInfo(name = "cancelDeadline") val cancelDeadline: LocalDate,
    @ColumnInfo(name = "signingStatus") val signingStatus: SigningStatus,
    @ColumnInfo(name = "slot") val slot: Slot?
)